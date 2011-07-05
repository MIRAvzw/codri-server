/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.application;

import be.mira.adastra3.server.exceptions.NetworkException;
import org.teleal.cling.model.action.ActionArgumentValue;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.Datatype;
import org.teleal.cling.model.types.InvalidValueException;

/**
 *
 * @author tim
 */
public class LoadInterface extends ActionInvocation {
    public LoadInterface(Service service) throws NetworkException {
        super(service.getAction("GetInterfaceRevision"));
        try {
            /* No parameters required */
        }
        catch (InvalidValueException ex) {
            throw new NetworkException("Could not invoke Kiosk.GetInterfaceRevision", ex);
        }
    }
    
    public Integer GetInterfaceRevisionValue() throws NetworkException {        
        ActionArgumentValue oVolume = getOutput("oInterfaceRevision");
        if (oVolume.getDatatype().getBuiltin() != Datatype.Builtin.UI4)
            throw new NetworkException("Invalid return type by Kiosk.GetInterfaceRevision: not an UI4");
        return (Integer) oVolume.getValue();
    }
}