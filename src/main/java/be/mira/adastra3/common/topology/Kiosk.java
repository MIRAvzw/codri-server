/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.common.topology;

/**
 *
 * @author tim
 */
public class Kiosk extends Machine {

    public static final long serialVersionUID = 42L;

    public Kiosk(Kiosk old) {
        super(old);
    }

    public Kiosk(String iName) {
        super(iName);
    }

}
