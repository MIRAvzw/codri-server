package be.mira.adastra3.server;

import be.mira.adastra3.server.discovery.ServiceDiscovery;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.website.EmbeddedTomcat;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import org.apache.log4j.Logger;
import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * Hello world!
 *
 */
public class Main implements SignalHandler {
    //
    // Member data
    //

    private SignalHandler oldHandler;

    //
    // Routines
    //

    public void handle(Signal signal) {
        mLogger.info("Received signal " + signal.getName());
        
        try {
            signalAction(signal);

            // Chain back to previous handler, if one exists
            if (oldHandler != SIG_DFL && oldHandler != SIG_IGN) {
                oldHandler.handle(signal);
            }

        } catch (Exception e) {
            mLogger.error("Signal handler for signal " + signal.getName() + " failed", e);
        }
    }

    public void signalAction(Signal signal) {
        mLogger.info("Handling signal " + signal.getName());

        if (mTomcat != null) {
            try {
                mTomcat.stop();
                mTomcat = null;
            } catch (ServiceRunException e) {
                mLogger.error("Could not stop embedded Tomcat subsystem", e);
            }
        }

        if (mServiceDiscoverer != null) {
            try {
                mServiceDiscoverer.stop();
                mServiceDiscoverer = null;
            } catch (ServiceRunException e) {
                mLogger.error("Could not stop service discovery subsystem", e);
            }
        }

        System.exit(0);
    }


    //
    // Static
    //
    
    private static EmbeddedTomcat mTomcat;
    private static ServiceDiscovery mServiceDiscoverer;
    private static Logger mLogger;

    public static SignalHandler install(String signalName) {
        mLogger.debug("Installing signal handler for SIG " + signalName);
        
        Signal diagSignal = new Signal(signalName);
        Main instance = new Main();
        instance.oldHandler = Signal.handle(diagSignal, instance);
        return instance;
    }

    public static void main(String[] args) throws ServletException, LifecycleException {
        //
        // Set-up
        //

        // Logging
        mLogger = Logger.getLogger(Main.class);
        mLogger.info("Initializing application");

        // Install handlers
        Main.install("TERM");
        Main.install("INT");
        Main.install("ABRT");

        // Configure Tomcat
        try {
            mTomcat = new EmbeddedTomcat();
            mTomcat.addWebapp("status");
        } catch (ServiceSetupException e) {
            mLogger.error("Could not configure embedded Tomcat subsystem", e);
            return;
        }

        // Configure service discoverer
        try {
            mServiceDiscoverer = new ServiceDiscovery();
        } catch (ServiceSetupException e) {
            mLogger.error("Could not configure service discovery subsystem", e);
            return;
        }


        //
        // Start
        //

        mLogger.info("Starting subservices");

        // Run Tomcat
        try {
            mTomcat.run();
        } catch (ServiceRunException e) {
            mLogger.error("Could not start embedded Tomcat subsystem", e);
            return;
        }

        // Run service discoverer
        try {
            mServiceDiscoverer.run();
        } catch (ServiceRunException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }


        //
        // Sleep
        //

        mLogger.info("Entering main loop");

        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            mLogger.error("Could not start service discovery subsystem", e);
            }
        }

    }
}
