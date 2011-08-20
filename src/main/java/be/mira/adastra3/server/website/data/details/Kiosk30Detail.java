/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data.details;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.controls.ApplicationControl;
import be.mira.adastra3.server.network.devices.Kiosk30;
import be.mira.adastra3.server.repository.media.Media;
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
    private WLineEdit mDeviceVolume, mDeviceLatency, mDeviceMark;
    
    // Application box
    private WLineEdit mApplicationMediaIdentifier, mApplicationMediaLocation, mApplicationMediaRevision;
    
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
        tLayout.addWidget(createApplicationBox()); // Redundant?      
    }
    
    private WGroupBox createDeviceBox() {
        WGroupBox tBox = new WGroupBox(this);
        tBox.setTitle("Device");
        
        WContainerWidget tInformation = new WContainerWidget(this);
        WGridLayout tInformationGrid = new WGridLayout(tInformation);
        tInformation.setLayout(tInformationGrid); // redundant?
        
        // Volume
        tInformationGrid.addWidget(new WLabel("Volume"), 0, 0);
        mDeviceVolume = new WLineEdit("<volume>");
        tInformationGrid.addWidget(mDeviceVolume, 0, 1);
        WPushButton tVolumeChange = new WPushButton("Change");
        tVolumeChange.clicked().addListener(this, mHandlerDeviceVolumeSet);
        tInformationGrid.addWidget(tVolumeChange, 0, 2);
        
        // Latency
        tInformationGrid.addWidget(new WLabel("Latency"), 1, 0);
        mDeviceLatency = new WLineEdit("<volume>");
        mDeviceLatency.setReadOnly(true);
        tInformationGrid.addWidget(mDeviceLatency, 1, 1);
        WPushButton tLatencyGet = new WPushButton("Ping");
        tLatencyGet.clicked().addListener(this, mHandlerDeviceLatencyGet);
        tInformationGrid.addWidget(tLatencyGet, 1, 2);
        
        // Mark time
        tInformationGrid.addWidget(new WLabel("Last contact"), 2, 0);
        mDeviceMark = new WLineEdit();
        mDeviceMark.setReadOnly(true);
        tInformationGrid.addWidget(mDeviceMark, 2, 1);
        // TODO: update hook
        
        
        // Reboot
        WPushButton tReboot = new WPushButton("Reboot");
        tReboot.clicked().addListener(this, mHandlerDeviceReboot);
        tInformationGrid.addWidget(tReboot, 3, 0);
        
        // Shutdown
        WPushButton tShutdown = new WPushButton("Shutdown");
        tReboot.clicked().addListener(this, mHandlerDeviceShutdown);
        tInformationGrid.addWidget(tShutdown, 4, 0);
        
        tBox.addWidget(tInformation); // redundant?
        
        return tBox;
    }    
    
    private WGroupBox createApplicationBox() {
        WGroupBox tBox = new WGroupBox(this);
        tBox.setTitle("Application");
        
        WContainerWidget tInformation = new WContainerWidget(this);
        WGridLayout tInformationGrid = new WGridLayout(tInformation);
        tInformation.setLayout(tInformationGrid); // redundant?
        
        // Media
        tInformationGrid.addWidget(new WLabel("Media identifier"), 0, 0);
        mApplicationMediaIdentifier = new WLineEdit();
        tInformationGrid.addWidget(mApplicationMediaIdentifier, 0, 1);
        tInformationGrid.addWidget(new WLabel("Media location"), 1, 0);
        mApplicationMediaLocation = new WLineEdit();
        tInformationGrid.addWidget(mApplicationMediaLocation, 1, 1);
        WPushButton tMediaLoad = new WPushButton("Load");
        tMediaLoad.clicked().addListener(this, mHandlerApplicationMediaSet);
        tInformationGrid.addWidget(tMediaLoad, 1, 2);
        tInformationGrid.addWidget(new WLabel("Media revision"), 2, 0);
        mApplicationMediaRevision = new WLineEdit();
        mApplicationMediaRevision.setReadOnly(true);
        tInformationGrid.addWidget(mApplicationMediaRevision, 2, 1);
        
        tBox.addWidget(tInformation); // redundant?
        
        return tBox;
    }
    
    //
    // Event handlers
    //
    
    private Signal.Listener mHandlerDeviceVolumeSet = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("setting device volume to " + mDeviceVolume.getText());
                Integer tVolume = Integer.parseInt(mDeviceVolume.getText());
                mDevice.getDeviceControl().setVolume(tVolume);
            } catch (NumberFormatException tException) {
                mEventError.trigger("invalid volume specified", tException);
            } catch (NetworkException tException) {
                mEventError.trigger("coult not get device volume", tException);
            }
        }
    };
    
    private Signal.Listener mHandlerDeviceVolumeGet = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("requesting device volume");
                mDeviceVolume.setText(mDevice.getDeviceControl().getVolume().toString());
            } catch (NetworkException tException) {
                mEventError.trigger("coult not get device volume", tException);
            }
        }
    };
    
    private Signal.Listener mHandlerDeviceLatencyGet = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("measuring device latency");
                mDeviceLatency.setText(String.valueOf(mDevice.getDeviceControl().ping()) + " ms");
            } catch (NetworkException tException) {
                mEventError.trigger("coult not measure device latency", tException);
            }
        }
    };
    
    private Signal.Listener mHandlerMarkGet = new Signal.Listener() {
        @Override
        public void trigger() {
            debug().trigger("getting device mark");
            mDeviceMark.setText((new SimpleDateFormat()).format(mDevice.getMark()));
        }
    };
    
    private Signal.Listener mHandlerDeviceReboot = new Signal.Listener() {
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
    
    private Signal.Listener mHandlerDeviceShutdown = new Signal.Listener() {
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
    
    private Signal.Listener mHandlerApplicationMediaGet = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("getting media");
                Media tMedia = mDevice.getApplicationControl().getMedia();
                mApplicationMediaIdentifier.setText(tMedia.getId());
                mApplicationMediaLocation.setText(tMedia.getLocation());
                mApplicationMediaRevision.setText(String.valueOf(tMedia.getRevision()));
            } catch (NetworkException tException) {
                mEventError.trigger("coult not get media parameters", tException);
            }
        }
    };
    
    private Signal.Listener mHandlerApplicationMediaSet = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("loading media parameters");
                Media tMedia = new Media(
                        mApplicationMediaIdentifier.getText(),
                        mApplicationMediaLocation.getText()
                );
                mDevice.getApplicationControl().setMedia(tMedia);
            } catch (NetworkException tException) {
                mEventError.trigger("coult not load media parameters", tException);
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
        mHandlerDeviceVolumeGet.trigger();
        mHandlerDeviceLatencyGet.trigger();
        mHandlerMarkGet.trigger();
        mHandlerApplicationMediaGet.trigger();
    }
}
