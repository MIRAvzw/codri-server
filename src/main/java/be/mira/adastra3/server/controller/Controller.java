/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.controller;

import be.mira.adastra3.server.Service;
import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import be.mira.adastra3.server.network.INetworkListener;
import be.mira.adastra3.server.network.Network;
import be.mira.adastra3.server.network.controls.ApplicationControl;
import be.mira.adastra3.server.network.controls.DeviceControl;
import be.mira.adastra3.server.repository.IRepositoryListener;
import be.mira.adastra3.server.repository.Repository;
import be.mira.adastra3.server.repository.configurations.KioskConfiguration;
import java.util.UUID;

/**
 *
 * @author tim
 */
public class Controller extends Service implements INetworkListener, IRepositoryListener {
    //
    // Data members
    //
    
    Network mNetwork;
    Repository mRepository;
    
    
    //
    // Construction and destruction
    //
    
    public Controller() throws ServiceSetupException {
        mNetwork = Network.getInstance();
        mRepository = Repository.getInstance();
    }
    
    
    //
    // Service interface
    //

    @Override
    public void run() throws ServiceRunException {
        mNetwork.addListener(this);
        mRepository.addListener(this);
    }

    @Override
    public void stop() throws ServiceRunException {
        mNetwork.removeListener(this);
        mRepository.removeListener(this);
    }
    
    
    //
    // Repository listener interface
    //

    @Override
    public void doRepositoryWarning(String iMessage) {
        getLogger().warn("Repository warning: " + iMessage);
    }

    @Override
    public void doRepositoryError(String iMessage, RepositoryException iException) {
        getLogger().error("Repository error: " + iMessage, iException);
    }
    
    @Override
    public void doKioskConfigurationAdded(KioskConfiguration iKioskConfiguration) {
        getLogger().info("Kiosk configuration added: " + iKioskConfiguration.getId());
    }

    @Override
    public void doKioskConfigurationUpdated(KioskConfiguration iOldKioskConfiguration, KioskConfiguration iKioskConfiguration) {
        getLogger().info("Kiosk configuration updated: " + iKioskConfiguration.getId());
    }
    
    
    //
    // Network listener interface
    //

    @Override
    public void doNetworkWarning(String iMessage) {
        getLogger().warn("Network warning: " + iMessage);
    }

    @Override
    public void doNetworkError(String iMessage, NetworkException iException) {
        getLogger().error("Network error: " + iMessage, iException);
    }

    @Override
    public void doApplicationControlAdded(UUID iUuid, ApplicationControl iApplicationControl) {
        getLogger().info("Application control added on kiosk " + iUuid);
    }

    @Override
    public void doApplicationControlRemoved(UUID iUuid) {
        getLogger().info("Application control removed from kiosk " + iUuid);
    }

    @Override
    public void doDeviceControlAdded(UUID iUuid, DeviceControl iMediaControl) {
        getLogger().info("Device control added on kiosk " + iUuid);
    }

    @Override
    public void doDeviceControlRemoved(UUID iUuid) {
        getLogger().info("Device control removed from kiosk " + iUuid);
    }
}
