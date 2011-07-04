/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.repository.subversion.ConfigurationEditor;
import be.mira.adastra3.server.repository.subversion.DummyBaton;
import be.mira.adastra3.server.Service;
import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import be.mira.adastra3.server.repository.configurations.Configuration;
import be.mira.adastra3.server.repository.configurations.KioskConfiguration;
import java.util.List;
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
                update();
            }
            catch (RepositoryException iException) {
                Repository.getInstance().emitError("could not update repository", iException);
            }
        }
    }


    //
    // Construction and destruction
    //

    public RepositoryMonitor() throws ServiceSetupException {
        DAVRepositoryFactory.setup();
        Repository tRepository = Repository.getInstance();

        // DAV location
        mDAVLocation = "http://"
                + getProperty("host", "localhost")
                + getProperty("path", "/repository");
        getLogger().debug("SVN repository DAV location: " + mDAVLocation);
        tRepository.setServer(mDAVLocation);
        
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
            throw new ServiceSetupException("Update interval out of valid range");
        mSVNMonitorInterval = iInterval;
        getLogger().debug("Scheduling SVN monitor with interval of " + iInterval + " seconds");
        mSVNMonitor = new Timer();
    }


    //
    // Service interface
    //

    @Override
    public void run() throws ServiceRunException {
        // Do a checkout
        getLogger().debug("Checking out the repository");
        try {
            checkout();
        } catch (RepositoryException iException) {
            throw new ServiceRunException("Could not perform initial checkout", iException);
        }

        // Schedule the monitor
        mSVNMonitor.schedule(new Monitor(), 0, mSVNMonitorInterval * 1000);
    }

    @Override
    public void stop() throws ServiceRunException {
    }


    //
    // Auxiliary
    //

    void checkout() throws RepositoryException  {
        try {
            mSVNRevision = mSVNRepository.getLatestRevision();

            // Checkout the new configurations
            getLogger().debug("Checking out configurations");
            ISVNReporterBaton tConfigurationBaton = new DummyBaton(mSVNRevision);
            ISVNEditor tConfigurationEditor = new ConfigurationEditor();
            mSVNRepository.update(mSVNRevision, "configurations", true, tConfigurationBaton, tConfigurationEditor);
            
            // Process them
            Repository tRepository = Repository.getInstance();
            List<Configuration> tConfigurations = ((ConfigurationEditor)tConfigurationEditor).getConfigurations();
            for (Configuration tConfiguration : tConfigurations) {
                try {
                    // KioskConfiguration processing
                    if (tConfiguration instanceof KioskConfiguration) {
                        KioskConfiguration tKioskConfiguration = (KioskConfiguration) tConfiguration;
                        getLogger().debug("Processing kiosk configuration " + tKioskConfiguration.getId());
                        KioskConfiguration tOldKioskConfiguration = tRepository.getKioskConfiguration(tKioskConfiguration.getId());
                        if (tOldKioskConfiguration == null) {
                            getLogger().debug("Configuration seems new, adding to repository");
                            tRepository.addKioskConfiguration(tKioskConfiguration);
                        }
                        else if (tKioskConfiguration.getRevision() > tOldKioskConfiguration.getRevision()) {
                            getLogger().debug("New configuration is a more recent version of an existing configuration, updating the repository");
                            tRepository.updateKioskConfiguration(tKioskConfiguration);
                        }
                        else
                            getLogger().debug("Configuration hasn't been updated, ignoring");
                    }
                    else
                        throw new RepositoryException("unknown configuration type");
                }
                catch (RepositoryException iException) {
                    throw new RepositoryException("could not process configuration", iException);
                }
            }
        }
        catch (SVNException e) {
            throw new RepositoryException("SVN checkout failed", e);
        }
    }
    
    private void update() throws RepositoryException {
        long tSVNRevision;
        try {
            tSVNRevision = mSVNRepository.getLatestRevision();
        }
        catch (SVNException iException) {
            throw new RepositoryException("could not fetch SVN revision", iException);
        }
        
        if (tSVNRevision != mSVNRevision) {
            getLogger().info("SVN repository changed from revision " + mSVNRevision + " to " + tSVNRevision);
            checkout();
            mSVNRevision = tSVNRevision;
        }
    }
}
