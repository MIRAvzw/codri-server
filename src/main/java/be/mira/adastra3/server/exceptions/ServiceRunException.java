/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.exceptions;

/**
 *
 * @author tim
 */
public class ServiceRunException extends Exception {
    public ServiceRunException(final Throwable iCause) {
        super(iCause);
    }
    
    public ServiceRunException(final String iMessage) {
        super(iMessage);
    }

    public ServiceRunException(final String iMessage, final Throwable iCause) {
        super(iMessage, iCause);
    }
}
