/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.application;

import be.mira.adastra3.server.exceptions.NetworkException;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.InvalidValueException;
import org.teleal.cling.model.types.UnsignedIntegerFourBytes;

/**
 *
 * @author tim
 */
public class SetConfigurationRevisionAction extends ActionInvocation {
    public SetConfigurationRevisionAction(final Service iService, final long iConfigurationRevision) throws NetworkException {
        super(iService.getAction("SetConfigurationRevision"));
        
        try {
            setInput("iConfigurationRevisionValue", new UnsignedIntegerFourBytes(iConfigurationRevision));
        } catch (InvalidValueException tException) {
            throw new NetworkException("could not invoke Device.SetConfigurationRevisionAction", tException);
        }
    }
}
