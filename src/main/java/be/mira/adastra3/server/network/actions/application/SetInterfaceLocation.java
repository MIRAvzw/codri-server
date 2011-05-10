/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.application;

import be.mira.adastra3.server.exceptions.NetworkException;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.InvalidValueException;

/**
 *
 * @author tim
 */
public class SetInterfaceLocation extends ActionInvocation {
    public SetInterfaceLocation(Service service, String iInterfaceLocation) throws NetworkException {
        super(service.getAction("SetInterfaceLocation"));
        try {
            setInput("iInterfaceLocationValue", iInterfaceLocation);
        }
        catch (InvalidValueException ex) {
            throw new NetworkException("Could not invoke Kiosk.SetInterfaceLocation", ex);
        }
    }
}