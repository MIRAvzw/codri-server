/**
 * Copyright (C) 2011-2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */
package be.mira.codri.server.exceptions;

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
