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
    public ServiceSetupException() {
        super();
    }
    
    public ServiceSetupException(String iMessage) {
        super(iMessage);
    }

    public ServiceSetupException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ServiceSetupException(Exception e) {
        super(e);
    }

}
