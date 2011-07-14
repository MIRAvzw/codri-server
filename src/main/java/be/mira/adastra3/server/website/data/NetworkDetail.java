/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data;

import be.mira.adastra3.server.network.devices.Device;
import be.mira.adastra3.server.network.devices.Kiosk30;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WText;
import eu.webtoolkit.jwt.WVBoxLayout;

/**
 *
 * @author tim
 */
public class NetworkDetail extends WContainerWidget {
    //
    // Data members
    //
    
    // Widgets
    WContainerWidget mDetailDummy;
    WContainerWidget mDetailKiosk30;
    
    //
    // Construction and destruction
    //
    
    public NetworkDetail(WContainerWidget parent) {
        super(parent);
        
    }
    
    
    //
    // UI creation
    //
    
    private void createUI() {
        // Layout
        WVBoxLayout tLayout = new WVBoxLayout();
        setLayout(tLayout);
        
        // Add detail widgets
        tLayout.addWidget(createDetailDummy());
        tLayout.addWidget(createDetailKiosk30());        
        showDetail(null);
        
    }
    
    public WContainerWidget createDetailDummy() {
        // Layout
        mDetailDummy = new WContainerWidget(this);
        
        mDetailDummy.addWidget(new WText("Please select a device"));
        
        return mDetailDummy;   
    }
    
    public WContainerWidget createDetailKiosk30() {
        // Layout
        mDetailKiosk30 = new WContainerWidget(this);
        
        mDetailKiosk30.addWidget(new WText("Kiosk30"));
        
        return mDetailKiosk30;
    }
    
    
    //
    // Basic I/O
    //
    
    public void hideAll() {
        mDetailDummy.hide();
        mDetailKiosk30.hide();
    }
    
    public void showDetail(NetworkItem iNetworkItem) {
        hideAll();
        if (iNetworkItem == null) {
            mDetailDummy.show();
            return;
        }
        
        Device iDevice = iNetworkItem.getDevice();
        if (iDevice instanceof Kiosk30) {
            Kiosk30 iKiosk = (Kiosk30) iDevice;
            
            mDetailKiosk30.show();
        }
        else {
            mDetailDummy.show();
            return;
        }
    }
}
