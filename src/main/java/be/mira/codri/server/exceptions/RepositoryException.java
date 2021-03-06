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
public class RepositoryException extends Exception {

    public RepositoryException(final Throwable iCause) {
        super(iCause);
    }

    public RepositoryException(final String iMessage, final Throwable iCause) {
        super(iMessage, iCause);
    }

    public RepositoryException(final String iMessage) {
        super(iMessage);
    }

    public RepositoryException() {
    }

}
