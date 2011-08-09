/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data;

import be.mira.adastra3.server.network.devices.Device;
import be.mira.adastra3.server.network.devices.Kiosk30;
import be.mira.adastra3.server.website.auxiliary.Signal1Bubbler;
import be.mira.adastra3.server.website.auxiliary.Signal2Bubbler;
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
    private WContainerWidget mDetailDummy;
    private Kiosk30Detail mDetailKiosk30;
    
    // Events
    private Signal2<String, Exception> mEventError;
    private Signal1<String> mEventWarning, mEventInfo, mEventDebug;
    
    //
    // Construction and destruction
    //
    
    public NetworkDetail(final WContainerWidget iParent) {
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
    
    private WContainerWidget createDetailDummy(final WContainerWidget iParent) {
        // Layout
        WContainerWidget tWidget = new WContainerWidget(iParent);
        
        tWidget.addWidget(new WText("Please select a device"));
        
        return tWidget;   
    }
    
    
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
    
    public final void showDetail(final NetworkItem iNetworkItem) {
        if (iNetworkItem == null) {
            setCurrentWidget(mDetailDummy);
            return;
        }
        
        Device tDevice = iNetworkItem.getDevice();
        if (tDevice instanceof Kiosk30) {
            Kiosk30 tKiosk = (Kiosk30) tDevice;
            mDetailKiosk30.loadDevice(tKiosk);
            setCurrentWidget(mDetailKiosk30);
        } else {
            setCurrentWidget(mDetailDummy);
        }
    }
}
