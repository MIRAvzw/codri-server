/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.website.status.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 *
 * @author tim
 */
public class MachineDetail extends Form {
	private Button save = new Button("Save");
	private Button cancel = new Button("Cancel");

	public MachineDetail() {
		addField("First Name", new TextField("First Name"));
		addField("Last Name", new TextField("Last Name"));
		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(true);
		footer.addComponent(save);
		footer.addComponent(cancel);
		setFooter(footer);
	}

}
