/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data.details;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.devices.Kiosk30;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.Signal2;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WGridLayout;
import eu.webtoolkit.jwt.WGroupBox;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WVBoxLayout;

/**
 *
 * @author tim
 */
public class Kiosk30Detail extends WContainerWidget {
    //
    // Data members
    //
    
    private Kiosk30 mDevice;
    
    // Device box
    WLineEdit mVolume;
    
    // Events
    Signal2<String, Exception> mEventError;
    Signal1<String> mEventWarning, mEventInfo, mEventDebug;
    
    
    //
    // Construction and destruction
    //
    
    public Kiosk30Detail(WContainerWidget parent) {
        super(parent);
        
        // Configure the events
        mEventError = new Signal2<String, Exception>();
        mEventWarning = new Signal1<String>();
        mEventInfo = new Signal1<String>();
        mEventDebug = new Signal1<String>();
        
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
        
        WContainerWidget tInformation = new WContainerWidget(this);
        WGridLayout tInformationGrid = new WGridLayout(tInformation);
        tInformation.setLayout(tInformationGrid); // redundant?
        
        // Volume
        tInformationGrid.addWidget(new WLabel("Volume"), 0, 0);
        mVolume = new WLineEdit("<volume>");
        tInformationGrid.addWidget(mVolume, 0, 1);
        WPushButton tVolumeGet = new WPushButton("Get");
        tVolumeGet.clicked().addListener(this, doVolumeGet);
        tInformationGrid.addWidget(tVolumeGet, 0, 2);
        WPushButton tVolumeSet = new WPushButton("Set");
        tVolumeSet.clicked().addListener(this, doVolumeSet);
        tInformationGrid.addWidget(tVolumeSet, 0, 3);
        
        tBox.addWidget(tInformation); // redundant?
        
        return tBox;
    }    
    
    private WGroupBox createApplicationBox() {
        WGroupBox tBox = new WGroupBox(this);
        tBox.setTitle("Application");
        
        return tBox;
    }
    
    //
    // Event handlers
    //
    
    private Signal.Listener doVolumeSet = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("setting device volume to " + mVolume.getText());
                Integer tVolume = Integer.parseInt(mVolume.getText());
                mDevice.getDeviceControl().SetVolume(tVolume);
            } catch (NumberFormatException iException) {
                mEventError.trigger("invalid volume specified", iException);
            } catch (NetworkException iException) {
                mEventError.trigger("coult not get device volume", iException);
            }
        }
    };
    
    private Signal.Listener doVolumeGet = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                mVolume.setText(mDevice.getDeviceControl().GetVolume().toString());
            } catch (NetworkException iException) {
                mEventError.trigger("coult not get device volume", iException);
            }
        }
    };
    
    
    //
    // UI Events
    //
    
    public Signal2<String, Exception> error() {
        return mEventError;
    }
    
    public Signal1<String> warning() {
        return mEventWarning;
    }
    
    public Signal1<String> info() {
        return mEventInfo;
    }
    
    public Signal1<String> debug() {
        return mEventDebug;
    }
    
    //
    // Basic I/O
    //
    
    public void loadDevice(Kiosk30 iDevice) {
        mDevice = iDevice;
        info().trigger("loading details for device '" + iDevice.getName() + "'");
        doVolumeGet.trigger();
    }
}
