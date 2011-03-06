/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.website.status.ui;

import be.mira.adastra3.server.website.status.StatusApplication;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Tree;

/**
 *
 * @author tim
 */
public class NavigationTree extends Tree {
	public static final Object SHOW_ALL = "Show all";
	public static final Object SERVERS = "Servers";
	public static final Object KIOSKS = "Kiosks";
	public static final Object SEARCH = "Search";

	public NavigationTree(StatusApplication app) {
		addItem(SHOW_ALL);
		addItem(SERVERS);
		addItem(KIOSKS);
		addItem(SEARCH);

		/*
		 * We want items to be selectable but do not want the user to be able to
		 * de-select an item.
		 */
		setSelectable(true);
		setNullSelectionAllowed(false);

		// Make application handle item click events
		addListener((ItemClickListener) app);

	}
}