/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.device;

import be.mira.adastra3.server.exceptions.NetworkException;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.InvalidValueException;

/**
 *
 * @author tim
 */
public class Reboot extends ActionInvocation {
    public Reboot(Service service) throws NetworkException {
        super(service.getAction("Reboot"));
        try {
            /* No parameters required */
        }
        catch (InvalidValueException ex) {
            throw new NetworkException("Could not invoke Kiosk.Reboot", ex);
        }
    }
}