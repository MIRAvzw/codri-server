/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.status;

import be.mira.adastra3.server.website.status.data.MachineContainer;
import be.mira.adastra3.server.website.status.ui.HelpWindow;
import be.mira.adastra3.server.website.status.ui.MachineDetail;
import be.mira.adastra3.server.website.status.ui.MachineList;
import be.mira.adastra3.server.website.status.ui.MachineView;
import be.mira.adastra3.server.website.status.ui.NavigationTree;
import com.vaadin.Application;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author tim
 */
public class StatusApplication extends Application implements ItemClickListener, Button.ClickListener {
    //
    // Member data
    //

    private NavigationTree tree = new NavigationTree(this);
    private SplitPanel horizontalSplit = new SplitPanel(SplitPanel.ORIENTATION_HORIZONTAL);
    private Button mButtonRefresh = new Button("Refresh");
    private Button mButtonHelp = new Button("Help");

    // Lazyly created ui references
    private MachineView mMachineView = null;
    private MachineList mMachineList = null;
    private MachineDetail mMachineDetail = null;
    private HelpWindow mHelpWindow = null;
    private MachineContainer mDataSource;


    //
    // UI Initialisation
    //

    @Override
    public void init() {
        getDataSource();
        buildMainLayout();
        setMainComponent(getListView());
    }

    private void buildMainLayout() {
        setMainWindow(new Window("MIRA Ad-Astra III status application"));

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        layout.addComponent(createToolbar());
        layout.addComponent(horizontalSplit);
        layout.setExpandRatio(horizontalSplit, 1);

        horizontalSplit.setSplitPosition(200, SplitPanel.UNITS_PIXELS);
        horizontalSplit.setFirstComponent(tree);

        getMainWindow().setContent(layout);
    }

    private HorizontalLayout createToolbar() {
        HorizontalLayout lo = new HorizontalLayout();
        lo.addComponent(mButtonRefresh);
        lo.addComponent(mButtonHelp);

        mButtonRefresh.addListener((ClickListener) this);
        mButtonHelp.addListener((ClickListener) this);
        
        lo.setStyleName("toolbar");

        return lo;
    }

    //
    // Event handlers
    //

    public void buttonClick(Button.ClickEvent event) {
        final Object source = event.getSource();

        if (source instanceof Button) {
            if (source == mButtonHelp) {
                showHelpWindow();
            } else if (source == mButtonRefresh) {
                refreshDataSource();
                // Todo: refresh list view etc
            }
        }
    }

    public void itemClick(ItemClickEvent event) {
        if (event.getSource() == tree) {
            Object itemId = event.getItemId();
            if (itemId != null) {
                if (NavigationTree.SHOW_ALL.equals(itemId)) {
                    getDataSource().removeAllContainerFilters();
                } else if (NavigationTree.SERVERS.equals(itemId)) {
                    getDataSource().removeAllContainerFilters();
                    getDataSource().addContainerFilter("type", "Server", false, false);
                    // Eventueel ook subfilters (offline, online)
                } else if (NavigationTree.KIOSKS.equals(itemId)) {
                    getDataSource().removeAllContainerFilters();
                    getDataSource().addContainerFilter("type", "Kiosk", false, false);
                    // Eventueel ook subfilters (offline, online)
                }
                showListView();
            }
        }
    }


    //
    // Data handling
    //

    public MachineContainer getDataSource() {
        if (mDataSource == null)
            mDataSource = MachineContainer.createFromTopology();
        return mDataSource;
    }

    public void refreshDataSource() {
        getDataSource().updateFromTopology();
    }


    //
    // Auxiliary
    //

    private void setMainComponent(Component c) {
        horizontalSplit.setSecondComponent(c);
    }

    /*
     * View getters exist so we can lazily generate the views, resulting in
     * faster application startup time.
     */
    private MachineView getListView() {
        if (mMachineView == null) {
            mMachineList = new MachineList(this);
            mMachineDetail = new MachineDetail();
            mMachineView = new MachineView(mMachineList, mMachineDetail);
        }
        return mMachineView;
    }

    private HelpWindow getHelpWindow() {
        if (mHelpWindow == null) {
            mHelpWindow = new HelpWindow();
        }
        return mHelpWindow;
    }

    private void showHelpWindow() {
        getMainWindow().addWindow(getHelpWindow());
    }

    private void showListView() {
        setMainComponent(getListView());
    }
}
