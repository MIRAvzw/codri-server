/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.exceptions;

/**
 *
 * @author tim
 */
public class InvalidStateException extends RuntimeException {
    public InvalidStateException(final String iMessage) {
        super(iMessage);
    }    
}