/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.device;

import be.mira.adastra3.server.exceptions.NetworkException;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;

/**
 *
 * @author tim
 */
public class Shutdown extends ActionInvocation {
    public Shutdown(final Service iService) throws NetworkException {
        super(iService.getAction("Shutdown"));
    }
}
