/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.common.topology;

/**
 *
 * @author tim
 */
public class Server extends Machine {

    public static final long serialVersionUID = 42L;

    public Server(Server old) {
        super(old);
    }

    public Server(String iName) {
        super(iName);
    }

}
