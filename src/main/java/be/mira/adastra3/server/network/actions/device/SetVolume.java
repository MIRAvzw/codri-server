/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.device;

import be.mira.adastra3.server.exceptions.NetworkException;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.InvalidValueException;
import org.teleal.cling.model.types.UnsignedIntegerOneByte;

/**
 *
 * @author tim
 */
public class SetVolume extends ActionInvocation {
    public SetVolume(final Service iService, final Integer iVolume) throws NetworkException {
        super(iService.getAction("SetVolume"));
        
        try {
            setInput("iVolumeValue", new UnsignedIntegerOneByte(iVolume));
        } catch (InvalidValueException tException) {
            throw new NetworkException("could not invoke Device.SetVolume", tException);
        }
    }
}
