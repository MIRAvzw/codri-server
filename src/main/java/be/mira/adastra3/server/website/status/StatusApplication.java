package be.mira.adastra3.server.website.status;

import be.mira.adastra3.server.website.data.DeferredExecution;
import be.mira.adastra3.server.website.data.NetworkItem;
import be.mira.adastra3.server.website.data.NetworkModel;
import be.mira.adastra3.server.website.data.TreeItem;
import eu.webtoolkit.jwt.SelectionBehavior;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WModelIndex;
import eu.webtoolkit.jwt.WTabWidget;
import eu.webtoolkit.jwt.WTimer;
import eu.webtoolkit.jwt.WTreeView;
import eu.webtoolkit.jwt.WVBoxLayout;
import java.util.SortedSet;

/*
 * A simple hello world application class which demonstrates how to react
 * to events, read input, and give feed-back.
 */
public class StatusApplication extends WApplication {
    //
    // Data members
    //
    
    WTimer mTimer;
    
    NetworkModel mNetworkModel;
    WTreeView mNetworkView;
    
    
    //
    // Construction and destruction
    //
    
    public StatusApplication(WEnvironment iEnvironment) {
        super(iEnvironment);
        
        setTitle("Status page");
        
        // Tabs
        WTabWidget tTabs = new WTabWidget(getRoot());
        
        // Network
        WContainerWidget tNetwork = new WContainerWidget();
        tTabs.addTab(tNetwork, "Network"); 
        
        // Network view
        mNetworkModel = new NetworkModel();
        mNetworkModel.attach();
        mNetworkView = new WTreeView();
        mNetworkView.setModel(mNetworkModel);
        mNetworkView.selectionChanged().addListener(this, new SelectionChanged());
        mNetworkView.setSelectable(true);
        mNetworkView.setSelectionBehavior(SelectionBehavior.SelectRows);
        mNetworkView.expandToDepth(2);
        tNetwork.addWidget(mNetworkView);
        
        // Repository
        WContainerWidget tRepository = new WContainerWidget();
        tTabs.addTab(tRepository, "Repository"); 
        
        // Repository view
        tRepository.addWidget(new WLabel("WIP"));
        
        // Schedule deferred executions
        mTimer = new WTimer(getRoot());
        mTimer.setInterval(1000);
        mTimer.timeout().addListener(this, new Signal.Listener() {
            @Override
            public void trigger() {
                for (DeferredExecution tDeferree : DeferredExecution.DEFERREES) {
                    tDeferree.execute();
                }
                DeferredExecution.DEFERREES.clear();
            }            
        });
        mTimer.start();
    }
    
    @Override
    protected void finalize() throws Throwable {
        mNetworkModel.detach();
    }
    
    
    //
    // UI events
    //
    
    private class SelectionChanged implements Signal.Listener {
        @Override
        public void trigger() {
            SortedSet<WModelIndex> tSelected = mNetworkView.getSelectedIndexes();
            if (tSelected.size() == 1) {
                WModelIndex tSelection = tSelected.first();
                TreeItem tItem = mNetworkModel.getItem(tSelection);
                if (tItem instanceof NetworkItem) {
                    // TODO
                }
            }
        }        
    }
}
