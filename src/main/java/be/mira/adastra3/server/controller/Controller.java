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
import be.mira.adastra3.server.network.devices.Kiosk30;
import be.mira.adastra3.server.repository.IRepositoryListener;
import be.mira.adastra3.server.repository.Repository;
import be.mira.adastra3.server.repository.configurations.KioskConfiguration;

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
    public final void doKioskConfigurationAdded(final KioskConfiguration iKioskConfiguration) {
        getLogger().info("Kiosk configuration added: " + iKioskConfiguration.getId());
    }

    @Override
    public final void doKioskConfigurationUpdated(final KioskConfiguration iOldKioskConfiguration, final KioskConfiguration iKioskConfiguration) {
        getLogger().info("Kiosk configuration updated: " + iKioskConfiguration.getId());
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
        
        if (iDevice instanceof Kiosk30) {
            Kiosk30 tKiosk = (Kiosk30) iDevice;
            
            // Check if there is a configuration for this device
            KioskConfiguration tKioskConfiguration = tRepository.lookupKioskConfiguration(iDevice.getUuid());

            if (tKioskConfiguration != null) {
                getLogger().debug("Loading configuration " + tKioskConfiguration.getId() + " onto device " + iDevice.getUuid());
                try {
                    tKiosk.setConfiguration(tKioskConfiguration);
                } catch (DeviceException tException) {
                    getLogger().warn("could not configure device", tException);
                }
            } else {
                getLogger().warn("could find any configuration for device " + iDevice.getUuid() + ", it'll remain unconfigured");
            }
        } else {
            getLogger().warn("unknown MIRA device");
        }
    }

    @Override
    public final void doDeviceRemoved(final Device iDevice) {
        getLogger().info("MIRA device removed from network: " + iDevice.getUuid());
    }
}
