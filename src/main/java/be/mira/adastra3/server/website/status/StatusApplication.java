package be.mira.adastra3.server.website.status;

import be.mira.adastra3.server.website.data.DeferredExecution;
import be.mira.adastra3.server.website.data.NetworkDetail;
import be.mira.adastra3.server.website.data.NetworkItem;
import be.mira.adastra3.server.website.data.NetworkModel;
import be.mira.adastra3.server.website.data.TreeItem;
import eu.webtoolkit.jwt.SelectionBehavior;
import eu.webtoolkit.jwt.SelectionMode;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.Signal2;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WGridLayout;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WModelIndex;
import eu.webtoolkit.jwt.WTabWidget;
import eu.webtoolkit.jwt.WTextArea;
import eu.webtoolkit.jwt.WTimer;
import eu.webtoolkit.jwt.WTreeView;
import java.util.SortedSet;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

/*
 * A simple hello world application class which demonstrates how to react
 * to events, read input, and give feed-back.
 */
public class StatusApplication extends WApplication {
    //
    // Data members
    //
    
    WTimer mTimer;
    
    WTextArea mStatusbar;
    WTabWidget mTabs;
    
    Logger mLogger;
    
    
    NetworkModel mNetworkModel;
    WTreeView mNetworkView;
    NetworkDetail mNetworkDetail;
    LogAppender mLogAppender;
    
    
    //
    // Construction and destruction
    //
    
    public StatusApplication(WEnvironment iEnvironment) {
        super(iEnvironment);
        mLogger = Logger.getLogger(this.getClass());
        
        setCssTheme("polished");        
        setTitle("Status page");        
        createUI();
        
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
        
        info("webapplication initialised");
    }
    
    @Override
    protected void unload() {
        quit();
        
        mNetworkModel.detach();
        
        Logger tLogger = Logger.getRootLogger();
        tLogger.removeAppender(mLogAppender);
    }
    
    
    //
    // UI creation
    //
    
    private void createUI() {        
        // Tabs
        mTabs = new WTabWidget();
        mTabs.addTab(createNetwork(), "Network");
        mTabs.addTab(createRepository(), "Repository");
        mTabs.addTab(createLog(), "Log");
        
        // Status bar
        mStatusbar = new WTextArea();
        mStatusbar.setReadOnly(true);
        mStatusbar.setSelectable(true);
        
        // Layout
        WContainerWidget tMain = getRoot();
        WGridLayout tLayout = new WGridLayout();
        tLayout.addWidget(mTabs, 0, 0);
        tLayout.addWidget(mStatusbar, 1, 0);
        tLayout.setRowStretch(0, 1);
        tLayout.setColumnStretch(0, 1);
        tMain.setLayout(tLayout);
        
    }
    
    private WContainerWidget createNetwork() {
        // Layout
        WContainerWidget tNetwork = new WContainerWidget();
        WGridLayout tNetworkLayout = new WGridLayout();
        tNetworkLayout.setColumnResizable(0);
        tNetworkLayout.setColumnStretch(1, 1);
        tNetwork.setLayout(tNetworkLayout);
        
        // Network view
        mNetworkModel = new NetworkModel();
        mNetworkModel.attach();
        mNetworkView = new WTreeView(tNetwork);
        mNetworkView.setModel(mNetworkModel);
        mNetworkView.selectionChanged().addListener(this, onSelectionChanged);
        mNetworkView.setSelectionMode(SelectionMode.SingleSelection);
        mNetworkView.setSelectionBehavior(SelectionBehavior.SelectRows);
        mNetworkView.expandToDepth(2);
        mNetworkView.resize(new WLength(400), WLength.Auto);
        tNetworkLayout.addWidget(mNetworkView, 0, 0);
        
        // Network detail
        mNetworkDetail = new NetworkDetail(tNetwork);
        mNetworkDetail.error().addListener(this, onError);
        mNetworkDetail.warning().addListener(this, onWarning);
        mNetworkDetail.info().addListener(this, onInfo);
        tNetworkLayout.addWidget(mNetworkDetail, 0, 1);
        
        return tNetwork;
    }
    
    private WContainerWidget createRepository() {
        // Layout
        WContainerWidget tRepository = new WContainerWidget();
        
        // Repository view
        tRepository.addWidget(new WLabel("WIP"));
        
        return tRepository;
    }
    
    private WContainerWidget createLog() {
        // Layout
        WContainerWidget tLog = new WContainerWidget();
        WGridLayout tLayout = new WGridLayout(tLog);
        
        // Text area
        WTextArea tLogText = new WTextArea();
        tLogText.setReadOnly(true);
        tLogText.setSelectable(true);
        tLayout.addWidget(tLogText);
        
        // Setup logging
        Logger tLogger = Logger.getRootLogger();
        mLogAppender = new LogAppender(tLogText);
        mLogAppender.setLayout(new SimpleLayout());
        mLogAppender.setThreshold(Level.DEBUG);
        tLogger.addAppender(mLogAppender);
        
        return tLog;
    }
    
    
    //
    // Event handlers
    //
    
    private Signal.Listener onSelectionChanged = new Signal.Listener() {
        @Override
        public void trigger() {
            // Get the network item
            SortedSet<WModelIndex> tSelected = mNetworkView.getSelectedIndexes();
            NetworkItem tNetworkItem = null;
            if (tSelected.size() == 1) {
                TreeItem tItem = mNetworkModel.getItem(tSelected.first());
                if (tItem instanceof NetworkItem)
                    tNetworkItem = (NetworkItem) tItem;
            }
            mNetworkDetail.showDetail(tNetworkItem);
        }        
    };
    
    private Signal2.Listener<String, Exception> onError = new Signal2.Listener<String, Exception>() {
        @Override
        public void trigger(String iMessage, Exception iException) {
            error(iMessage, iException);
        }            
    };
    
    private Signal1.Listener<String> onWarning = new Signal1.Listener<String>() {
        @Override
        public void trigger(String iMessage) {
            warn(iMessage);
        }            
    };
    
    private Signal1.Listener<String> onInfo = new Signal1.Listener<String>() {
        @Override
        public void trigger(String iMessage) {
            info(iMessage);
        }            
    };
    
    
    //
    // Auxiliary functions
    //
    
    private void error(String iMessage, Exception iException) {
        mLogger.error(iMessage, iException);
        mStatusbar.setText(mStatusbar.getText() + "Error: " + iMessage + "\n");
    }
    
    private void warn(String iMessage) {
        mLogger.warn(iMessage);
        mStatusbar.setText(mStatusbar.getText() + "Warning: " + iMessage + "\n");        
    }
    
    private void info(String iMessage) {
        mLogger.info(iMessage);
        mStatusbar.setText(mStatusbar.getText() + "Notice: " + iMessage + "\n");        
    }
    
    
    //
    // Subclasses
    //
    
    private class LogAppender extends AppenderSkeleton {
        private WTextArea mLogText;
        
        public LogAppender(WTextArea iLogText) {
            super();
            mLogText = iLogText;
            mLogText.setText("");
        }
        
        @Override
        public void close() {
            mLogText.setText("");
        }

        @Override
        public boolean requiresLayout() {
            return true;
        }
        
        @Override
        protected void append(LoggingEvent event) {
            if (event != null) {
                DeferredExecution.DEFERREES.add(new DeferredExecution() {
                    String mMessage;
                    ThrowableInformation mThrowableInformation;
                    public DeferredExecution construct(String iMessage, ThrowableInformation iThrowableInformation) {
                        mMessage = iMessage;
                        mThrowableInformation = iThrowableInformation;
                        return this;
                    }
                    @Override
                    public void execute() {
                        String tCurrentMessage = mLogText.getText();
                        
                        tCurrentMessage += mMessage;
                        
                        if (mThrowableInformation != null) {
                            String[] tExceptionStrings = mThrowableInformation.getThrowableStrRep();
                            for (String tExceptionString : tExceptionStrings)
                                tCurrentMessage += tExceptionString + "\n";
                        }
                        
                        mLogText.setText(tCurrentMessage);
                    }
                }.construct(getLayout().format(event), event.getThrowableInformation()));
            }
        }
    }
}
