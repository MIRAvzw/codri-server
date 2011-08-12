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
import java.text.SimpleDateFormat;

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
    private WLineEdit mVolume, mLatency, mMark;
    
    // Events
    private Signal2<String, Exception> mEventError;
    private Signal1<String> mEventWarning, mEventInfo, mEventDebug;
    
    
    //
    // Construction and destruction
    //
    
    public Kiosk30Detail(final WContainerWidget iParent) {
        super(iParent);
        
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
        tVolumeGet.clicked().addListener(this, mHandlerVolumeGet);
        tInformationGrid.addWidget(tVolumeGet, 0, 2);
        WPushButton tVolumeSet = new WPushButton("Set");
        tVolumeSet.clicked().addListener(this, mHandlerVolumeSet);
        tInformationGrid.addWidget(tVolumeSet, 0, 3);
        
        // Latency
        tInformationGrid.addWidget(new WLabel("Latency"), 1, 0);
        mLatency = new WLineEdit("<volume>");
        mLatency.setReadOnly(true);
        tInformationGrid.addWidget(mLatency, 1, 1);
        WPushButton tLatencyGet = new WPushButton("Ping");
        tLatencyGet.clicked().addListener(this, mHandlerLatencyGet);
        tInformationGrid.addWidget(tLatencyGet, 1, 2);
        
        // Mark time
        tInformationGrid.addWidget(new WLabel("Last contact"), 2, 0);
        mMark = new WLineEdit("<volume>");
        mMark.setReadOnly(true);
        tInformationGrid.addWidget(mMark, 2, 1);
        // TODO: update hook
        
        
        // Reboot
        WPushButton tReboot = new WPushButton("Reboot");
        tReboot.clicked().addListener(this, mHandlerReboot);
        tInformationGrid.addWidget(tReboot, 3, 0);
        
        // Shutdown
        WPushButton tShutdown = new WPushButton("Shutdown");
        tReboot.clicked().addListener(this, mHandlerShutdown);
        tInformationGrid.addWidget(tShutdown, 4, 0);
        
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
    
    private Signal.Listener mHandlerVolumeSet = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("setting device volume to " + mVolume.getText());
                Integer tVolume = Integer.parseInt(mVolume.getText());
                mDevice.getDeviceControl().setVolume(tVolume);
            } catch (NumberFormatException tException) {
                mEventError.trigger("invalid volume specified", tException);
            } catch (NetworkException tException) {
                mEventError.trigger("coult not get device volume", tException);
            }
        }
    };
    
    private Signal.Listener mHandlerVolumeGet = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("requesting device volume");
                mVolume.setText(mDevice.getDeviceControl().getVolume().toString());
            } catch (NetworkException tException) {
                mEventError.trigger("coult not get device volume", tException);
            }
        }
    };
    
    private Signal.Listener mHandlerLatencyGet = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("measuring device latency");
                mLatency.setText(String.valueOf(mDevice.getDeviceControl().ping()) + " ms");
            } catch (NetworkException tException) {
                mEventError.trigger("coult not measure device latency", tException);
            }
        }
    };
    
    private Signal.Listener mHandlerMarkGet = new Signal.Listener() {
        @Override
        public void trigger() {
            debug().trigger("getting device mark");
            mMark.setText((new SimpleDateFormat()).format(mDevice.getMark()));
        }
    };
    
    private Signal.Listener mHandlerReboot = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("rebooting device");
                mDevice.getDeviceControl().reboot();
            } catch (NetworkException tException) {
                mEventError.trigger("coult not reboot device", tException);
            }
        }
    };
    
    private Signal.Listener mHandlerShutdown = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("shutting device down");
                mDevice.getDeviceControl().shutdown();
            } catch (NetworkException tException) {
                mEventError.trigger("coult not shutdown device", tException);
            }
        }
    };
    
    
    //
    // UI Events
    //
    
    public final Signal2<String, Exception> error() {
        return mEventError;
    }
    
    public final Signal1<String> warning() {
        return mEventWarning;
    }
    
    public final Signal1<String> info() {
        return mEventInfo;
    }
    
    public final Signal1<String> debug() {
        return mEventDebug;
    }
    
    //
    // Basic I/O
    //
    
    public final void loadDevice(final Kiosk30 iDevice) {
        mDevice = iDevice;
        info().trigger("loading details for device '" + iDevice.getName() + "'");
        mHandlerVolumeGet.trigger();
        mHandlerLatencyGet.trigger();
        mHandlerMarkGet.trigger();
    }
}
