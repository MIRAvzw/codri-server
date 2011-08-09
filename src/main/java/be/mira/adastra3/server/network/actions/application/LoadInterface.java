/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network.actions.application;

import be.mira.adastra3.server.exceptions.NetworkException;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.InvalidValueException;

/**
 *
 * @author tim
 */
public class LoadInterface extends ActionInvocation {
    public LoadInterface(final Service iService, final String iInterfaceIdentifier, final String iInterfaceRole, final String iInterfaceLocation) throws NetworkException {
        super(iService.getAction("LoadInterface"));
        try {
            setInput("iInterfaceIdentifierValue", iInterfaceIdentifier);
            setInput("iInterfaceRoleValue", iInterfaceRole);
            setInput("iInterfaceLocationValue", iInterfaceLocation);
        } catch (InvalidValueException tException) {
            throw new NetworkException("could not invoke Application.LoadInterface", tException);
        }
    }
}
