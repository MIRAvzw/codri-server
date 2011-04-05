/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.common.repository;

/**
 *
 * @author tim
 */
public class RepositoryException extends Exception {

    public RepositoryException(Throwable cause) {
        super(cause);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException() {
    }

}
