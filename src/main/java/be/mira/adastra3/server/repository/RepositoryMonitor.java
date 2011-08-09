/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.repository.subversion.RepositoryEditor;
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
            } catch (RepositoryException tException) {
                Repository.getInstance().emitError("could not update repository", tException);
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
        } catch (SVNException tException) {
            throw new ServiceSetupException(tException);
        }

        // Monitor timer
        Integer tInterval = Integer.parseInt(getProperty("interval", "60"));
        if (tInterval <= 0) {
            throw new ServiceSetupException("Update interval out of valid range");
        }
        mSVNMonitorInterval = tInterval;
        getLogger().debug("Scheduling SVN monitor with interval of " + tInterval + " seconds");
        mSVNMonitor = new Timer();
    }


    //
    // Service interface
    //

    @Override
    public final void run() throws ServiceRunException {
        // Do a checkout
        getLogger().debug("Checking out the repository");
        try {
            checkout();
        } catch (RepositoryException tException) {
            throw new ServiceRunException("Could not perform initial checkout", tException);
        }

        // Schedule the monitor
        mSVNMonitor.schedule(new Monitor(), 0, mSVNMonitorInterval * 1000);
    }

    @Override
    public final void stop() throws ServiceRunException {
    }


    //
    // Auxiliary
    //

    final void checkout() throws RepositoryException  {
        try {
            mSVNRevision = mSVNRepository.getLatestRevision();

            // Checkout the repository
            getLogger().debug("Checking out configurations");
            ISVNReporterBaton tConfigurationBaton = new DummyBaton(mSVNRevision);
            RepositoryEditor tRepositoryEditor = new RepositoryEditor();
            mSVNRepository.update(mSVNRevision, "configurations", true, tConfigurationBaton, tRepositoryEditor);
            
            // Process the configurations
            // TODO: detect the changed configurations
            Repository tRepository = Repository.getInstance();
            List<Configuration> tConfigurations = tRepositoryEditor.getConfigurations();
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
                        } else if (tKioskConfiguration.getRevision() > tOldKioskConfiguration.getRevision()) {
                            getLogger().debug("New configuration is a more recent version of an existing configuration, updating the repository");
                            tRepository.updateKioskConfiguration(tKioskConfiguration);
                        } else {
                            getLogger().debug("Configuration hasn't been updated, ignoring");
                        }
                    } else {
                        throw new RepositoryException("unknown configuration type");
                    }
                } catch (RepositoryException tException) {
                    throw new RepositoryException("could not process configuration", tException);
                }
            }
            
            // Process the media
            // TODO: detect the changed media
            //       this is only needed when we detect the changed configurations,
            //       because now we reload each configuration anyhow
        } catch (SVNException tException) {
            throw new RepositoryException("SVN checkout failed", tException);
        }
    }
    
    private void update() throws RepositoryException {
        long tSVNRevision;
        try {
            tSVNRevision = mSVNRepository.getLatestRevision();
        } catch (SVNException tException) {
            throw new RepositoryException("could not fetch SVN revision", tException);
        }
        
        if (tSVNRevision != mSVNRevision) {
            getLogger().info("SVN repository changed from revision " + mSVNRevision + " to " + tSVNRevision);
            checkout();
            mSVNRevision = tSVNRevision;
        }
    }
}
