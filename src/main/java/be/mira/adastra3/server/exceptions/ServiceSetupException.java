/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.exceptions;

/**
 *
 * @author tim
 */
public class ServiceSetupException extends Exception {
    public ServiceSetupException(final Throwable iCause) {
        super(iCause);
    }
    
    public ServiceSetupException(final String iMessage) {
        super(iMessage);
    }

    public ServiceSetupException(final String iMessage, final Throwable iCause) {
        super(iMessage, iCause);
    }
}
