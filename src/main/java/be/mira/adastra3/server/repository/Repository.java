/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.configurations.Configuration;
import be.mira.adastra3.server.repository.media.Media;
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
    
    private Map<String, Configuration> mConfigurations;
    private Map<String, Media> mMedia;
    private List<IRepositoryListener> mListeners;
    private String mServer;


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
        mMedia = new HashMap<String, Media>();
        mListeners = new ArrayList<IRepositoryListener>();
    }


    //
    // Getters and setters
    //
    
    public String getServer() {
        return mServer;
    }
    
    // TODO: this isn't used? wat?
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

    public Configuration getConfiguration(final String iName) {
        return mConfigurations.get(iName);
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

    public void removeConfiguration(final Configuration iConfiguration) throws RepositoryException {
        if (! mConfigurations.containsKey(iConfiguration.getId())) {
            throw new RepositoryException("configuration " + iConfiguration.getId() + " not present in repository");
        }
        mConfigurations.remove(iConfiguration.getId());
        emitConfigurationRemoved(iConfiguration);
    }
    
    public Collection<Media> getMedias() {
        return mMedia.values();
    }

    public Media getMedia(final String iName) {
        return mMedia.get(iName);
    }

    public void addMedia(final Media iMedia) throws RepositoryException {
        if (mMedia.containsKey(iMedia.getId())) {
            throw new RepositoryException("configuration " + iMedia.getId() + " already present in repository");
        }
        mMedia.put(iMedia.getId(), iMedia);
        emitMediaAdded(iMedia);
    }
    
    public void updateMedia(final Media iMedia) throws RepositoryException {
        if (! mMedia.containsKey(iMedia.getId())) {
            throw new RepositoryException("configuration " + iMedia.getId() + " not present in repository");
        }
        Media tOldMedia = mMedia.put(iMedia.getId(), iMedia);
        emitMediaUpdated(tOldMedia, iMedia);
    }

    public void removeMedia(final Media iMedia) throws RepositoryException {
        if (! mMedia.containsKey(iMedia.getId())) {
            throw new RepositoryException("configuration " + iMedia.getId() + " not present in repository");
        }
        mMedia.remove(iMedia.getId());
        emitMediaRemoved(iMedia);
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
    
    private void emitConfigurationRemoved(final Configuration iConfiguration) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doConfigurationRemoved(iConfiguration);
        }
    }
    
    private void emitMediaAdded(final Media iMedia) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doMediaAdded(iMedia);
        }
    }
    
    private void emitMediaUpdated(final Media iOldMedia, final Media iMedia) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doMediaUpdated(iOldMedia, iMedia);
        }
    }
    
    private void emitMediaRemoved(final Media iMedia) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doMediaRemoved(iMedia);
        }
    }
}
