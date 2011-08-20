/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data.details;

import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.controls.MediaControl;
import be.mira.adastra3.server.network.entities.Kiosk;
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
public class KioskDetail extends WContainerWidget {
    //
    // Data members
    //
    
    private Kiosk mKiosk;
    
    // Device box
    private WLineEdit mDeviceRevision, mDeviceVolume, mDeviceLatency, mDeviceMark;
    
    // Application box
    private WLineEdit mMediaLocation, mMediaRevision;
    
    // Events
    private Signal2<String, Exception> mEventError;
    private Signal1<String> mEventWarning, mEventInfo, mEventDebug;
    
    
    //
    // Construction and destruction
    //
    
    public KioskDetail(final WContainerWidget iParent) {
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
        
        // Kiosk information boxes
        tLayout.addWidget(createDeviceBox()); // Redundant?   
        tLayout.addWidget(createMediaBox()); // Redundant?      
    }
    
    private WGroupBox createDeviceBox() {
        WGroupBox tBox = new WGroupBox(this);
        tBox.setTitle("Device");
        
        WContainerWidget tInformation = new WContainerWidget(this);
        WGridLayout tInformationGrid = new WGridLayout(tInformation);
        tInformation.setLayout(tInformationGrid); // redundant?
        
        // Revision
        tInformationGrid.addWidget(new WLabel("Revision"), 0, 0);
        mDeviceRevision = new WLineEdit("<revision>");
        mDeviceRevision.setReadOnly(true);
        tInformationGrid.addWidget(mDeviceRevision, 0, 1);
        
        // Volume
        tInformationGrid.addWidget(new WLabel("Volume"), 1, 0);
        mDeviceVolume = new WLineEdit("<volume>");
        tInformationGrid.addWidget(mDeviceVolume, 1, 1);
        WPushButton tVolumeChange = new WPushButton("Change");
        tVolumeChange.clicked().addListener(this, mHandlerDeviceVolumeSet);
        tInformationGrid.addWidget(tVolumeChange, 1, 2);
        
        // Latency
        tInformationGrid.addWidget(new WLabel("Latency"), 2, 0);
        mDeviceLatency = new WLineEdit("<volume>");
        mDeviceLatency.setReadOnly(true);
        tInformationGrid.addWidget(mDeviceLatency, 2, 1);
        WPushButton tLatencyGet = new WPushButton("Ping");
        tLatencyGet.clicked().addListener(this, mHandlerDeviceLatencyGet);
        tInformationGrid.addWidget(tLatencyGet, 2, 2);
        
        // Mark time
        tInformationGrid.addWidget(new WLabel("Last contact"), 3, 0);
        mDeviceMark = new WLineEdit();
        mDeviceMark.setReadOnly(true);
        tInformationGrid.addWidget(mDeviceMark, 3, 1);
        // TODO: update hook        
        
        // Reboot
        WPushButton tReboot = new WPushButton("Reboot");
        tReboot.clicked().addListener(this, mHandlerDeviceReboot);
        tInformationGrid.addWidget(tReboot, 4, 0);
        
        // Shutdown
        WPushButton tShutdown = new WPushButton("Shutdown");
        tReboot.clicked().addListener(this, mHandlerDeviceShutdown);
        tInformationGrid.addWidget(tShutdown, 5, 0);
        
        tBox.addWidget(tInformation); // redundant?
        
        return tBox;
    }    
    
    private WGroupBox createMediaBox() {
        WGroupBox tBox = new WGroupBox(this);
        tBox.setTitle("Media");
        
        WContainerWidget tInformation = new WContainerWidget(this);
        WGridLayout tInformationGrid = new WGridLayout(tInformation);
        tInformation.setLayout(tInformationGrid); // redundant?
        
        // Revision
        tInformationGrid.addWidget(new WLabel("Revision"), 0, 0);
        mMediaRevision = new WLineEdit();
        mMediaRevision.setReadOnly(true);
        tInformationGrid.addWidget(mMediaRevision, 0, 1);
        
        // Location
        tInformationGrid.addWidget(new WLabel("Location"), 1, 0);
        mMediaLocation = new WLineEdit();
        tInformationGrid.addWidget(mMediaLocation, 1, 1);
        WPushButton tMediaLoad = new WPushButton("Set");
        tMediaLoad.clicked().addListener(this, mHandlerMediaLocationSet);
        tInformationGrid.addWidget(tMediaLoad, 1, 2);
        
        tBox.addWidget(tInformation); // redundant?
        
        return tBox;
    }
    
    //
    // Event handlers
    //
    
    private Signal.Listener mHandlerDeviceRevisionGet = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("getting device revision");
                mDeviceRevision.setText(String.valueOf(mKiosk.getDeviceControl().getRevision()));
            } catch (NetworkException tException) {
                mEventError.trigger("coult not get device revision", tException);
            }
        }
    };
    
    private Signal.Listener mHandlerDeviceVolumeSet = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("setting device volume to " + mDeviceVolume.getText());
                Integer tVolume = Integer.parseInt(mDeviceVolume.getText());
                mKiosk.getDeviceControl().setVolume(tVolume);
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
                mDeviceVolume.setText(mKiosk.getDeviceControl().getVolume().toString());
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
                mDeviceLatency.setText(String.valueOf(mKiosk.getDeviceControl().ping()) + " ms");
            } catch (NetworkException tException) {
                mEventError.trigger("coult not measure device latency", tException);
            }
        }
    };
    
    private Signal.Listener mHandlerDeviceMarkGet = new Signal.Listener() {
        @Override
        public void trigger() {
            debug().trigger("getting device mark");
            mDeviceMark.setText((new SimpleDateFormat()).format(mKiosk.getMark()));
        }
    };
    
    private Signal.Listener mHandlerDeviceReboot = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("rebooting device");
                mKiosk.getDeviceControl().reboot();
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
                mKiosk.getDeviceControl().shutdown();
            } catch (NetworkException tException) {
                mEventError.trigger("coult not shutdown device", tException);
            }
        }
    };
    
    private Signal.Listener mHandlerMediaRevisionGet = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("getting media revision");
                mMediaRevision.setText(String.valueOf(mKiosk.getMediaControl().getRevision()));
            } catch (NetworkException tException) {
                mEventError.trigger("coult not get media revision", tException);
            }
        }
    };
    
    private Signal.Listener mHandlerMediaLocationGet = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("getting media location");
                mMediaLocation.setText(mKiosk.getMediaControl().getLocation());
            } catch (NetworkException tException) {
                mEventError.trigger("coult not get media location", tException);
            }
        }
    };
    
    private Signal.Listener mHandlerMediaLocationSet = new Signal.Listener() {
        @Override
        public void trigger() {
            try {
                debug().trigger("setting media location to " + mMediaLocation.getText());
                mKiosk.getMediaControl().setLocation(mMediaLocation.getText());
            } catch (NetworkException tException) {
                mEventError.trigger("coult not set media location", tException);
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
    
    public final void loadKiosk(final Kiosk iDevice) {
        //TODO: shoudln't this be a kiosk?
        mKiosk = iDevice;
        info().trigger("loading details for kiosk '" + iDevice.getName() + "'");
        
        mHandlerDeviceRevisionGet.trigger();
        mHandlerDeviceVolumeGet.trigger();
        mHandlerDeviceLatencyGet.trigger();
        mHandlerDeviceMarkGet.trigger();
        
        mHandlerMediaRevisionGet.trigger();
        mHandlerMediaLocationGet.trigger();
    }
}
