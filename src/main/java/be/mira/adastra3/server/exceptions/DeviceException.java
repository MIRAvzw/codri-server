/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.exceptions;

/**
 *
 * @author tim
 */
public class DeviceException extends Exception {

    public DeviceException(final Throwable iCause) {
        super(iCause);
    }

    public DeviceException(final String iMessage, final Throwable iCause) {
        super(iMessage, iCause);
    }

    public DeviceException(final String iMessage) {
        super(iMessage);
    }

    public DeviceException() {
    }
    
}
