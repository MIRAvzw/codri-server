package be.mira.adastra3.server;

import be.mira.adastra3.server.discovery.ServiceDiscovery;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.repository.RepositoryMonitor;
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

        mLogger.info("Stopping subsystems");
        stop();

        mLogger.info("Exiting");
        System.exit(0);
    }


    //
    // Static
    //
    
    private static EmbeddedTomcat mTomcat;
    private static ServiceDiscovery mServiceDiscoverer;
    private static RepositoryMonitor mRepositoryMonitor;
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

        // Install handlers
        Main.install("TERM");
        Main.install("INT");
        Main.install("ABRT");

        // Subsystems
        mLogger.info("Initializing subsystems");
        if (!initialize()) {
            mLogger.error("Some subsystems failed to initialize, bailing out");
            return;
        }

        //
        // Start
        //

        // Subsystems
        mLogger.info("Starting subsystems");
        if (!start()) {
            mLogger.error("Some subsystems failed to start, bailing out");
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
                mLogger.warn("Main loop interrupted", e);
                break;
            }
        }

        mLogger.info("Stopping subsystems");
        stop();

        mLogger.info("Exiting");
        System.exit(0);
    }

    private static boolean initialize() {
        // Repository monitor
        mLogger.debug("Initializing repository monitor");
        try {
            mRepositoryMonitor = new RepositoryMonitor();
        } catch (ServiceSetupException e) {
            mLogger.error("Could not initialize repository monitor", e);
            return false;
        }

        // Embedded Tomcat
        mLogger.debug("Initializing embedded Tomcat");
        try {
            mTomcat = new EmbeddedTomcat();
            mTomcat.addWebapp("status");
        } catch (ServiceSetupException e) {
            mLogger.error("Could not initialize embedded Tomcat", e);
            return false;
        }

        // Service discoverer
        mLogger.debug("Initializing service discovery");
        try {
            mServiceDiscoverer = new ServiceDiscovery();
        } catch (ServiceSetupException e) {
            mLogger.error("Could not initialize service discovery", e);
            return false;
        }

        return true;
    }

    private static boolean start() {
        // Repository monitor
        mLogger.debug("Starting repository monitor");
        try {
            mRepositoryMonitor.run();
        } catch (ServiceRunException e) {
            mLogger.error("Could not start repository monitor", e);
            return false;
        }

        // Embedded Tomcat
        mLogger.debug("Starting embedded Tomcat");
        try {
            mTomcat.run();
        } catch (ServiceRunException e) {
            mLogger.error("Could not start embedded Tomcat", e);
            return false;
        }

        // Service discoverer
        mLogger.debug("Starting service discovery");
        try {
            mServiceDiscoverer.run();
        } catch (ServiceRunException e) {
            mLogger.error("Could not start service discovery", e);
            return false;
        }

        return true;
    }

    private static void stop() {
        if (mRepositoryMonitor != null) {
            mLogger.debug("Stopping repository monitor");
            try {
                mRepositoryMonitor.stop();
                mRepositoryMonitor = null;
            } catch (ServiceRunException e) {
                mLogger.error("Could not stop repository monitor", e);
            }
        }

        if (mTomcat != null) {
            mLogger.debug("Stopping embedded Tomcat");
            try {
                mTomcat.stop();
                mTomcat = null;
            } catch (ServiceRunException e) {
                mLogger.error("Could not stop embedded Tomcat", e);
            }
        }

        if (mServiceDiscoverer != null) {
            mLogger.debug("Stopping service discovery");
            try {
                mServiceDiscoverer.stop();
                mServiceDiscoverer = null;
            } catch (ServiceRunException e) {
                mLogger.error("Could not stop service discovery", e);
            }
        }
    }
}
