/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.exceptions;

/**
 *
 * @author tim
 */
public class NetworkException extends Exception {
    
    public NetworkException(final Throwable iCause) {
        super(iCause);
    }

    public NetworkException(final String iMessage, final Throwable iCause) {
        super(iMessage, iCause);
    }

    public NetworkException(final String iMessage) {
        super(iMessage);
    }

    public NetworkException() {
    }
}
