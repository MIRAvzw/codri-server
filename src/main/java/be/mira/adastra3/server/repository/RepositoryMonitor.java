/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.repository.processors.ConfigurationProcessor;
import be.mira.adastra3.server.Service;
import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import be.mira.adastra3.server.repository.configuration.Configuration;
import be.mira.adastra3.server.repository.connection.Connection;
import be.mira.adastra3.server.repository.presentation.Presentation;
import be.mira.adastra3.server.repository.processors.ConnectionProcessor;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.tigris.subversion.javahl.ClientException;
import org.tigris.subversion.javahl.Depth;
import org.tigris.subversion.javahl.Info2;
import org.tigris.subversion.javahl.InfoCallback;
import org.tigris.subversion.javahl.Revision;
import org.tigris.subversion.javahl.SVNClient;

/**
 *
 * @author tim
 */
public class RepositoryMonitor extends Service {
    //
    // Data members
    //

    private String mSVNLocation;
    private File mSVNCheckoutRoot;
    private SVNClient mSVNClient;
    private Timer mSVNMonitor;
    private int mSVNMonitorInterval;
    
    private long mConnectionsRevision;
    private long mConfigurationsRevision;
    private long mPresentationsRevision;


    //
    // Auxiliary classes
    //

    private class Monitor extends TimerTask {
        @Override
        public void run() {
            // TODO: squash these three cases in something using the 
            //       RepositoryEntity interface
            // Check the connections
            try {
                getLogger().debug("Checking the configurations");
                long tConnectionsRevision = checkConnections();
                if (mConnectionsRevision != tConnectionsRevision) {
                    getLogger().info("Connections changed to revision " + tConnectionsRevision);
                    mConnectionsRevision = tConnectionsRevision;
                    getConnections();
                    processConnections();
                }
            } catch (RepositoryException tException) {
                Repository.getInstance().emitError("could not update the connections", tException);
            }
            
            // Check the configurations
            try {
                getLogger().debug("Checking the configurations");
                long tConfigurationsRevision = checkConfigurations();
                if (mConfigurationsRevision != tConfigurationsRevision) {
                    getLogger().info("Configurations changed to revision " + tConfigurationsRevision);
                    mConfigurationsRevision = tConfigurationsRevision;
                    getConfigurations();
                    processConfigurations();
                }
            } catch (RepositoryException tException) {
                Repository.getInstance().emitError("could not update the configurations", tException);
            }
            
            // Check the presentations
            try {
                getLogger().debug("Checking the presentations");
                long tPresentationRevision = checkPresentations();
                if (mPresentationsRevision != tPresentationRevision) {
                    getLogger().info("Presentations changed to revision " + tPresentationRevision);
                    mPresentationsRevision = tPresentationRevision;
                    processPresentations();
                }
            } catch (RepositoryException tException) {
                Repository.getInstance().emitError("could not update the presentations", tException);
            }
        }
        
    }
    
    public class XMLFilter implements FilenameFilter {
      protected Pattern mPattern = Pattern.compile("\\.xml$", Pattern.CASE_INSENSITIVE);

      @Override
      public boolean accept(File iDirectory, String iFilename) {
          return mPattern.matcher(iFilename).find();
      }
    }


    //
    // Construction and destruction
    //

    public RepositoryMonitor() throws ServiceSetupException {
        // Subversion checkout root
        mSVNCheckoutRoot = new File(getConfiguration().getString("repository.checkout"));
        if (!mSVNCheckoutRoot.exists() || !mSVNCheckoutRoot.canWrite())
            throw new ServiceSetupException("checkout path does not exist or is not writable");
        
        // Subversion location
        Pattern tLocationPattern = Pattern.compile("^(https?|file|svn)://");
        mSVNLocation = getConfiguration().getString("repository.location");
        Matcher tLocationMatcher = tLocationPattern.matcher(mSVNLocation);
        if (!tLocationMatcher.find()) {
            throw new ServiceSetupException("repository location '" + mSVNLocation + "' is not a valid URL");
        }
        Repository.getInstance().setServer(mSVNLocation);
        mSVNClient = new SVNClient();

        // Monitor timer
        Integer tInterval = getConfiguration().getInt("repository.interval");
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
        // Get the configurations
        try {
            getLogger().debug("Checking out and processing the configurations");
            mConfigurationsRevision = getConfigurations();
            processConfigurations();
        } catch (RepositoryException tException) {
            throw new ServiceRunException("could not fetch the configurations", tException);
        }
        
        // Get the media
        try {
            getLogger().debug("Processing the media");
            mConfigurationsRevision = checkPresentations();
            processPresentations();
        } catch (RepositoryException tException) {
            throw new ServiceRunException("could not fetch the media", tException);
        }

        // Schedule the monitor
        mSVNMonitor.schedule(
                new Monitor(),
                mSVNMonitorInterval * 1000, // Initial delay
                mSVNMonitorInterval * 1000  // Period
            );
    }

    @Override
    public final void stop() throws ServiceRunException {
    }
    
    
    //
    // Connection helpers
    //
    
    // TODO: Remove the quite identical Connection/Configuration/Presentation setters
    //       somehow make it using the RepositoryEntity interface
    
    private long checkConnections() throws RepositoryException {
        return getPathRevision("connections");
    }
    
    private long getConnections() throws RepositoryException {
        // Get a local checkout and location
        final File tCheckout =  new File(mSVNCheckoutRoot, "connections");
        final String tLocation = mSVNLocation + "/connections";
        
        // Check if the repository exists and is valid
        Long tConnectionRevision = null;
        try {
            tConnectionRevision = checkConnections();
        } catch (RepositoryException tException) {
            // Do nothing
        }
        
        // Checkout or update
        try {
            if (tConnectionRevision == null) {
                FileUtils.cleanDirectory(tCheckout);
                tConnectionRevision = checkoutRepository(tCheckout, tLocation);            
            } else {
                tConnectionRevision = updateRepository(tCheckout);              
            }
        } catch (RepositoryException tException) {
            throw new RepositoryException("could not download the repository", tException);
        } catch (IOException tException) {
            throw new RepositoryException("could not clean the existing (and seemingly invalid) copy of the repository", tException);
        }
        
        return tConnectionRevision;
    }
    
    private void processConnections() throws RepositoryException {        
        // Read
        getLogger().debug("Reading connections");
        Map<String, Connection> tNewConnections = new HashMap<String, Connection>();
        File tDirectory = new File(mSVNCheckoutRoot, "connections");
        for (File tFile: tDirectory.listFiles(new XMLFilter())) {
            // Generate an identifier
            String tFilename = tFile.getName();
            getLogger().trace("Processing '" + tFilename + "'");
            int tDotPosition = tFilename.lastIndexOf('.');
            String tId = tFilename.substring(0, tDotPosition);
            
            // Get the local revision
            String tPath = "/connections/" + tFilename;
            final long tRevision = getPathRevision(tPath);
            
            // Process the contents
            ConnectionProcessor tReader = new ConnectionProcessor(tRevision, tPath, tId, tFile);
            tReader.process();
            Connection tConnection = tReader.getConnection();
            if (tConnection == null) {
                throw new RepositoryException("found empty connection file");
            }
            tNewConnections.put(tConnection.getId(), tConnection);
        }
        
        // Update
        getLogger().debug("Updating connections");
        Repository tRepository = Repository.getInstance();
        RepositoryChangeset<Connection> tChangeset = new RepositoryChangeset<Connection>(tRepository.getConnections(), tNewConnections);
        for (Connection tRemoval: tChangeset.getRemovals().values()) {
            tRepository.removeConnection(tRemoval);
        }
        for (Connection tAddition: tChangeset.getAdditions().values()) {
            tRepository.addConnection(tAddition);
        }
        for (Connection tRemoval: tChangeset.getUpdates().values()) {
            tRepository.addConnection(tRemoval);
        }
    }
    
    
    //
    // Configuration helpers
    //
    
    private long checkConfigurations() throws RepositoryException {
        return getPathRevision("configurations");
    }
    
    private long getConfigurations() throws RepositoryException {
        // Get a local checkout and location
        final File tCheckout =  new File(mSVNCheckoutRoot, "configurations");
        final String tLocation = mSVNLocation + "/configurations";
        
        // Check if the repository exists and is valid
        Long tConfigurationRevision = null;
        try {
            tConfigurationRevision = checkConfigurations();
        } catch (RepositoryException tException) {
            // Do nothing
        }
        
        // Checkout or update
        try {
            if (tConfigurationRevision == null) {
                FileUtils.cleanDirectory(tCheckout);
                tConfigurationRevision = checkoutRepository(tCheckout, tLocation);            
            } else {
                tConfigurationRevision = updateRepository(tCheckout);              
            }
        } catch (RepositoryException tException) {
            throw new RepositoryException("could not download the repository", tException);
        } catch (IOException tException) {
            throw new RepositoryException("could not clean the existing (and seemingly invalid) copy of the repository", tException);
        }
        
        return tConfigurationRevision;
    }
    
    private void processConfigurations() throws RepositoryException {        
        // Read
        getLogger().debug("Reading configurations");
        Map<String, Configuration> tNewConfigurations = new HashMap<String, Configuration>();
        File tDirectory = new File(mSVNCheckoutRoot, "configurations");
        for (File tFile: tDirectory.listFiles(new XMLFilter())) {
            // Generate an identifier
            String tFilename = tFile.getName();
            getLogger().trace("Processing '" + tFilename + "'");
            int tDotPosition = tFilename.lastIndexOf('.');
            String tId = tFilename.substring(0, tDotPosition);
            
            // Get the local revision
            String tPath = "/configurations/" + tFilename;
            final long tRevision = getPathRevision(tPath);
            
            // Process the contents
            ConfigurationProcessor tReader = new ConfigurationProcessor(tRevision, tPath, tId, tFile);
            tReader.process();
            Configuration tConfiguration = tReader.getConfiguration();
            if (tConfiguration == null) {
                throw new RepositoryException("found empty configuration file");
            }
            tNewConfigurations.put(tConfiguration.getId(), tConfiguration);
        }
        
        // Update
        getLogger().debug("Updating configurations");
        Repository tRepository = Repository.getInstance();
        RepositoryChangeset<Configuration> tChangeset = new RepositoryChangeset<Configuration>(tRepository.getConfigurations(), tNewConfigurations);
        for (Configuration tRemoval: tChangeset.getRemovals().values()) {
            tRepository.removeConfiguration(tRemoval);
        }
        for (Configuration tAddition: tChangeset.getAdditions().values()) {
            tRepository.addConfiguration(tAddition);
        }
        for (Configuration tRemoval: tChangeset.getUpdates().values()) {
            tRepository.addConfiguration(tRemoval);
        }
    }
    
    
    //
    // Presentation helpers
    //
    
    private long checkPresentations() throws RepositoryException {
        return getPathRevision("presentations");
    }
    
    private void processPresentations() throws RepositoryException {        
        // List
        getLogger().debug("Listing presentations");
        Map<String, Presentation> tNewPresentations = new HashMap<String, Presentation>();
        Map<String, Long> tPathEntries = getChildrenRevisions("presentations");
        for (String tId: tPathEntries.keySet()) {
            long tRevision = tPathEntries.get(tId);
            String tPath = "/presentations/" + tId;            
            Presentation tMedia = new Presentation(tId, tRevision, tPath);
            tNewPresentations.put(tId, tMedia);
        }
        
        // Update
        getLogger().debug("Updating presentations");
        Repository tRepository = Repository.getInstance();
        RepositoryChangeset<Presentation> tChangeset = new RepositoryChangeset<Presentation>(tRepository.getPresentations(), tNewPresentations);
        for (Presentation tRemoval: tChangeset.getRemovals().values()) {
            tRepository.removePresentation(tRemoval);
        }
        for (Presentation tAddition: tChangeset.getAdditions().values()) {
            tRepository.addPresentation(tAddition);
        }
        for (Presentation tRemoval: tChangeset.getUpdates().values()) {
            tRepository.updatePresentation(tRemoval);
        }
    }


    //
    // Auxiliary
    //
    
    private long getPathRevision(final String iPath) throws RepositoryException {
        try {
            final List<Long> tRevisions = new ArrayList<Long>();
            mSVNClient.info2(
                    mSVNLocation + "/" + iPath,
                    Revision.HEAD,
                    Revision.HEAD,
                    Depth.empty,
                    null,
                    new InfoCallback() {
                        @Override
                        public void singleInfo(Info2 iInfo) {
                            tRevisions.add(iInfo.getLastChangedRev());
                        }
                    });
            if (tRevisions.size() != 1)
                throw new RepositoryException("unexpected amount of info entries");
            return tRevisions.get(0);
        } catch (ClientException tException) {
            throw new RepositoryException("could not check the repository", tException);
        }
    }
    
    private Map<String, Long> getChildrenRevisions(final String iPath) throws RepositoryException {
        try {
            final Map<String, Long> tChildren = new HashMap<String, Long>();
            mSVNClient.info2(
                    mSVNLocation + "/" + iPath,
                    Revision.HEAD,
                    Revision.HEAD,
                    Depth.immediates,
                    null,
                    new InfoCallback() {
                        @Override
                        public void singleInfo(Info2 iInfo) {
                            if (iInfo.getPath().equals(iPath))
                                return;
                            tChildren.put(iInfo.getPath(), iInfo.getLastChangedRev());
                        }
                    });
            return tChildren;
        } catch (ClientException tException) {
            throw new RepositoryException("could not check the repository", tException);
        }
    }

    private long checkoutRepository(final File iCheckout, final String iLocation) throws RepositoryException {
        try {
            long tRevision = mSVNClient.checkout(
                    iLocation,
                    iCheckout.getAbsolutePath(),
                    Revision.HEAD,
                    Revision.HEAD,
                    Depth.infinity,
                    false,
                    false);            
            return tRevision;
        } catch (ClientException tException) {
            throw new RepositoryException("could not checkout the repository", tException);
        }
    }
    
    private long updateRepository(final File iCheckout) throws RepositoryException {
        try {
            long tRevision = mSVNClient.update(
                    iCheckout.getAbsolutePath(),
                    Revision.HEAD,
                    Depth.infinity,
                    true,
                    false,
                    false);
            return tRevision;
        } catch (ClientException tException) {
            throw new RepositoryException("could not update the repository", tException);
        }
    }
    
    private class RepositoryChangeset<T extends RepositoryEntity> {
        //
        // Member data
        //
        
        private final Map<String, T> mAdditions;
        private final Map<String, T> mRemovals;
        private final Map<String, T> mUpdates;
        
        
        //
        // Construction and destruction
        //
        
        public RepositoryChangeset(Map<String, T> iOldEntities, Map<String, T> iCurrentEntities) {
            // Check for removed entities
            mRemovals = new HashMap<String, T>();
            for (T tOldEntity: iOldEntities.values()) {
                if (! iCurrentEntities.containsKey(tOldEntity.getId())) {
                    getLogger().debug("Entity "
                            + tOldEntity.getId()
                            + " seems to have been deleted (last known rev "
                            + tOldEntity.getRevision()
                            + "), removing from repository");
                    mRemovals.put(tOldEntity.getId(), tOldEntity);
                }
            }

            // Check for new and updated entities      
            mAdditions = new HashMap<String, T>();
            mUpdates = new HashMap<String, T>();
            for (T tCurrentEntity : iCurrentEntities.values()) {
                T tOldEntity = iOldEntities.get(tCurrentEntity.getId());
                if (tOldEntity == null) {
                    getLogger().debug("Entity "
                            + tCurrentEntity.getId()
                            + " seems new (rev "
                            + tCurrentEntity.getRevision()
                            + "), adding to repository");
                    mAdditions.put(tCurrentEntity.getId(), tCurrentEntity);
                } else if (tCurrentEntity.getRevision() > tOldEntity.getRevision()) {
                    getLogger().debug("Entity "
                            + tCurrentEntity.getId()
                            + " is a more recent version (rev "
                            + tCurrentEntity.getRevision()
                            + ") of an existing media (rev "
                            + tOldEntity.getRevision()
                            + "), updating the repository");
                    mUpdates.put(tCurrentEntity.getId(), tCurrentEntity);
                }
            }
        }
        
        
        //
        // Getters and setters
        //
        
        public final Map<String, T> getAdditions() {
            return mAdditions;
        }
        
        public final Map<String, T> getRemovals() {
            return mRemovals;
        }
        
        public final Map<String, T> getUpdates() {
            return mUpdates;
        }
    }
}
