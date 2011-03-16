/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.common.repository;

/**
 *
 * @author tim
 */
public class TopologyException extends Exception {

    public TopologyException(Throwable cause) {
        super(cause);
    }

    public TopologyException(String message) {
        super(message);
    }

    public TopologyException() {
    }

}
