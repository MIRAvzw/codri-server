/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.application;

import be.mira.adastra3.server.exceptions.NetworkException;
import org.teleal.cling.model.action.ActionArgumentValue;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;

/**
 *
 * @author tim
 */
public class GetMediaAction extends ActionInvocation {
    public GetMediaAction(final Service iService) throws NetworkException {
        super(iService.getAction("GetMedia"));
    }
    
    public final String getIdentifier() throws NetworkException {    
        ActionArgumentValue tMediaIdentifier = getOutput("oMediaIdentifierValue");
        if (tMediaIdentifier == null) {
            throw new NetworkException("state variable not accessible");
        }
        return ((String) tMediaIdentifier.getValue());
    }
    
    public final String getLocation() throws NetworkException {    
        ActionArgumentValue tMediaIdentifier = getOutput("oMediaLocationValue");
        if (tMediaIdentifier == null) {
            throw new NetworkException("state variable not accessible");
        }
        return ((String) tMediaIdentifier.getValue());
    }
}
