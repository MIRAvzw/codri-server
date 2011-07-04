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

    public DeviceException(Throwable cause) {
        super(cause);
    }

    public DeviceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeviceException(String message) {
        super(message);
    }

    public DeviceException() {
    }
    
}
