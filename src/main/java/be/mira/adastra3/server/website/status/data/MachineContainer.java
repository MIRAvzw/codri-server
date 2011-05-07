/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.status.data;

import be.mira.adastra3.server.network.Kiosk;
import com.vaadin.data.util.BeanItemContainer;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author tim
 */
public class MachineContainer extends BeanItemContainer<Kiosk> implements Serializable {

    public static final long serialVersionUID = 42L;

    public MachineContainer() throws InstantiationException, IllegalAccessException {
        super(Kiosk.class);
    }
    /**
     * Natural property order for Person bean. Used in tables and forms.
     */
    public static final Object[] NATURAL_COL_ORDER = new Object[]{
        "name", "state"};
    /**
     * "Human readable" captions for properties in same order as in
     * NATURAL_COL_ORDER.
     */
    public static final String[] COL_HEADERS_ENGLISH = new String[]{
        "Name", "State"};

    public static MachineContainer createFromTopology() {
        MachineContainer oContainer = null;
        try {
            oContainer = new MachineContainer();
        } catch (InstantiationException e) {
            e.printStackTrace(); // TODO
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // TODO
        }
        oContainer.updateFromTopology();
        return oContainer;
    }

    public void updateFromTopology() {
        removeAllItems();
        //addAll(Topology.getInstance().getServers());
        //addAll(Topology.getInstance().getKiosks());
        Collection<String> tProperties = this.getContainerPropertyIds();
    }
}
