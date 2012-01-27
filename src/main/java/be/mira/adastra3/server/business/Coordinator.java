/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.business;

import be.mira.adastra3.server.events.*;
import be.mira.adastra3.server.exceptions.DeviceException;
import be.mira.adastra3.server.network.Kiosk;
import be.mira.adastra3.server.repository.configuration.Configuration;
import be.mira.adastra3.server.repository.connection.Connection;
import be.mira.adastra3.server.repository.presentation.Presentation;
import be.mira.adastra3.spring.Slf4jLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
*
* @author tim
*/
public class Coordinator implements ApplicationListener<ApplicationEvent> {
    //
    // Data members
    //
    
    @Slf4jLogger
    private Logger mLogger;
    
    private Network mNetwork;
    private Repository mRepository;
    
    
    //
    // Construction and destruction
    //
    
    public Coordinator(final Network iNetwork, final Repository iRepository) {
        mNetwork = iNetwork;
        mRepository = iRepository;
    }
    
    
    //
    // ApplicationEvent handler
    //
    
    @Override
    public final void onApplicationEvent(final ApplicationEvent iEvent) {
        // TODO: this is ugly
        if (iEvent instanceof NetworkEvent) {
            if (iEvent instanceof NetworkKioskEvent) {
                onNetworkKioskEvent((NetworkKioskEvent) iEvent);
            }
        } else if (iEvent instanceof RepositoryEvent) {
            if (iEvent instanceof RepositoryPresentationEvent) {
                onRepositoryPresentationEvent((RepositoryPresentationEvent) iEvent);
            } else if (iEvent instanceof RepositoryConfigurationEvent) {
                onRepositoryConfigurationEvent((RepositoryConfigurationEvent) iEvent);
            } else if (iEvent instanceof RepositoryConnectionEvent) {
                onRepositoryConnectionEvent((RepositoryConnectionEvent) iEvent);
            }
        }
    }
    
    
    //
    // NetworkEvent handler
    //
    
    public final void onNetworkKioskEvent(final NetworkKioskEvent iEvent) {        
        switch (iEvent.getType()) {
            case ADDED:
                mLogger.info("Kiosk added: " + iEvent.getId());

                // Find the connections this device is a part of
                Map<String, Connection> tRelevantConnections = new HashMap<String, Connection>();
                for (Map.Entry<String, Connection> tEntry: mRepository.getConnections().entrySet()) {
                    if (tEntry.getValue().getKiosk().equals(iEvent.getId())) {
                        tRelevantConnections.put(tEntry.getKey(), tEntry.getValue());
                    }
                }

                // Check the connections
                if (tRelevantConnections.isEmpty()) {
                    mLogger.warn("Couldn't find any configuration for kiosk " + iEvent.getId() + ", it'll remain unconfigured");
                } else if (tRelevantConnections.size() > 1) {
                    mLogger.warn("Ambiguous connections found for kiosk " + iEvent.getId() + ", it'll remain unconfigured");
                } else {
                    Map.Entry<String, Connection> tEntry = tRelevantConnections.entrySet().iterator().next();
                    pushConnection(tEntry.getKey(), tEntry.getValue());
                }
                
                break;
                
            case REMOVED:
                mLogger.info("Kiosk removed: " + iEvent.getId());                
        }
    }
    
    
    //
    // RepositoryPresentationEvent handler
    //
    
    // TODO: the doRepository as well as doPresentation(Update|Added) functions are
    // quite similar, it would be nice to deduplicate this code&
    
    public final void onRepositoryPresentationEvent(final RepositoryPresentationEvent iEvent) {
        Presentation tPresentation = (Presentation) iEvent.getEntity();
        
        switch (iEvent.getType()) {
            case ADDED:
                mLogger.info("Presentation added: " + iEvent.getId());
                pushPresentation(iEvent.getId(), tPresentation);
                
                break; 
                
            case UPDATED:
                // TODO: do something with old one
                
                mLogger.info("Presentation updated: " + iEvent.getId());
                pushPresentation(iEvent.getId(), tPresentation);
                
                break;
                
            case REMOVED:
                mLogger.info("Presentation removed: " + iEvent.getId());

                // TODO
                
                break;
        }
    }
        
    private void pushPresentation(final String iId, final Presentation iPresentation) {
        // Find the connections this presentation is a part of
        List<Connection> tRelevantConnections = new ArrayList<Connection>();
        for (Connection tConnection: mRepository.getConnections().values()) {
            if (tConnection.getPresentation().equals(iId)) {
                tRelevantConnections.add(tConnection);
            }
        }
        
        // Push to the devices
        for (Connection tConnection: tRelevantConnections) {
            Kiosk tKiosk = mNetwork.getKiosk(tConnection.getKiosk());
            // No need to display too many errors here, this should already have
            // have happened when the connection was initially added to the
            // repository
            if (tKiosk != null) {
                try {
                    tKiosk.setPresentation(iPresentation);
                } catch (DeviceException tException) {
                    mLogger.error("Could not push presentation " + tConnection.getPresentation() + " to target device '" + tConnection.getKiosk() + "'", tException);
                }
            }
        }
    }
    
    
    //
    // RepositoryConfigurationEvent handler
    //
    
    public final void onRepositoryConfigurationEvent(final RepositoryConfigurationEvent iEvent) {
        Configuration tConfiguration = (Configuration) iEvent.getEntity();
        
        switch (iEvent.getType()) {
            case ADDED:
                mLogger.info("Configuration added: " + iEvent.getId());
                pushConfiguration(iEvent.getId(), tConfiguration);
                
                break;
                
            case UPDATED:
                // TODO: do something with old one
                
                mLogger.info("Configuration updated: " + iEvent.getId());
                pushConfiguration(iEvent.getId(), tConfiguration);
                
                break;
                
            case REMOVED:
                mLogger.info("Configuration removed: " + iEvent.getId());

                // TODO: Erase the configuration on the device
                
                break;
        }
    }
    
    private void pushConfiguration(final String iId, final Configuration iConfiguration) {
        // Find the connections this configuration is a part of
        List<Connection> tRelevantConnections = new ArrayList<Connection>();
        for (Connection tConnection: mRepository.getConnections().values()) {
            if (tConnection.getConfiguration().equals(iId)) {
                tRelevantConnections.add(tConnection);
            }
        }
        
        // Push to the devices
        for (Connection tConnection: tRelevantConnections) {
            Kiosk tKiosk = mNetwork.getKiosk(tConnection.getKiosk());
            // No need to display too many errors here, this should already have
            // have happened when the connection was initially added to the
            // repository
            if (tKiosk != null) {
                try {
                    tKiosk.setConfiguration(iConfiguration);
                } catch (DeviceException tException) {
                    mLogger.error("Could not push configuration " + tConnection.getConfiguration() + " to target device '" + tConnection.getKiosk()  + "'", tException);
                }
            }
        }
    }
    
    //
    // RepositoryConnectionEvent handler
    //
    
    public final void onRepositoryConnectionEvent(final RepositoryConnectionEvent iEvent) {
        Connection tConnection = (Connection) iEvent.getEntity();
        
        switch (iEvent.getType()) {
            case ADDED:
                mLogger.info("Connection added: " + iEvent.getId());
                pushConnection(iEvent.getId(), tConnection);
                
                break;
                
            case UPDATED:
                // TODO: do something with old one
                
                mLogger.info("Connection updated: " + iEvent.getId());
                pushConnection(iEvent.getId(), tConnection);
                
                break;
                
            case REMOVED:
                mLogger.info("Connection removed: " + iEvent.getId());

                // TODO: Erase the connection on the device
                
                break;
        }
    }
    
    private void pushConnection(final String iId, final Connection iConnection) {
        // Check if there is a valid target device
        Kiosk tKiosk = mNetwork.getKiosk(iConnection.getKiosk());
        if (tKiosk == null) {
            mLogger.warn("Connection " + iId + " does not point to a valid device");
            return;
        }
        
        // Push the configuration
        Configuration tConfiguration = mRepository.getConfiguration(iConnection.getConfiguration());
        if (tConfiguration != null) {
            try {
                tKiosk.setConfiguration(tConfiguration);
            } catch (DeviceException tException) {
                mLogger.error("Could not upload configuration " + iId, tException);
            }
        } else {
            mLogger.warn("Connection " + iId + " does not point to a valid configuration");
        }

        // Push the presentation
        Presentation tPresentation = mRepository.getPresentation(iConnection.getPresentation());
        if (tPresentation != null) {
            try {
                tKiosk.setPresentation(tPresentation);
            } catch (DeviceException tException) {
                mLogger.error("Could not upload presentation " + iId, tException);
            }
        } else {
            mLogger.warn("Connection " + iId + " does not point to a valid presentation");
        }
    }
}
