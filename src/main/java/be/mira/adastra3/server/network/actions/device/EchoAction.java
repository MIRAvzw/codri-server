/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.device;

import be.mira.adastra3.server.exceptions.NetworkException;
import org.teleal.cling.model.action.ActionArgumentValue;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;

/**
 *
 * @author tim
 */
public class EchoAction extends ActionInvocation {
    public EchoAction(final Service iService, final String iEcho) throws NetworkException {
        super(iService.getAction("Echo"));
        setInput("iEchoValue", iEcho);
    }
    
    public final String getEcho() throws NetworkException {    
        ActionArgumentValue tEcho = getOutput("oEchoValue");
        if (tEcho == null) {
            throw new NetworkException("state variable not accessible");
        }
        return ((String) tEcho.getValue());
    }
}
