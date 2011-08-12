/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.configurations.KioskConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author tim
 */
public final class Repository {
    //
    // Member data
    //

    private String mServer;
    private Map<String, KioskConfiguration> mKioskConfigurations;
    private Map<UUID, String> mKioskMapping;
    private List<IRepositoryListener> mListeners;


    //
    // Static functionality
    //

    private static Repository INSTANCE;

    public static Repository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Repository();
        }
        return INSTANCE;
    }


    //
    // Construction and destruction
    //

    private Repository() {
        mKioskConfigurations = new HashMap<String, KioskConfiguration>();
        mKioskMapping = new HashMap<UUID, String>();
        mListeners = new ArrayList<IRepositoryListener>();
    }


    //
    // Getters and setters
    //
    
    public String getServer() {
        return mServer;
    }
    
    public void setServer(final String iServer) {
        mServer = iServer;
    }
    
    public void addListener(final IRepositoryListener iListener) {
        mListeners.add(iListener);
    }
    
    public void removeListener(final IRepositoryListener iListener) {
        mListeners.remove(iListener);
    }

    public void addKioskConfiguration(final KioskConfiguration iKioskConfiguration) throws RepositoryException {
        if (mKioskConfigurations.containsKey(iKioskConfiguration.getId())) {
            throw new RepositoryException("configuration " + iKioskConfiguration.getId() + " already present in repository");
        }
        mKioskConfigurations.put(iKioskConfiguration.getId(), iKioskConfiguration);
        mKioskMapping.put(iKioskConfiguration.getTarget(), iKioskConfiguration.getId());
        emitKioskConfigurationAdded(iKioskConfiguration);
    }
    
    public void updateKioskConfiguration(final KioskConfiguration iKioskConfiguration) throws RepositoryException {
        if (! mKioskConfigurations.containsKey(iKioskConfiguration.getId())) {
            throw new RepositoryException("configuration " + iKioskConfiguration.getId() + " not present in repository");
        }
        KioskConfiguration tOldKioskConfiguration = mKioskConfigurations.put(iKioskConfiguration.getId(), iKioskConfiguration);
        emitKioskConfigurationUpdated(tOldKioskConfiguration, iKioskConfiguration);
    }

    public KioskConfiguration getKioskConfiguration(final String iName) {
        return mKioskConfigurations.get(iName);
    }

    public KioskConfiguration lookupKioskConfiguration(final UUID iUuid) throws RepositoryException {
        if (!mKioskMapping.containsKey(iUuid)) {
            throw new RepositoryException("Kiosk configuration for device '" + iUuid + "' not found");
        }
        return mKioskConfigurations.get(mKioskMapping.get(iUuid));
    }
    
    
    //
    // Signals
    //
    
    public void emitError(final String iMessage, final RepositoryException iException) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doRepositoryError(iMessage, iException);
        }
    }
    
    public void emitWarning(final String iMessage) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doRepositoryWarning(iMessage);
        }
    }  
    
    private void emitKioskConfigurationAdded(final KioskConfiguration iKioskConfiguration) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doKioskConfigurationAdded(iKioskConfiguration);
        }
    }
    
    private void emitKioskConfigurationUpdated(final KioskConfiguration iOldKioskConfiguration, final KioskConfiguration iKioskConfiguration) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doKioskConfigurationUpdated(iOldKioskConfiguration, iKioskConfiguration);
        }
    }
}
