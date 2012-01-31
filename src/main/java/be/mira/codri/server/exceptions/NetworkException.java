/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.exceptions;

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
