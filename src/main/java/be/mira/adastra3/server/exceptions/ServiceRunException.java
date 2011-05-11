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
    public ServiceRunException() {
        super();
    }
    
    public ServiceRunException(String iMessage) {
        super(iMessage);
    }

    public ServiceRunException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ServiceRunException(Exception e) {
        super(e);
    }
}
