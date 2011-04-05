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
import java.util.Timer;
import java.util.TimerTask;
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
    private long mSVNRevision;
    private Timer mSVNMonitor;
    private int mSVNMonitorInterval;


    //
    // Auxiliary classes
    //

    private class Monitor extends TimerTask {
        @Override
        public void run() {
            getLogger().debug("Checking SVN revision");
            try {
                long tSVNRevision = mSVNRepository.getLatestRevision();
                if (tSVNRevision != mSVNRevision) {
                    getLogger().info("SVN repository changed from revision " + mSVNRevision + " to " + tSVNRevision);

                    // TODO

                    mSVNRevision = tSVNRevision;
                }
            }
            catch (SVNException e) {
                getLogger().error("Error monitoring SVN repository", e);
            }
        }
    }


    //
    // Construction and destruction
    //

    public RepositoryMonitor() throws ServiceSetupException {
        DAVRepositoryFactory.setup();

        // DAV location
        mDAVLocation = "http://"
                + getProperty("host", "localhost")
                + getProperty("path", "/repository");
        getLogger().debug("SVN repository DAV location: " + mDAVLocation);
        
        // SVN repository
        try {
            SVNURL tSVNLocation = SVNURL.parseURIDecoded(mDAVLocation);
            mSVNRepository = SVNRepositoryFactory.create(tSVNLocation, null);
        }
        catch (SVNException e) {
            throw new ServiceSetupException(e);
        }

        // Monitor timer
        Integer iInterval = Integer.parseInt(getProperty("interval", "60"));
        if (iInterval <= 0)
            throw new ServiceSetupException("Server port out of valid range");
        getLogger().debug("Scheduling SVN monitor with interval of " + iInterval + " s");
        mSVNMonitor = new Timer();
        mSVNMonitorInterval = iInterval;
    }


    //
    // Service interface
    //

    @Override
    public void run() throws ServiceRunException {
        // Do a checkout
        checkout();

        // Schedule the monitor
        mSVNMonitor.schedule(new Monitor(), mSVNMonitorInterval * 1000);
    }

    @Override
    public void stop() throws ServiceRunException {
    }


    //
    // Auxiliary
    //

    void checkout() throws ServiceRunException  {
        try {
            mSVNRevision = mSVNRepository.getLatestRevision();

            getLogger().debug("Checking out configurations");
            ISVNReporterBaton tConfigurationBaton = new DummyBaton(mSVNRevision);
            ISVNEditor tConfigurationEditor = new ConfigurationEditor();
            mSVNRepository.update(mSVNRevision, "configurations", true, tConfigurationBaton, tConfigurationEditor);

            getLogger().debug("Checking out kiosks");
            ISVNReporterBaton tKioskBaton = new DummyBaton(mSVNRevision);
            ISVNEditor tKioskEditor = new KioskConfigurationEditor();
            mSVNRepository.update(mSVNRevision, "kiosks", true, tKioskBaton, tKioskEditor);
        }
        catch (SVNException e) {
            getLogger().error("SVN checkout failed", e);
        }
    }
}
