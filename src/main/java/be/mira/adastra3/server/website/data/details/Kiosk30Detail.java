/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data.details;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.devices.Kiosk30;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WGridLayout;
import eu.webtoolkit.jwt.WGroupBox;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WVBoxLayout;
import org.apache.log4j.Logger;

/**
 *
 * @author tim
 */
public class Kiosk30Detail extends WContainerWidget {
    //
    // Data members
    //
    
    // Device box
    WLineEdit mVolume;
    
    
    //
    // Construction and destruction
    //
    
    public Kiosk30Detail(WContainerWidget parent) {
        super(parent);
        
        createUI();
    }
    
    
    //
    // UI creation
    //
    
    private void createUI() {
        // Layout
        WVBoxLayout tLayout = new WVBoxLayout(this);
        setLayout(tLayout); // Redundant?
        
        // Kiosk30 information boxes
        tLayout.addWidget(createDeviceBox()); // Redundant?        
    }
    
    private WGroupBox createDeviceBox() {
        WGroupBox tBox = new WGroupBox(this);
        tBox.setTitle("Device");
        
        // Information
        WContainerWidget tInformation = new WContainerWidget(this);
        WGridLayout tInformationGrid = new WGridLayout(tInformation);
        tInformation.setLayout(tInformationGrid); // redundant?
        tInformationGrid.addWidget(new WLabel("Volume"), 0, 0);
        mVolume = new WLineEdit("<volume>");
        tInformationGrid.addWidget(mVolume, 0, 1);
        tBox.addWidget(tInformation); // redundant?
        
        return tBox;
    }
    
    
    private WGroupBox createApplicationBox() {
        WGroupBox tBox = new WGroupBox(this);
        tBox.setTitle("Application");
        
        return tBox;
    }
    
    //
    // Basic I/O
    //
    
    public void loadDevice(Kiosk30 iDevice) {
        // Device information
        try {
            mVolume.setText(iDevice.getDeviceControl().GetVolume().toString());
        } catch (NetworkException iException) {
            Logger.getLogger(this.getClass()).error("could not get volume", iException);
            mVolume.setText("could not fetch volume");
        }
    }
}
