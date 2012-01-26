/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
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
