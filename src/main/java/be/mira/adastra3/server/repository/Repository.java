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
public class Repository {
    //
    // Member data
    //

    private Map<String, KioskConfiguration> mKioskConfigurations;
    private Map<UUID, String> mKioskMapping;
    private List<IRepositoryListener> mListeners;


    //
    // Static functionality
    //

    private static Repository mInstance;

    public static Repository getInstance() {
        if (mInstance == null)
            mInstance = new Repository();
        return mInstance;
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
    
    public void addListener(IRepositoryListener iListener) {
        mListeners.add(iListener);
    }
    
    public void removeListener(IRepositoryListener iListener) {
        mListeners.remove(iListener);
    }

    public void addKioskConfiguration(KioskConfiguration iKioskConfiguration) throws RepositoryException {
        if (mKioskConfigurations.containsKey(iKioskConfiguration.getId()))
            throw new RepositoryException("configuration " + iKioskConfiguration.getId() + " already present in repository");
        mKioskConfigurations.put(iKioskConfiguration.getId(), iKioskConfiguration);
        mKioskMapping.put(iKioskConfiguration.getTarget(), iKioskConfiguration.getId());
        emitKioskConfigurationAdded(iKioskConfiguration);
    }
    
    public void updateKioskConfiguration(KioskConfiguration iKioskConfiguration) throws RepositoryException {
        if (! mKioskConfigurations.containsKey(iKioskConfiguration.getId()))
            throw new RepositoryException("configuration " + iKioskConfiguration.getId() + " not present in repository");
        KioskConfiguration tOldKioskConfiguration = mKioskConfigurations.put(iKioskConfiguration.getId(), iKioskConfiguration);
        emitKioskConfigurationUpdated(tOldKioskConfiguration, iKioskConfiguration);
    }

    public KioskConfiguration getKioskConfiguration(String iName) throws RepositoryException {
        return mKioskConfigurations.get(iName);
    }

    public KioskConfiguration lookupKioskConfiguration(UUID iUuid) throws RepositoryException {
        if (!mKioskMapping.containsKey(iUuid))
            return null;
        return mKioskConfigurations.get(mKioskMapping.get(iUuid));
    }
    
    
    //
    // Signals
    //
    
    public void emitError(String iMessage, RepositoryException iException) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doError(iMessage, iException);
        }
    }
    
    public void emitWarning(String iMessage) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doWarning(iMessage);
        }
    }  
    
    private void emitKioskConfigurationAdded(KioskConfiguration iKioskConfiguration) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doKioskConfigurationAdded(iKioskConfiguration);
        }
    }
    
    private void emitKioskConfigurationUpdated(KioskConfiguration iOldKioskConfiguration, KioskConfiguration iKioskConfiguration) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doKioskConfigurationUpdated(iOldKioskConfiguration, iKioskConfiguration);
        }
    }
}
