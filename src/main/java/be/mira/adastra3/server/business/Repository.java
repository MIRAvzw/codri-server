/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.business;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.configuration.Configuration;
import be.mira.adastra3.server.repository.connection.Connection;
import be.mira.adastra3.server.repository.presentation.Presentation;
import be.mira.adastra3.spring.Logger;
import be.mira.adastra3.server.events.RepositoryConfigurationEvent;
import be.mira.adastra3.server.events.RepositoryConnectionEvent;
import be.mira.adastra3.server.events.RepositoryEvent;
import be.mira.adastra3.server.events.RepositoryEvent.RepositoryEventType;
import be.mira.adastra3.server.events.RepositoryPresentationEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.logging.Log;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 *
 * @author tim
 */
@XmlRootElement(name="repository")
public final class Repository implements ApplicationEventPublisherAware {
    //
    // Member data
    //
    
    @Logger
    private Log mLogger;
    
    private ApplicationEventPublisher mPublisher;
    
    private final Map<String, Configuration> mConfigurations;
    private final Map<String, Presentation> mPresentations;
    private final Map<String, Connection> mConnections;
    private String mServer;


    //
    // Construction and destruction
    //

    public Repository() {
        mConfigurations = new HashMap<String, Configuration>();
        mPresentations = new HashMap<String, Presentation>();
        mConnections = new HashMap<String, Connection>();
    }
    
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher iPublisher) {
        mPublisher = iPublisher;
    }
    
    @PostConstruct
    public void init() {
        
    }
    
    @PreDestroy
    public void destroy() {
        mConfigurations.clear();
        mPresentations.clear();
        mConnections.clear();
    }


    //
    // Getters and setters
    //
    
    // TODO: Remove the quite identical Connection/Configuration/Presentation setters
    //       somehow make it using the RepositoryEntity interface
    
    @XmlElement
    public synchronized String getServer() {
        return mServer;
    }
    
    public synchronized void setServer(final String iServer) {
        mServer = iServer;
    }
    
    // TODO: why return a map?
    @XmlElementWrapper(name="connections")
    @XmlElement(name="connection")
    public synchronized Collection<Connection> getConnections() {
        return mConnections.values();
    }
    
    public synchronized Map<String, Connection> getConnectionsMap() {
        return mConnections;
    }

    public synchronized Connection getConnection(final String iName) {
        return mConnections.get(iName);
    }

    public synchronized void addConnection(final Connection iConnection) throws RepositoryException {
        if (mConnections.containsKey(iConnection.getId())) {
            throw new RepositoryException("connection " + iConnection.getId() + " already present in repository");
        }
        mConnections.put(iConnection.getId(), iConnection);
        
        RepositoryEvent tEvent = new RepositoryConnectionEvent(this, RepositoryEventType.ADDED, iConnection);
        mPublisher.publishEvent(tEvent);
    }
    
    public synchronized void updateConnection(final Connection iConnection) throws RepositoryException {
        if (! mConnections.containsKey(iConnection.getId())) {
            throw new RepositoryException("connection " + iConnection.getId() + " not present in repository");
        }
        Connection tOldConnection = mConnections.put(iConnection.getId(), iConnection);
        
        RepositoryEvent tEvent = new RepositoryConnectionEvent(this, RepositoryEventType.UPDATED, iConnection, tOldConnection);
        mPublisher.publishEvent(tEvent);
    }

    public synchronized void removeConnection(final Connection iConnection) throws RepositoryException {
        if (! mConnections.containsKey(iConnection.getId())) {
            throw new RepositoryException("connection " + iConnection.getId() + " not present in repository");
        }
        mConnections.remove(iConnection.getId());
        
        RepositoryEvent tEvent = new RepositoryConnectionEvent(this, RepositoryEventType.REMOVED, iConnection);
        mPublisher.publishEvent(tEvent);
    }
    
    // TODO: why return map?
    @XmlElementWrapper(name="configurations")
    @XmlElement(name="configuration")
    public synchronized Collection<Configuration> getConfigurations() {
        return mConfigurations.values();
    }
    
    public synchronized Map<String, Configuration> getConfigurationsMap() {
        return mConfigurations;
    }

    public synchronized Configuration getConfiguration(final String iName) {
        return mConfigurations.get(iName);
    }

    public synchronized void addConfiguration(final Configuration iConfiguration) throws RepositoryException {
        if (mConfigurations.containsKey(iConfiguration.getId())) {
            throw new RepositoryException("configuration " + iConfiguration.getId() + " already present in repository");
        }
        mConfigurations.put(iConfiguration.getId(), iConfiguration);
        
        RepositoryEvent tEvent = new RepositoryConfigurationEvent(this, RepositoryEventType.ADDED, iConfiguration);
        mPublisher.publishEvent(tEvent);
    }
    
    public synchronized void updateConfiguration(final Configuration iConfiguration) throws RepositoryException {
        if (! mConfigurations.containsKey(iConfiguration.getId())) {
            throw new RepositoryException("configuration " + iConfiguration.getId() + " not present in repository");
        }
        Configuration tOldConfiguration = mConfigurations.put(iConfiguration.getId(), iConfiguration);
        
        RepositoryEvent tEvent = new RepositoryConfigurationEvent(this, RepositoryEventType.UPDATED, iConfiguration, tOldConfiguration);
        mPublisher.publishEvent(tEvent);
    }

    public synchronized void removeConfiguration(final Configuration iConfiguration) throws RepositoryException {
        if (! mConfigurations.containsKey(iConfiguration.getId())) {
            throw new RepositoryException("configuration " + iConfiguration.getId() + " not present in repository");
        }
        mConfigurations.remove(iConfiguration.getId());
        
        RepositoryEvent tEvent = new RepositoryConfigurationEvent(this, RepositoryEventType.REMOVED, iConfiguration);
        mPublisher.publishEvent(tEvent);
    }
    
    // TODO: why return a map?    
    @XmlElementWrapper(name="presentations")
    @XmlElement(name="presentation")
    public synchronized Collection<Presentation> getPresentations() {
        return mPresentations.values();
    }
    
    public synchronized Map<String, Presentation> getPresentationsMap() {
        return mPresentations;
    }

    public synchronized Presentation getPresentation(final String iId) {
        return mPresentations.get(iId);
    }

    public synchronized void addPresentation(final Presentation iPresentation) throws RepositoryException {
        if (mPresentations.containsKey(iPresentation.getId())) {
            throw new RepositoryException("presentation " + iPresentation.getId() + " already present in repository");
        }
        mPresentations.put(iPresentation.getId(), iPresentation);
        
        RepositoryEvent tEvent = new RepositoryPresentationEvent(this, RepositoryEventType.ADDED, iPresentation);
        mPublisher.publishEvent(tEvent);
    }
    
    public synchronized void updatePresentation(final Presentation iPresentation) throws RepositoryException {
        if (! mPresentations.containsKey(iPresentation.getId())) {
            throw new RepositoryException("presentation " + iPresentation.getId() + " not present in repository");
        }
        Presentation tOldPresentation = mPresentations.put(iPresentation.getId(), iPresentation);
        
        RepositoryEvent tEvent = new RepositoryPresentationEvent(this, RepositoryEventType.UPDATED, iPresentation, tOldPresentation);
        mPublisher.publishEvent(tEvent);
    }

    public synchronized void removePresentation(final Presentation iPresentation) throws RepositoryException {
        if (! mPresentations.containsKey(iPresentation.getId())) {
            throw new RepositoryException("presentation " + iPresentation.getId() + " not present in repository");
        }
        mPresentations.remove(iPresentation.getId());
        
        RepositoryEvent tEvent = new RepositoryPresentationEvent(this, RepositoryEventType.REMOVED, iPresentation);
        mPublisher.publishEvent(tEvent);
    }
}
