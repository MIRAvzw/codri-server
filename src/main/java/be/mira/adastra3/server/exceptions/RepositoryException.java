/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.exceptions;

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
