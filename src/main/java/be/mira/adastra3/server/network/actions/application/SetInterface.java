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
public class SetInterface extends ActionInvocation {
    public SetInterface(Service service, String iInterface) throws NetworkException {
        super(service.getAction("SetInterface"));
        try {
            setInput("iInterfaceValue", iInterface);
        }
        catch (InvalidValueException ex) {
            throw new NetworkException("Could not invoke Kiosk.SetInterface", ex);
        }
    }
}