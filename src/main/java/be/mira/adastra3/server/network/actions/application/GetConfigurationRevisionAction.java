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
import org.teleal.cling.model.types.UnsignedIntegerFourBytes;

/**
 *
 * @author tim
 */
public class GetConfigurationRevisionAction extends ActionInvocation {
    public GetConfigurationRevisionAction(final Service iService) throws NetworkException {
        super(iService.getAction("GetConfigurationRevision"));
    }
    
    public final long getConfigurationRevision() throws NetworkException {    
        ActionArgumentValue tConfigurationRevision = getOutput("oConfigurationRevisionValue");
        if (tConfigurationRevision == null) {
            throw new NetworkException("state variable not accessible");
        } else if (tConfigurationRevision.getDatatype().getBuiltin() != Datatype.Builtin.UI4) {
            throw new NetworkException("invalid return type by Media.GetConfigurationRevision (not an UI4)");
        }
        return (long) ((UnsignedIntegerFourBytes) tConfigurationRevision.getValue()).getValue();
    }
}
