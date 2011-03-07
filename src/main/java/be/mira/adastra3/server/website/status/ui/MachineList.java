/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.status.ui;

import be.mira.adastra3.server.website.status.StatusApplication;
import be.mira.adastra3.server.website.status.data.MachineContainer;
import com.vaadin.ui.Table;

/**
 *
 * @author tim
 */
public class MachineList extends Table {

    public MachineList(StatusApplication app) {
        setSizeFull();
        setContainerDataSource(app.getDataSource());

        setVisibleColumns(MachineContainer.NATURAL_COL_ORDER);
        setColumnHeaders(MachineContainer.COL_HEADERS_ENGLISH);
        setColumnCollapsingAllowed(true);
        setColumnReorderingAllowed(false);

        /*
         * Make table selectable, react immediatedly to user events, and pass
         * events to the controller (our main application)
         */
        setSelectable(true);
        setImmediate(true);
        addListener((ValueChangeListener) app);
        /* We don't want to allow users to de-select a row */
        setNullSelectionAllowed(false);
    }
}
