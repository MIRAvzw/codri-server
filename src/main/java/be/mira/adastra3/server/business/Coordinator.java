/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package be.mira.adastra3.server.business;

import be.mira.adastra3.spring.Logger;
import be.mira.adastra3.server.events.*;
import be.mira.adastra3.server.exceptions.DeviceException;
import be.mira.adastra3.server.network.NetworkEntity;
import be.mira.adastra3.server.network.Kiosk;
import be.mira.adastra3.server.repository.configuration.Configuration;
import be.mira.adastra3.server.repository.connection.Connection;
import be.mira.adastra3.server.repository.presentation.Presentation;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
*
* @author tim
*/
public class Coordinator implements ApplicationListener {
    //
    // Data members
    //
    
    @Logger
    private Log mLogger;
    
    private Network mNetwork;
    private Repository mRepository;
    
    
    //
    // Construction and destruction
    //
    
    public Coordinator(Network iNetwork, Repository iRepository) {
        mNetwork = iNetwork;
        mRepository = iRepository;
    }
    
    
    //
    // ApplicationEvent handler
    //
    
    // TODO: filter to MiraEvent?
    
    @Override
    public void onApplicationEvent(ApplicationEvent iEvent) {
        
    }
    
    
    //
    // NetworkEvent handler
    //
    
    public void onNetworkEvent(NetworkEvent iEvent) {
        NetworkEntity tDevice = iEvent.getDevice();
        
        switch (iEvent.getType()) {
            case ADDED: {
                mLogger.info("MIRA network entity added: " + tDevice.getUuid());

                // Find the connections this device is a part of
                List<Connection> tRelevantConnections = new ArrayList<Connection>();
                for (Connection tConnection : mRepository.getConnectionsMap().values()) {
                    if (tConnection.getKiosk().equals(tDevice.getUuid())) {
                        tRelevantConnections.add(tConnection);
                    }
                }

                // Check the connections
                if (tRelevantConnections.isEmpty()) {
                    mLogger.warn("Couldn't find any configuration for device " + tDevice.getUuid() + ", it'll remain unconfigured");
                } else if (tRelevantConnections.size() > 1) {
                    mLogger.warn("Ambiguous connections found for device " + tDevice.getUuid() + ", it'll remain unconfigured");
                } else {
                    Connection tConnection = tRelevantConnections.get(0);
                    pushConnection(tConnection);
                }
                
                break;
            }
                
            case REMOVED: {
                mLogger.info("MIRA network entity removed: " + tDevice.getUuid());                
            }
                
        }
    }
    
    
    //
    // RepositoryPresentationEvent handler
    //
    
    // TODO: the doRepository as well as doPresentation(Update|Added) functions are
    // quite similar, it would be nice to deduplicate this code&
    
    public void onRepositoryPresentationEvent(RepositoryPresentationEvent iEvent) {
        Presentation tPresentation = (Presentation) iEvent.getEntity();
        
        switch (iEvent.getType()) {
            case ADDED: {
                mLogger.info("Presentation added: " + tPresentation.getId());
                pushPresentation(tPresentation);
                
                break;                
            }
                
            case UPDATED: {
                // TODO: do something with old one
                
                mLogger.info("Presentation updated: " + tPresentation.getId());
                pushPresentation(tPresentation);
                
                break;              
            }
                
            case REMOVED: {
                mLogger.info("Presentation removed: " + tPresentation.getId());

                // TODO
                
                break;                
            }
        }
    }
        
    private void pushPresentation(final Presentation iPresentation) {
        // Find the connections this presentation is a part of
        List<Connection> tRelevantConnections = new ArrayList<Connection>();
        for (Connection tConnection: mRepository.getConnectionsMap().values()) {
            if (tConnection.getPresentation().equals(iPresentation.getId())) {
                tRelevantConnections.add(tConnection);
            }
        }
        
        // Push to the devices
        for (Connection tConnection: tRelevantConnections) {
            NetworkEntity tDevice = mNetwork.getDevice(tConnection.getKiosk());
            // No need to display too many errors here, this should already have
            // have happened when the connection was initially added to the
            // repository
            if (tDevice != null) {
                if (tDevice instanceof Kiosk) {
                    Kiosk tKiosk = (Kiosk) tDevice;
                    try {
                        tKiosk.setPresentation(iPresentation);
                    } catch (DeviceException tException) {
                        mLogger.error("Could not push presentation " + iPresentation.getId() + " to target device '" + tDevice.getUuid() + "'", tException);
                    }
                }
            }
        }
    }
    
    
    //
    // RepositoryConfigurationEvent handler
    //
    
    public void onRepositoryConfigurationEvent(RepositoryConfigurationEvent iEvent) {
        Configuration tConfiguration = (Configuration) iEvent.getEntity();
        
        switch (iEvent.getType()) {
            case ADDED: {
                mLogger.info("Configuration added: " + tConfiguration.getId());
                pushConfiguration(tConfiguration);
                
                break;
            }
                
            case UPDATED: {
                // TODO: do something with old one
                
                mLogger.info("Configuration updated: " + tConfiguration.getId());
                pushConfiguration(tConfiguration);
                
                break;
            }
                
            case REMOVED: {
                mLogger.info("Configuration removed: " + tConfiguration.getId());

                // TODO: Erase the configuration on the device
                
                break;
            }
        }
    }
    
    private void pushConfiguration(final Configuration iConfiguration) {
        // Find the connections this configuration is a part of
        List<Connection> tRelevantConnections = new ArrayList<Connection>();
        for (Connection tConnection: mRepository.getConnectionsMap().values()) {
            if (tConnection.getConfiguration().equals(iConfiguration.getId())) {
                tRelevantConnections.add(tConnection);
            }
        }
        
        // Push to the devices
        for (Connection tConnection: tRelevantConnections) {
            NetworkEntity tDevice = mNetwork.getDevice(tConnection.getKiosk());
            // No need to display too many errors here, this should already have
            // have happened when the connection was initially added to the
            // repository
            if (tDevice != null) {
                if (tDevice instanceof Kiosk) {
                    Kiosk tKiosk = (Kiosk) tDevice;
                    try {
                        tKiosk.setConfiguration(iConfiguration);
                    } catch (DeviceException tException) {
                        mLogger.error("Could not push configuration " + iConfiguration.getId() + " to target device '" + tDevice.getUuid() + "'", tException);
                    }
                }
            }
        }
    }
    
    //
    // RepositoryConnectionEvent handler
    //
    
    public void onRepositoryConnectionEvent(RepositoryConnectionEvent iEvent) {
        Connection tConnection = (Connection) iEvent.getEntity();
        
        switch (iEvent.getType()) {
            case ADDED: {
                mLogger.info("Connection added: " + tConnection.getId());
                pushConnection(tConnection);
                
                break;
            }
                
            case UPDATED: {
                // TODO: do something with old one
                
                mLogger.info("Connection updated: " + tConnection.getId());
                pushConnection(tConnection);
                
                break;
            }
                
            case REMOVED: {
                mLogger.info("Connection removed: " + tConnection.getId());

                // TODO: Erase the connection on the device
                
                break;
            }
        }
    }
    
    private void pushConnection(final Connection iConnection) {
        // Check if there is a valid target device
        NetworkEntity tDevice = mNetwork.getDevice(iConnection.getKiosk());
        if (tDevice == null) {
            mLogger.warn("Connection " + iConnection.getId() + " does not point to a valid device");
            return;
        }
        
        // Process all types of devices
        if (tDevice instanceof Kiosk) {
            Kiosk tKiosk = (Kiosk) tDevice;
            
            // Push the configuration
            Configuration tConfiguration = mRepository.getConfiguration(iConnection.getConfiguration());
            if (tConfiguration != null) {
                try {
                    tKiosk.setConfiguration(tConfiguration);
                } catch (DeviceException tException) {
                    mLogger.error("Could not upload configuration " + tConfiguration.getId(), tException);
                }
            } else {
                mLogger.warn("Connection " + iConnection.getId() + " does not point to a valid configuration");
            }
            
            // Push the presentation
            Presentation tPresentation = mRepository.getPresentation(iConnection.getPresentation());
            if (tPresentation != null) {
                try {
                    tKiosk.setPresentation(tPresentation);
                } catch (DeviceException tException) {
                    mLogger.error("Could not upload presentation " + tConfiguration.getId(), tException);
                }
            } else {
                mLogger.warn("Connection " + iConnection.getId() + " does not point to a valid presentation");
            }
        } else {
            mLogger.error("Cannot handle a device of type " + tDevice.getType());
        }
    }
}