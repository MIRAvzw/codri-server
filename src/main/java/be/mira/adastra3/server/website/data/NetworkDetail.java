/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data;

import be.mira.adastra3.server.network.devices.Device;
import be.mira.adastra3.server.network.devices.Kiosk30;
import be.mira.adastra3.server.website.data.details.Kiosk30Detail;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.Signal2;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WStackedWidget;
import eu.webtoolkit.jwt.WText;

/**
 *
 * @author tim
 */
public class NetworkDetail extends WStackedWidget {
    //
    // Data members
    //
    
    // Widgets
    WContainerWidget mDetailDummy;
    Kiosk30Detail mDetailKiosk30;
    
    // Events
    Signal2<String, Exception> mEventError;
    Signal1<String> mEventWarning, mEventInfo, mEventDebug;
    
    //
    // Construction and destruction
    //
    
    public NetworkDetail(WContainerWidget parent) {
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
        // Dummy detail
        mDetailDummy = createDetailDummy(this);
        addWidget(mDetailDummy); // Redundant?
        
        // Kiosk30 detail
        mDetailKiosk30 = new Kiosk30Detail(this);
        mDetailKiosk30.error().addListener(this, new Signal2Bubbler<String, Exception>(mEventError));
        mDetailKiosk30.warning().addListener(this, new Signal1Bubbler<String>(mEventWarning));
        mDetailKiosk30.info().addListener(this, new Signal1Bubbler<String>(mEventInfo));
        mDetailKiosk30.debug().addListener(this, new Signal1Bubbler<String>(mEventDebug));
        addWidget(mDetailKiosk30); // Redundant?
        
        showDetail(null);        
    }
    
    public WContainerWidget createDetailDummy(WContainerWidget iParent) {
        // Layout
        WContainerWidget tWidget = new WContainerWidget(iParent);
        
        tWidget.addWidget(new WText("Please select a device"));
        
        return tWidget;   
    }
    
    
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
    
    public void showDetail(NetworkItem iNetworkItem) {
        if (iNetworkItem == null) {
            setCurrentWidget(mDetailDummy);
            return;
        }
        
        Device iDevice = iNetworkItem.getDevice();
        if (iDevice instanceof Kiosk30) {
            Kiosk30 iKiosk = (Kiosk30) iDevice;
            mDetailKiosk30.loadDevice(iKiosk);
            setCurrentWidget(mDetailKiosk30);
        }
        else {
            setCurrentWidget(mDetailDummy);
        }
    }
    
    
    //
    // Auxiliary classes
    //
    
    private class Signal1Bubbler<A1> implements Signal1.Listener<A1> {
        Signal1<A1> mBubble;
        
        public Signal1Bubbler(Signal1<A1> iBubble) {
            mBubble = iBubble;
        }

        @Override
        public void trigger(A1 arg) {
            mBubble.trigger(arg);
        }
    }
    
    private class Signal2Bubbler<A1, A2> implements Signal2.Listener<A1, A2> {
        Signal2<A1, A2> mBubble;
        
        public Signal2Bubbler(Signal2<A1, A2> iBubble) {
            mBubble = iBubble;
        }

        @Override
        public void trigger(A1 arg1, A2 arg2) {
            mBubble.trigger(arg1, arg2);
        }
    }
}
