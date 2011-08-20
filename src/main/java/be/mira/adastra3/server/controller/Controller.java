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
import be.mira.adastra3.server.network.devices.Device;
import be.mira.adastra3.server.repository.IRepositoryListener;
import be.mira.adastra3.server.repository.Repository;
import be.mira.adastra3.server.repository.configurations.Configuration;
import be.mira.adastra3.server.repository.configurations.Kiosk30Configuration;

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

    @Override
    public final void doRepositoryWarning(final String iMessage) {
        getLogger().warn("Repository warning: " + iMessage);
    }

    @Override
    public final void doRepositoryError(final String iMessage, final RepositoryException iException) {
        getLogger().error("Repository error: " + iMessage, iException);
    }
    
    @Override
    public final void doConfigurationAdded(final Configuration iConfiguration) {
        getLogger().info("Configuration added: " + iConfiguration.getId());
        
        // Check if there is a valid target device, and if so push the configuration
        Device tDevice = null;
        try {
            tDevice = Network.getInstance().getDevice(iConfiguration.getTarget());
            tDevice.setConfiguration(iConfiguration);
        } catch (NetworkException tException) {
            getLogger().warn("Configuration " + iConfiguration.getId() + " does not target a valid device");
        } catch (DeviceException tException) {
            getLogger().error("Could not push configuration " + iConfiguration.getId() + " to target device '" + tDevice.getUuid() + "'", tException);
        }
    }

    @Override
    public final void doConfigurationUpdated(final Configuration iOldConfiguration, final Configuration iConfiguration) {
        getLogger().info("Configuration updated: " + iConfiguration.getId());
        
        // Check if there is a valid target device, and if so update the configuration
        Device tDevice = null;
        try {
            tDevice = Network.getInstance().getDevice(iConfiguration.getTarget());
            tDevice.setConfiguration(iConfiguration);
        } catch (NetworkException tException) {
            getLogger().warn("Configuration " + iConfiguration.getId() + " does not target a valid device");
        } catch (DeviceException tException) {
            getLogger().error("Could not update configuration " + iConfiguration.getId() + " on target device '" + tDevice.getUuid() + "'", tException);
        }
    }
    
    @Override
    public final void doConfigurationRemoved(final Configuration iConfiguration) {
        getLogger().info("Configuration removed: " + iConfiguration.getId());
        
        // TODO: Erase the configuration on the device
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
    public final void doDeviceAdded(final Device iDevice) {
        getLogger().info("MIRA device added to network: " + iDevice.getUuid());
        Repository tRepository = Repository.getInstance();
            
        // Check if there is a configuration for this device
        try {
            Configuration tConfiguration = null;
            for (Configuration tAvailableConfiguration: tRepository.getConfigurations()) {
                if (tAvailableConfiguration.getTarget() == iDevice.getUuid()) {
                    tConfiguration = tAvailableConfiguration;
                    break;
                }
            }
            if (tConfiguration == null) {
                getLogger().warn("Couldn't find any configuration for device " + iDevice.getUuid() + ", it'll remain unconfigured");
            } else {
                getLogger().debug("Loading configuration " + tConfiguration.getId() + " onto device " + iDevice.getUuid());
                iDevice.setConfiguration(tConfiguration);
            }
        } catch (DeviceException tException) {
            getLogger().error("could not configure device", tException);
        }
    }

    @Override
    public final void doDeviceRemoved(final Device iDevice) {
        getLogger().info("MIRA device removed from network: " + iDevice.getUuid());
    }
}
