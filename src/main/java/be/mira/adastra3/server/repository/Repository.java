/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.configuration.Configuration;
import be.mira.adastra3.server.repository.connection.Connection;
import be.mira.adastra3.server.repository.presentation.Presentation;
import java.util.ArrayList;
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
    
    private final Map<String, Connection> mConnections;
    private final Map<String, Configuration> mConfigurations;
    private final Map<String, Presentation> mMedia;
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
        mConnections = new HashMap<String, Connection>();
        mConfigurations = new HashMap<String, Configuration>();
        mMedia = new HashMap<String, Presentation>();
        mListeners = new ArrayList<IRepositoryListener>();
    }


    //
    // Getters and setters
    //
    
    // TODO: Remove the quite identical Connection/Configuration/Presentation setters
    //       somehow make it using the RepositoryEntity interface
    
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
    
    public Map<String, Connection> getConnections() {
        return mConnections;
    }

    public Connection getConnection(final String iName) {
        return mConnections.get(iName);
    }

    public void addConnection(final Connection iConnection) throws RepositoryException {
        if (mConnections.containsKey(iConnection.getId())) {
            throw new RepositoryException("connection " + iConnection.getId() + " already present in repository");
        }
        mConnections.put(iConnection.getId(), iConnection);
        emitConnectionAdded(iConnection);
    }
    
    public void updateConnection(final Connection iConnection) throws RepositoryException {
        if (! mConnections.containsKey(iConnection.getId())) {
            throw new RepositoryException("connection " + iConnection.getId() + " not present in repository");
        }
        Connection tOldConnection = mConnections.put(iConnection.getId(), iConnection);
        emitConnectionUpdated(tOldConnection, iConnection);
    }

    public void removeConnection(final Connection iConnection) throws RepositoryException {
        if (! mConnections.containsKey(iConnection.getId())) {
            throw new RepositoryException("connection " + iConnection.getId() + " not present in repository");
        }
        mConnections.remove(iConnection.getId());
        emitConnectionRemoved(iConnection);
    }
    
    public Map<String, Configuration> getConfigurations() {
        return mConfigurations;
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
    
    public Map<String, Presentation> getPresentations() {
        return mMedia;
    }

    public Presentation getPresentation(final String iId) {
        return mMedia.get(iId);
    }

    public void addPresentation(final Presentation iPresentation) throws RepositoryException {
        if (mMedia.containsKey(iPresentation.getId())) {
            throw new RepositoryException("presentation " + iPresentation.getId() + " already present in repository");
        }
        mMedia.put(iPresentation.getId(), iPresentation);
        emitPresentationAdded(iPresentation);
    }
    
    public void updatePresentation(final Presentation iPresentation) throws RepositoryException {
        if (! mMedia.containsKey(iPresentation.getId())) {
            throw new RepositoryException("presentation " + iPresentation.getId() + " not present in repository");
        }
        Presentation tOldMedia = mMedia.put(iPresentation.getId(), iPresentation);
        emitPresentationUpdated(tOldMedia, iPresentation);
    }

    public void removePresentation(final Presentation iPresentation) throws RepositoryException {
        if (! mMedia.containsKey(iPresentation.getId())) {
            throw new RepositoryException("presentation " + iPresentation.getId() + " not present in repository");
        }
        mMedia.remove(iPresentation.getId());
        emitPresentationRemoved(iPresentation);
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
    
    private void emitConnectionAdded(final Connection iConnection) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doConnectionAdded(iConnection);
        }
    }
    
    private void emitConnectionUpdated(final Connection iOldConnection, final Connection iConnection) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doConnectionUpdated(iOldConnection, iConnection);
        }
    }
    
    private void emitConnectionRemoved(final Connection iConnection) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doConnectionRemoved(iConnection);
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
    
    private void emitPresentationAdded(final Presentation iMedia) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doPresentationAdded(iMedia);
        }
    }
    
    private void emitPresentationUpdated(final Presentation iOldMedia, final Presentation iMedia) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doPresentationUpdated(iOldMedia, iMedia);
        }
    }
    
    private void emitPresentationRemoved(final Presentation iMedia) {
        for (IRepositoryListener tListener : mListeners) {
            tListener.doPresentationRemoved(iMedia);
        }
    }
}
