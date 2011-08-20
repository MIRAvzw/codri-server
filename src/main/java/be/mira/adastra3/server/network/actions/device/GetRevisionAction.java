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
import org.teleal.cling.model.types.UnsignedIntegerFourBytes;

/**
 *
 * @author tim
 */
public class GetRevisionAction extends ActionInvocation {
    public GetRevisionAction(final Service iService) throws NetworkException {
        super(iService.getAction("GetRevision"));
    }
    
    public final long getConfigurationRevision() throws NetworkException {    
        ActionArgumentValue tRevision = getOutput("oRevisionValue");
        if (tRevision == null) {
            throw new NetworkException("state variable not accessible");
        } else if (tRevision.getDatatype().getBuiltin() != Datatype.Builtin.UI4) {
            throw new NetworkException("invalid return type by Device.GetRevision (not an UI4)");
        }
        return (long) ((UnsignedIntegerFourBytes) tRevision.getValue()).getValue();
    }
}
