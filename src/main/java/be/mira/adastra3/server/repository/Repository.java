/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.configurations.Configuration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tim
 */
public final class Repository {
    //
    // Member data
    //

    private String mServer;
    private Map<String, Configuration> mConfigurations;
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
        mConfigurations = new HashMap<String, Configuration>();
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
    
    public Collection<Configuration> getConfigurations() {
        return mConfigurations.values();
    }

    public void addConfiguration(final Configuration iConfiguration) throws RepositoryException {
        if (mConfigurations.containsKey(iConfiguration.getId())) {
            throw new RepositoryException("configuration " + iConfiguration.getId() + " already present in repository");
        }
        mConfigurations.put(iConfiguration.getId(), iConfiguration);
        emitConfigurationAdded(iConfiguration);
    }
    
    public void updateConfiguration(final Configuration iConfiguration) throws RepositoryException {
        if (! mConfigurations.containsKey(iConfiguration.getId())) {
            throw new RepositoryException("configuration " + iConfiguration.getId() + " not present in repository");
        }
        Configuration tOldConfiguration = mConfigurations.put(iConfiguration.getId(), iConfiguration);
        emitConfigurationUpdated(tOldConfiguration, iConfiguration);
    }

    public Configuration getConfiguration(final String iName) {
        return mConfigurations.get(iName);
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
    
    private void emitConfigurationAdded(final Configuration iConfiguration) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doConfigurationAdded(iConfiguration);
        }
    }
    
    private void emitConfigurationUpdated(final Configuration iOldConfiguration, final Configuration iConfiguration) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doConfigurationUpdated(iOldConfiguration, iConfiguration);
        }
    }
}
