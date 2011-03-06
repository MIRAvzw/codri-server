/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.website.status.ui;

import com.vaadin.ui.SplitPanel;

/**
 *
 * @author tim
 */
public class MachineView extends SplitPanel {
	public MachineView(MachineList iMachineList, MachineDetail iMachineDetail) {
		addStyleName("view");
		setFirstComponent(iMachineList);
		setSecondComponent(iMachineDetail);
		setSplitPosition(40);
	}
}