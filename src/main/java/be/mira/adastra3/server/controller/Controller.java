/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.controller;

import be.mira.adastra3.server.Service;
import be.mira.adastra3.server.exceptions.DeviceException;
import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import be.mira.adastra3.server.network.INetworkListener;
import be.mira.adastra3.server.network.Network;
import be.mira.adastra3.server.network.NetworkEntity;
import be.mira.adastra3.server.network.Kiosk;
import be.mira.adastra3.server.repository.IRepositoryListener;
import be.mira.adastra3.server.repository.Repository;
import be.mira.adastra3.server.repository.configuration.Configuration;
import be.mira.adastra3.server.repository.connection.Connection;
import be.mira.adastra3.server.repository.presentation.Presentation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tim
 */
public class Controller extends Service implements INetworkListener, IRepositoryListener {
    //
    // Data members
    //
    
    private Network mNetwork;
    private Repository mRepository;
    
    
    //
    // Construction and destruction
    //
    
    public Controller() throws ServiceSetupException {
        mNetwork = Network.getInstance();
        mRepository = Repository.getInstance();
        
        mNetwork.addListener(this);
        mRepository.addListener(this);
    }
    
    
    //
    // Service interface
    //

    @Override
    public final void run() throws ServiceRunException {
    }

    @Override
    public final void stop() throws ServiceRunException {
        mNetwork.removeListener(this);
        mRepository.removeListener(this);
    }
    
    
    //
    // Repository listener interface
    //
    
    // TODO: the doRepository as well as doPresentation(Update|Added) functions are
    //       quite similar, it would be nice to deduplicate this code&

    @Override
    public final void doRepositoryWarning(final String iMessage) {
        getLogger().warn("Repository warning: " + iMessage);
    }

    @Override
    public final void doRepositoryError(final String iMessage, final RepositoryException iException) {
        getLogger().error("Repository error: " + iMessage, iException);
    }
    
    
    private void pushConnection(final Connection iConnection) {
        // Check if there is a valid target device
        NetworkEntity tDevice = Network.getInstance().getDevice(iConnection.getKiosk());
        if (tDevice == null) {
            getLogger().warn("Connection " + iConnection.getId() + " does not point to a valid device");
            return;
        }
        
        // Process all types of devices
        if (tDevice instanceof Kiosk) {
            Kiosk tKiosk = (Kiosk) tDevice;
            
            // Push the configuration
            Configuration tConfiguration = Repository.getInstance().getConfiguration(iConnection.getConfiguration());
            if (tConfiguration != null) {
                try {
                    tKiosk.setConfiguration(tConfiguration);
                } catch (DeviceException tException) {
                    getLogger().error("Could not upload configuration " + tConfiguration.getId(), tException);
                }
            } else {
                getLogger().warn("Connection " + iConnection.getId() + " does not point to a valid configuration");
            }
            
            // Push the presentation
            Presentation tPresentation = Repository.getInstance().getPresentation(iConnection.getPresentation());
            if (tPresentation != null) {
                try {
                    tKiosk.setPresentation(tPresentation);
                } catch (DeviceException tException) {
                    getLogger().error("Could not upload presentation " + tConfiguration.getId(), tException);
                }
            } else {
                getLogger().warn("Connection " + iConnection.getId() + " does not point to a valid presentation");
            }
        } else {
            getLogger().error("Cannot handle a device of type " + tDevice.getType());
        }        
    }
    @Override
    public final void doConnectionAdded(final Connection iConnection) {
        getLogger().info("Connection added: " + iConnection.getId());
        pushConnection(iConnection);
    }

    @Override
    public final void doConnectionUpdated(final Connection iOldConnection, final Connection iConnection) {
        getLogger().info("Connection updated: " + iConnection.getId());
        pushConnection(iConnection);
    }
    
    @Override
    public final void doConnectionRemoved(final Connection iConnection) {
        getLogger().info("Connection removed: " + iConnection.getId());
        
        // TODO: Erase the connection on the device
    }
    
    private void pushConfiguration(final Configuration iConfiguration) {
        // Find the connections this configuration is a part of
        List<Connection> tRelevantConnections = new ArrayList<Connection>();
        for (Connection tConnection: Repository.getInstance().getConnections().values()) {
            if (tConnection.getConfiguration().equals(iConfiguration.getId())) {
                tRelevantConnections.add(tConnection);
            }
        }
        
        // Push to the devices
        for (Connection tConnection: tRelevantConnections) {
            NetworkEntity tDevice = Network.getInstance().getDevice(tConnection.getKiosk());
            // No need to display too many errors here, this should already have
            // have happened when the connection was initially added to the
            // repository
            if (tDevice != null) {
                if (tDevice instanceof Kiosk) {
                    Kiosk tKiosk = (Kiosk) tDevice;
                    try {
                        tKiosk.setConfiguration(iConfiguration);
                    } catch (DeviceException tException) {
                        getLogger().error("Could not push configuration " + iConfiguration.getId() + " to target device '" + tDevice.getUuid() + "'", tException);
                    }
                }
            }
        }        
    }
    
    @Override
    public final void doConfigurationAdded(final Configuration iConfiguration) {
        getLogger().info("Configuration added: " + iConfiguration.getId());
        pushConfiguration(iConfiguration);
    }

    @Override
    public final void doConfigurationUpdated(final Configuration iOldConfiguration, final Configuration iConfiguration) {
        getLogger().info("Configuration updated: " + iConfiguration.getId());
        pushConfiguration(iConfiguration);
    }
    
    @Override
    public final void doConfigurationRemoved(final Configuration iConfiguration) {
        getLogger().info("Configuration removed: " + iConfiguration.getId());
        
        // TODO: Erase the configuration on the device
    }
    
    
    private void pushPresentation(final Presentation iPresentation) {
        // Find the connections this presentation is a part of
        List<Connection> tRelevantConnections = new ArrayList<Connection>();
        for (Connection tConnection: Repository.getInstance().getConnections().values()) {
            if (tConnection.getPresentation().equals(iPresentation.getId())) {
                tRelevantConnections.add(tConnection);
            }
        }
        
        // Push to the devices
        for (Connection tConnection: tRelevantConnections) {
            NetworkEntity tDevice = Network.getInstance().getDevice(tConnection.getKiosk());
            // No need to display too many errors here, this should already have
            // have happened when the connection was initially added to the
            // repository
            if (tDevice != null) {
                if (tDevice instanceof Kiosk) {
                    Kiosk tKiosk = (Kiosk) tDevice;
                    try {
                        tKiosk.setPresentation(iPresentation);
                    } catch (DeviceException tException) {
                        getLogger().error("Could not push presentation " + iPresentation.getId() + " to target device '" + tDevice.getUuid() + "'", tException);
                    }
                }
            }
        }        
    }
    
    @Override
    public final void doPresentationAdded(final Presentation iPresentation) {
        getLogger().info("Presentation added: " + iPresentation.getId());
        pushPresentation(iPresentation);
    }

    @Override
    public final void doPresentationUpdated(final Presentation iOldPresentation, final Presentation iPresentation) {
        getLogger().info("Presentation updated: " + iPresentation.getId());
        pushPresentation(iPresentation);
    }
    
    @Override
    public final void doPresentationRemoved(final Presentation iPresentation) {
        getLogger().info("Presentation removed: " + iPresentation.getId());
        
        // TODO
    }
    
    
    //
    // Network listener interface
    //

    @Override
    public final void doNetworkWarning(final String iMessage) {
        getLogger().warn("Network warning: " + iMessage);
    }

    @Override
    public final void doNetworkError(final String iMessage, final NetworkException iException) {
        getLogger().error("Network error: " + iMessage, iException);
    }

    @Override
    public final void doEntityAdded(final NetworkEntity iEntity) {
        getLogger().info("MIRA network entity added: " + iEntity.getUuid());
        
        // Find the connections this device is a part of
        List<Connection> tRelevantConnections = new ArrayList<Connection>();
        for (Connection tConnection: Repository.getInstance().getConnections().values()) {
            if (tConnection.getKiosk().equals(iEntity.getUuid())) {
                tRelevantConnections.add(tConnection);
            }
        }
        
        // Check the connections
        if (tRelevantConnections.isEmpty()) {
            getLogger().warn("Couldn't find any configuration for device " + iEntity.getUuid() + ", it'll remain unconfigured");
        } else if (tRelevantConnections.size() > 1) {
            getLogger().warn("Ambiguous connections found for device " + iEntity.getUuid() + ", it'll remain unconfigured");
        } else {
            Connection tConnection = tRelevantConnections.get(0);
            pushConnection(tConnection);
        }
    }

    @Override
    public final void doEntityRemoved(final NetworkEntity iEntity) {
        getLogger().info("MIRA network entity removed: " + iEntity.getUuid());
    }
}
