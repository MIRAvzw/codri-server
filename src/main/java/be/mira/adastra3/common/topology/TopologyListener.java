/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.common.topology;

/**
 *
 * @author tim
 */
public interface TopologyListener {
    void kioskAddedAction(Kiosk iKiosk);
    void kioskUpdatedAction(Kiosk iKioskOld, Kiosk iKioskNew);
    void serverAddedAction(Server iServer);
    void serverUpdatedAction(Server iServerOld, Server iServerNew);
}
