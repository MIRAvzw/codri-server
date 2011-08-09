/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.subversion;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.ConfigurationReader;
import be.mira.adastra3.server.repository.configurations.Configuration;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.diff.SVNDeltaProcessor;
import org.tmatesoft.svn.core.io.diff.SVNDiffWindow;

/**
 * Deze klasse zal de wijzigingen opegehaald van de server, interpreteren om
 * zo de working copy up to date te brengen.
 * 
 * @author tim
 */
public class RepositoryEditor implements ISVNEditor {
    //
    // Data members
    //

    private static Logger LOGGER = Logger.getLogger(RepositoryEditor.class);
    private ByteArrayOutputStream mTemporaryStream;
    private SVNDeltaProcessor mDeltaProcessor;
    private String mDataIdentifier;
    private String mRepositoryDirectory = "configurations";
    private long mRevision;
    private List<Configuration> mConfigurations;


    //
    // Construction and destruction
    //

    public RepositoryEditor() {
        mDeltaProcessor = new SVNDeltaProcessor();
        
        mConfigurations = new ArrayList<Configuration>();
    }
    
    
    //
    // Getters and setters
    //
    
    public final List<Configuration> getConfigurations() {
        return mConfigurations;
    }


    //
    // ISVNEditor interface
    //

    /*
     * Server reports revision to which application of the further
     * instructions will update working copy to.
     */
    @Override
    public final void targetRevision(final long iRevision) throws SVNException {
        LOGGER.trace("Next instructions target revision " + iRevision);
        mRevision = iRevision;
    }

    /*
     * Called before sending other instructions.
     */
    @Override
    public final void openRoot(final long iRevision) throws SVNException {
        LOGGER.trace("Opening root at revision " + iRevision);
    }

    /*
     * Called when a new directory has to be added.
     *
     * For each 'addDir' call server will call 'closeDir' method after
     * all children of the added directory are added.
     *
     * This implementation creates corresponding directory below root directory.
     */
    @Override
    public final void addDir(final String iPath, final String iCopyFromPath, final long iCopyFromRevision) throws SVNException {
        LOGGER.trace("Adding directory '" + iPath + "'");
    }

    /*
     * Called when there is an existing directory that has to be 'opened' either
     * to modify this directory properties or to process other files and directories
     * inside this directory.
     *
     * In case of export this method will never be called because we reported
     * that our 'working copy' is empty and so server knows that there are
     * no 'existing' directories.
     */
    @Override
    public final void openDir(final String iPath, final long iRevision) throws SVNException {
        LOGGER.trace("Opening directory '" + iPath + "'");
    }

    /*
     * Instructs to change opened or added directory property.
     *
     * This method is called to update properties set by the user as well
     * as those created automatically, like "svn:committed-rev".
     * See SVNProperty class for default property names.
     *
     * When property has to be deleted value will be 'null'.
     */
    @Override
    public final void changeDirProperty(final String iName, final SVNPropertyValue iProperty) throws SVNException {
        LOGGER.trace("Changing properties of directory '" + iName + "'");
    }

    /*
     * Called when a new file has to be created.
     *
     * For each 'addFile' call server will call 'closeFile' method after
     * sending file properties and contents.
     *
     * This implementation creates empty file below root directory, file contents
     * will be updated later, and for empty files may not be sent at all.
     */
    @Override
    public final void addFile(final String iPath, final String iCopyFromPath, final long iCopyFromRevision) throws SVNException {
        LOGGER.trace("Adding file '" + iPath + "'");
        if (iCopyFromPath != null) {
            SVNErrorMessage tError = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "Current editor does not support copying files");
            throw new SVNException(tError);
        }
        File tFile = new File(iPath);

        // Check if file is in the appropriate directory
        File tFileParent = tFile.getParentFile();
        if (tFileParent == null || ! tFileParent.getName().equals(mRepositoryDirectory) || tFileParent.getParentFile() != null) {
            SVNErrorMessage tError = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "File in wrong directory found");
            throw new SVNException(tError);
        }

        // Check extension
        int tDotPosition = tFile.getName().lastIndexOf('.');
        if (tDotPosition == -1) {
            SVNErrorMessage tError = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "File without extension found");
            throw new SVNException(tError);
        }
        if (tDotPosition == 0) {
            LOGGER.debug("Ignoring hidden file");
            return;
        }
        String tFileExtension = tFile.getName().substring(tDotPosition+1);
        if (! tFileExtension.equalsIgnoreCase("xml")) {
            SVNErrorMessage tError = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "File with unknown extension found");
            throw new SVNException(tError);
        }
        String tFileBasename = tFile.getName().substring(0, tDotPosition);

        // Prepare state
        mDataIdentifier = tFileBasename;
        mTemporaryStream = new ByteArrayOutputStream();
    }

    /*
     * Called when there is an existing files that has to be 'opened' either
     * to modify file contents or properties.
     *
     * In case of export this method will never be called because we reported
     * that our 'working copy' is empty and so server knows that there are
     * no 'existing' files.
     */
    @Override
    public final void openFile(final String iPath, final long iRevision) throws SVNException {
        SVNErrorMessage tError = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "Current editor does not support opening existing files");
        throw new SVNException(tError);
    }

    /*
     * Instructs to add, modify or delete file property.
     * In this example we skip this instruction, but 'real' export operation
     * may inspect 'svn:eol-style' or 'svn:mime-type' property values to
     * transfor file contents propertly after receiving.
     */
    @Override
    public final void changeFileProperty(final String iPath, final String iName, final SVNPropertyValue iProperty) throws SVNException {
        LOGGER.trace("Changing properties of file '" + iName + "'");
    }

    /*
     * Called before sending 'delta' for a file. Delta may include instructions
     * on how to create a file or how to modify existing file. In this example
     * delta will always contain instructions on how to create a new file and so
     * we set up deltaProcessor with 'null' base file and target file to which we would
     * like to store the result of delta application.
     */
    @Override
    public final void applyTextDelta(final String iPath, final String iBaseChecksum) throws SVNException {
        if (mTemporaryStream == null) {
            return;
        }

        mDeltaProcessor.applyTextDelta((InputStream) null, mTemporaryStream, false);
    }

    /*
     * Server sends deltas in form of 'diff windows'. Depending on the file size
     * there may be several diff windows. Utility class SVNDeltaProcessor processes
     * these windows for us.
     */
    @Override
    public final OutputStream textDeltaChunk(final String iPath, final SVNDiffWindow iDiffWindow) throws SVNException {
        if (mTemporaryStream == null) {
            return null;
        }

        return mDeltaProcessor.textDeltaChunk(iDiffWindow);
    }

    /*
     * Called when all diff windows (delta) is transferred.
     */
    @Override
    public final void textDeltaEnd(final String iPath) throws SVNException {
        if (mTemporaryStream == null) {
            return;
        }

        mDeltaProcessor.textDeltaEnd();
    }

    /*
     * Called when file update is completed.
     * This call always matches addFile or openFile call.
     */
    @Override
    public final void closeFile(final String iPath, final String iTextChecksum) throws SVNException {
        LOGGER.trace("Closing file '" + iPath + "'");
        if (mTemporaryStream == null) {
            return;
        }

        // Read and proces the received data        
        try {
            ConfigurationReader tReader = new ConfigurationReader(mDataIdentifier, new ByteArrayInputStream(mTemporaryStream.toByteArray()));
            tReader.process();
            
            if (tReader.getConfiguration() != null) {
                tReader.getConfiguration().setRevision(mRevision);
                mConfigurations.add(tReader.getConfiguration());
            } else {
                throw new RepositoryException("found empty configuration file");
            }
        } catch (RepositoryException tException) {
            // TODO: bug in SVNKIt, the inner error does not get printed
            // http://old.nabble.com/Passing-an-exception-to-SVNException-td31171795.html
            LOGGER.error("SVNKit inner exception", tException);
            
            SVNErrorMessage tError = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "could not process file contents");
            throw new SVNException(tError, tException);
        } finally {
            mTemporaryStream = null;
            mDataIdentifier = null;
        }
    }

    /*
     * Called when all child files and directories are processed.
     * This call always matches addDir, openDir or openRoot call.
     */
    @Override
    public final void closeDir() throws SVNException {
        LOGGER.trace("Closing directory");
    }

    /*
     * Insturcts to delete an entry in the 'working copy'. Of course will not be
     * called during export operation.
     */
    @Override
    public final void deleteEntry(final String iPath, final long iRevision) throws SVNException {
        LOGGER.trace("Deleting entry '" + iPath + "'");
    }

    /*
     * Called when directory at 'path' should be somehow processed,
     * but authenticated user (or anonymous user) doesn't have enough
     * access rights to get information on this directory (properties, children).
     */
    @Override
    public final void absentDir(final String iPath) throws SVNException {
        LOGGER.trace("Access denied to directory '" + iPath + "' (will be marked as absent)");
    }

    /*
     * Called when file at 'path' should be somehow processed,
     * but authenticated user (or anonymous user) doesn't have enough
     * access rights to get information on this file (contents, properties).
     */
    @Override
    public final void absentFile(final String iPath) throws SVNException {
        LOGGER.trace("Access denied to file '" + iPath + "' (will be marked as absent)");
    }

    /*
     * Called when update is completed.
     */
    @Override
    public final SVNCommitInfo closeEdit() throws SVNException {
        LOGGER.trace("Closing an edit");

        return null;
    }

    /*
     * Called when update is completed with an error or server
     * requests client to abort update operation.
     */
    @Override
    public final void abortEdit() throws SVNException {
        LOGGER.trace("Aborting an edit");
    }
}
