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
import java.util.HashMap;
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
    
    @XmlElement(nillable=true)
    public synchronized String getServer() {
        return mServer;
    }
    
    public synchronized void setServer(final String iServer) {
        mServer = iServer;
    }
    
    @XmlElementWrapper(name="connections")
    @XmlElement(name="connection")
    public synchronized Map<String, Connection> getConnections() {
        return mConnections;
    }

    public synchronized Connection getConnection(final String iId) {
        return mConnections.get(iId);
    }

    public synchronized void addConnection(final String iId, final Connection iConnection) throws RepositoryException {
        if (mConnections.containsKey(iId)) {
            throw new RepositoryException("connection " + iId + " already present in repository");
        }
        mConnections.put(iId, iConnection);
        
        RepositoryEvent tEvent = new RepositoryConnectionEvent(this, RepositoryEventType.ADDED, iId, iConnection);
        mPublisher.publishEvent(tEvent);
    }
    
    public synchronized void updateConnection(final String iId, final Connection iConnection) throws RepositoryException {
        if (! mConnections.containsKey(iId)) {
            throw new RepositoryException("connection " + iId + " not present in repository");
        }
        Connection tOldConnection = mConnections.put(iId, iConnection);
        
        RepositoryEvent tEvent = new RepositoryConnectionEvent(this, RepositoryEventType.UPDATED, iId, iConnection, tOldConnection);
        mPublisher.publishEvent(tEvent);
    }

    public synchronized void removeConnection(final String iId, final Connection iConnection) throws RepositoryException {
        if (! mConnections.containsKey(iId)) {
            throw new RepositoryException("connection " + iId + " not present in repository");
        }
        mConnections.remove(iId);
        
        RepositoryEvent tEvent = new RepositoryConnectionEvent(this, RepositoryEventType.REMOVED, iId, iConnection);
        mPublisher.publishEvent(tEvent);
    }
    
    @XmlElementWrapper(name="configurations")
    @XmlElement(name="configuration")
    public synchronized Map<String, Configuration> getConfigurations() {
        return mConfigurations;
    }

    public synchronized Configuration getConfiguration(final String iId) {
        return mConfigurations.get(iId);
    }

    public synchronized void addConfiguration(final String iId, final Configuration iConfiguration) throws RepositoryException {
        if (mConfigurations.containsKey(iId)) {
            throw new RepositoryException("configuration " + iId + " already present in repository");
        }
        mConfigurations.put(iId, iConfiguration);
        
        RepositoryEvent tEvent = new RepositoryConfigurationEvent(this, RepositoryEventType.ADDED, iId, iConfiguration);
        mPublisher.publishEvent(tEvent);
    }
    
    public synchronized void updateConfiguration(final String iId, final Configuration iConfiguration) throws RepositoryException {
        if (! mConfigurations.containsKey(iId)) {
            throw new RepositoryException("configuration " + iId + " not present in repository");
        }
        Configuration tOldConfiguration = mConfigurations.put(iId, iConfiguration);
        
        RepositoryEvent tEvent = new RepositoryConfigurationEvent(this, RepositoryEventType.UPDATED, iId, iConfiguration, tOldConfiguration);
        mPublisher.publishEvent(tEvent);
    }

    public synchronized void removeConfiguration(final String iId, final Configuration iConfiguration) throws RepositoryException {
        if (! mConfigurations.containsKey(iId)) {
            throw new RepositoryException("configuration " + iId + " not present in repository");
        }
        mConfigurations.remove(iId);
        
        RepositoryEvent tEvent = new RepositoryConfigurationEvent(this, RepositoryEventType.REMOVED, iId, iConfiguration);
        mPublisher.publishEvent(tEvent);
    }
     
    @XmlElementWrapper(name="presentations")
    @XmlElement(name="presentation")
    public synchronized Map<String, Presentation> getPresentations() {
        return mPresentations;
    }

    public synchronized Presentation getPresentation(final String iId) {
        return mPresentations.get(iId);
    }

    public synchronized void addPresentation(final String iId, final Presentation iPresentation) throws RepositoryException {
        if (mPresentations.containsKey(iId)) {
            throw new RepositoryException("presentation " + iId + " already present in repository");
        }
        mPresentations.put(iId, iPresentation);
        
        RepositoryEvent tEvent = new RepositoryPresentationEvent(this, RepositoryEventType.ADDED, iId, iPresentation);
        mPublisher.publishEvent(tEvent);
    }
    
    public synchronized void updatePresentation(final String iId, final Presentation iPresentation) throws RepositoryException {
        if (! mPresentations.containsKey(iId)) {
            throw new RepositoryException("presentation " + iId + " not present in repository");
        }
        Presentation tOldPresentation = mPresentations.put(iId, iPresentation);
        
        RepositoryEvent tEvent = new RepositoryPresentationEvent(this, RepositoryEventType.UPDATED, iId, iPresentation, tOldPresentation);
        mPublisher.publishEvent(tEvent);
    }

    public synchronized void removePresentation(final String iId, final Presentation iPresentation) throws RepositoryException {
        if (! mPresentations.containsKey(iId)) {
            throw new RepositoryException("presentation " + iId + " not present in repository");
        }
        mPresentations.remove(iId);
        
        RepositoryEvent tEvent = new RepositoryPresentationEvent(this, RepositoryEventType.REMOVED, iId, iPresentation);
        mPublisher.publishEvent(tEvent);
    }
}
