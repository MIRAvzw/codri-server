/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.media;

import be.mira.adastra3.server.exceptions.NetworkException;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.InvalidValueException;

/**
 *
 * @author tim
 */
public class SetVolume extends ActionInvocation {
    public SetVolume(Service service, Integer iVolume) throws NetworkException {
        super(service.getAction("SetVolume"));
        
        if (iVolume < 0)
            throw new NetworkException("Cannot invoke Media.SetVolume: iVolumeValue should be an UI4");
        
        try {
            setInput("iVolumeValue", iVolume);
        }
        catch (InvalidValueException ex) {
            throw new NetworkException("Could not invoke Media.SetVolume", ex);
        }
    }
}