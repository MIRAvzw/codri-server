package be.mira.adastra3.server.website.status;

import be.mira.adastra3.server.website.data.DeferredExecution;
import be.mira.adastra3.server.website.data.NetworkModel;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WTimer;
import eu.webtoolkit.jwt.WTreeView;

/*
 * A simple hello world application class which demonstrates how to react
 * to events, read input, and give feed-back.
 */
public class StatusApplication extends WApplication {
    //
    // Data members
    //
    
    NetworkModel mNetworkModel;
    WTimer mTimer;
    
    
    //
    // Construction and destruction
    //
    
    public StatusApplication(WEnvironment iEnvironment) {
        super(iEnvironment);
        
        setTitle("Status page");
        
        // Prepare model
        mNetworkModel = new NetworkModel();
        mNetworkModel.attach();
        WTreeView treeview = new WTreeView(getRoot());
        treeview.setModel(mNetworkModel);
        
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
}
