/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.mueumkiosk.server.mdns;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceTypeListener;

/**
 *
 * @author tim
 */
public class TypeListener implements ServiceTypeListener {

    @Override
    public void serviceTypeAdded(ServiceEvent event) {
        System.out.println("+ Service type toegevoegd: " + event.getType());
    }
    
    @Override
    public void subTypeForServiceTypeAdded(ServiceEvent event) {
        System.out.println("+ SubType voor service type toegevoegd: " + event.getType());
    }
}
