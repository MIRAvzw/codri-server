/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.device;

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
public class GetVolume extends ActionInvocation {
    public GetVolume(Service service) throws NetworkException {
        super(service.getAction("GetVolume"));
        try {
            /* No parameters required */
        }
        catch (InvalidValueException ex) {
            throw new NetworkException("Could not invoke Media.GetVolume", ex);
        }
    }
    
    public Integer GetVolumeValue() throws NetworkException {        
        ActionArgumentValue oVolume = getOutput("oVolumeValue");
        if (oVolume.getDatatype().getBuiltin() != Datatype.Builtin.UI4)
            throw new NetworkException("Invalid return type by Media.GetVolume: not an UI4");
        return (Integer) oVolume.getValue();
    }
}