/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.subversion;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.ConfigurationReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
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
public class ConfigurationEditor implements ISVNEditor {
    //
    // Data members
    //

    private static Logger mLogger = Logger.getLogger(ConfigurationEditor.class);
    private ByteArrayOutputStream mTemporaryStream;
    private SVNDeltaProcessor myDeltaProcessor;
    protected String mDataIdentifier;
    protected String mRepositoryDirectory = "configurations";


    //
    // Construction and destruction
    //

    public ConfigurationEditor() {
        /*
         * Utility class that will help us to transform 'deltas' sent by the
         * server to the new file contents.
         */
        myDeltaProcessor = new SVNDeltaProcessor();
    }


    //
    // ISVNEditor interface
    //

    /*
     * Server reports revision to which application of the further
     * instructions will update working copy to.
     */
    @Override
    public void targetRevision(long revision) throws SVNException {
        mLogger.trace("Next instructions target revision " + revision);
    }

    /*
     * Called before sending other instructions.
     */
    @Override
    public void openRoot(long revision) throws SVNException {
        mLogger.trace("Opening root at revision " + revision);
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
    public void addDir(String path, String copyFromPath, long copyFromRevision) throws SVNException {
        mLogger.trace("Adding directory '" + path + "'");
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
    public void openDir(String path, long revision) throws SVNException {
        mLogger.trace("Opening directory '" + path + "'");
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
    public void changeDirProperty(String name, SVNPropertyValue property) throws SVNException {
        mLogger.trace("Changing properties of directory '" + name + "'");
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
    public void addFile(String path, String copyFromPath, long copyFromRevision) throws SVNException {
        mLogger.trace("Adding file '" + path + "'");
        if (copyFromPath != null ) {
            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "Current editor does not support copying files");
            throw new SVNException(err);
        }
        File tFile = new File(path);

        // Check if file is in the appropriate directory
        File tFileParent = tFile.getParentFile();
        if (tFileParent == null || ! tFileParent.getName().equals(mRepositoryDirectory) || tFileParent.getParentFile() != null) {
            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "File in wrong directory found");
            throw new SVNException(err);
        }

        // Check extension
        int tDotPosition = tFile.getName().lastIndexOf('.');
        if (tDotPosition == -1) {
            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "File without extension found");
            throw new SVNException(err);
        }
        if (tDotPosition == 0) {
            mLogger.debug("Ignoring hidden file");
            return;
        }
        String tFileExtension = tFile.getName().substring(tDotPosition+1);
        if (! tFileExtension.equalsIgnoreCase("xml")) {
            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "File with unknown extension found");
            throw new SVNException(err);
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
    public void openFile(String path, long revision) throws SVNException {
        SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "Current editor does not support opening existing files");
        throw new SVNException(err);
    }

    /*
     * Instructs to add, modify or delete file property.
     * In this example we skip this instruction, but 'real' export operation
     * may inspect 'svn:eol-style' or 'svn:mime-type' property values to
     * transfor file contents propertly after receiving.
     */
    @Override
    public void changeFileProperty(String path, String name, SVNPropertyValue property) throws SVNException {
    }

    /*
     * Called before sending 'delta' for a file. Delta may include instructions
     * on how to create a file or how to modify existing file. In this example
     * delta will always contain instructions on how to create a new file and so
     * we set up deltaProcessor with 'null' base file and target file to which we would
     * like to store the result of delta application.
     */
    @Override
    public void applyTextDelta(String path, String baseChecksum) throws SVNException {
        if (mTemporaryStream == null)
            return;

        myDeltaProcessor.applyTextDelta((InputStream) null, mTemporaryStream, false);
    }

    /*
     * Server sends deltas in form of 'diff windows'. Depending on the file size
     * there may be several diff windows. Utility class SVNDeltaProcessor processes
     * these windows for us.
     */
    @Override
    public OutputStream textDeltaChunk(String path, SVNDiffWindow diffWindow) throws SVNException {
        if (mTemporaryStream == null)
            return null;

        return myDeltaProcessor.textDeltaChunk(diffWindow);
    }

    /*
     * Called when all diff windows (delta) is transferred.
     */
    @Override
    public void textDeltaEnd(String path) throws SVNException {
        if (mTemporaryStream == null)
            return;

        myDeltaProcessor.textDeltaEnd();
    }

    /*
     * Called when file update is completed.
     * This call always matches addFile or openFile call.
     */
    @Override
    public void closeFile(String path, String textChecksum) throws SVNException {
        mLogger.trace("Closing file '" + path + "'");
        if (mTemporaryStream == null)
            return;

        // Read and proces the received data        
        try {
            ConfigurationReader tReader = new ConfigurationReader(new ByteArrayInputStream(mTemporaryStream.toByteArray()));
            tReader.process();
        }
        catch (RepositoryException e) {
            // TODO: bug in SVNKIt, the inner error does not get printed
            // http://old.nabble.com/Passing-an-exception-to-SVNException-td31171795.html
            mLogger.error("SVNKit inner exception", e);
            
            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.IO_ERROR, "Could not process file contents");
            throw new SVNException(err, e);
        }
        finally {
            mTemporaryStream = null;
            mDataIdentifier = null;
        }
    }

    /*
     * Called when all child files and directories are processed.
     * This call always matches addDir, openDir or openRoot call.
     */
    @Override
    public void closeDir() throws SVNException {
        mLogger.trace("Closing directory");
    }

    /*
     * Insturcts to delete an entry in the 'working copy'. Of course will not be
     * called during export operation.
     */
    @Override
    public void deleteEntry(String path, long revision) throws SVNException {
        mLogger.trace("Deleting entry '" + path + "'");
    }

    /*
     * Called when directory at 'path' should be somehow processed,
     * but authenticated user (or anonymous user) doesn't have enough
     * access rights to get information on this directory (properties, children).
     */
    @Override
    public void absentDir(String path) throws SVNException {
        mLogger.trace("Access denied to directory '" + path + "' (will be marked as absent)");
    }

    /*
     * Called when file at 'path' should be somehow processed,
     * but authenticated user (or anonymous user) doesn't have enough
     * access rights to get information on this file (contents, properties).
     */
    @Override
    public void absentFile(String path) throws SVNException {
        mLogger.trace("Access denied to file '" + path + "' (will be marked as absent)");
    }

    /*
     * Called when update is completed.
     */
    @Override
    public SVNCommitInfo closeEdit() throws SVNException {
        mLogger.trace("Closing an edit");

        return null;
    }

    /*
     * Called when update is completed with an error or server
     * requests client to abort update operation.
     */
    @Override
    public void abortEdit() throws SVNException {
        mLogger.trace("Aborting an edit");
    }
}
