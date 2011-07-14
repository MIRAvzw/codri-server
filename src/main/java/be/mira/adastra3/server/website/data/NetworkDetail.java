/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data;

import be.mira.adastra3.server.network.devices.Device;
import be.mira.adastra3.server.network.devices.Kiosk30;
import be.mira.adastra3.server.website.data.details.Kiosk30Detail;
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
    
    //
    // Construction and destruction
    //
    
    public NetworkDetail(WContainerWidget parent) {
        super(parent);
        
        createUI();        
    }
    
    
    //
    // UI creation
    //
    
    private void createUI() {
        // Add detail widgets
        addWidget(createDetailDummy()); // Redundant?
        mDetailKiosk30 = new Kiosk30Detail(this);
        addWidget(mDetailKiosk30); // Redundant?
        showDetail(null);
        
    }
    
    public WContainerWidget createDetailDummy() {
        // Layout
        mDetailDummy = new WContainerWidget(this);
        
        mDetailDummy.addWidget(new WText("Please select a device"));
        
        return mDetailDummy;   
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
}
