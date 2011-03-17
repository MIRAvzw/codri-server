/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.repository;

import be.mira.adastra3.common.repository.DummyBaton;
import be.mira.adastra3.common.repository.ConfigurationEditor;
import be.mira.adastra3.common.repository.KioskConfigurationEditor;
import be.mira.adastra3.server.Service;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

/**
 *
 * @author tim
 */
public class RepositoryMonitor extends Service {
    //
    // Data members
    //

    private String mDAVLocation;
    private SVNRepository mSVNRepository;


    //
    // Construction and destruction
    //

    public RepositoryMonitor() throws ServiceSetupException {
        DAVRepositoryFactory.setup();

        // DAV location
        mDAVLocation = "http://"
                + getProperty("host", "localhost")
                + getProperty("path", "/repository");
        getLogger().debug("Repository DAV location: " + mDAVLocation);
        
        // SVN repository
        try {
            SVNURL tSVNLocation = SVNURL.parseURIDecoded(mDAVLocation);
            mSVNRepository = SVNRepositoryFactory.create(tSVNLocation, null);
        }
        catch (SVNException e) {
            throw new ServiceSetupException(e);
        }
    }


    //
    // Service interface
    //

    @Override
    public void run() throws ServiceRunException {
        // Do a checkout
        checkout();
    }

    @Override
    public void stop() throws ServiceRunException {
    }


    //
    // Auxiliary
    //

    void checkout() throws ServiceRunException  {
        try {
            long tSVNRepositoryRevision = mSVNRepository.getLatestRevision();

            getLogger().debug("Checking out configurations");
            ISVNReporterBaton tConfigurationBaton = new DummyBaton(tSVNRepositoryRevision);
            ISVNEditor tConfigurationEditor = new ConfigurationEditor();
            mSVNRepository.update(tSVNRepositoryRevision, "configurations", true, tConfigurationBaton, tConfigurationEditor);

            getLogger().debug("Checking out kiosks");
            ISVNReporterBaton tKioskBaton = new DummyBaton(tSVNRepositoryRevision);
            ISVNEditor tKioskEditor = new KioskConfigurationEditor();
            mSVNRepository.update(tSVNRepositoryRevision, "kiosks", true, tKioskBaton, tKioskEditor);
        }
        catch (SVNException e) {
            getLogger().error("SVN checkout failed", e);
        }
    }

}
