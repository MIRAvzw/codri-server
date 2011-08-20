/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.media;

import be.mira.adastra3.server.exceptions.NetworkException;
import org.teleal.cling.model.action.ActionArgumentValue;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;

/**
 *
 * @author tim
 */
public class GetLocationAction extends ActionInvocation {
    public GetLocationAction(final Service iService) throws NetworkException {
        super(iService.getAction("GetLocationAction"));
    }
    
    public final String getLocation() throws NetworkException {    
        ActionArgumentValue tLocation = getOutput("oLocationValue");
        if (tLocation == null) {
            throw new NetworkException("state variable not accessible");
        }
        return ((String) tLocation.getValue());
    }
}
