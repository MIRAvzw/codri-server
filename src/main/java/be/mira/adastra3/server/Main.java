package be.mira.adastra3.server;

import be.mira.adastra3.server.network.NetworkMonitor;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.repository.RepositoryMonitor;
import be.mira.adastra3.server.website.EmbeddedTomcat;
import java.util.EnumMap;
import java.util.Map;
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
    // Auxiliary classes
    //

    private enum Status {
        IDLE,
        INITIALIZING,
        STARTING,
        RUNNING,
        STOPPING
    }

    private enum ServiceType {
        NETWORK,
        REPOSITORY,
        WEBSITE
    }

    //
    // Member data
    //

    private SignalHandler oldHandler;

    //
    // Routines
    //

    @Override
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

        if (mStatus == Status.RUNNING) {
            mLogger.info("Stopping subsystems");
            mStatus = Status.STOPPING;
            stop();

            mLogger.info("Exiting");
            mStatus = Status.IDLE;
            System.exit(0);
        }
        else if (mStatus != Status.STOPPING) {
            mLogger.info("Exiting");
            mStatus = Status.IDLE;
            System.exit(0);
        }
        else {
            mLogger.debug("Ignoring signal as the application is " + mStatus.name());
        }
    }


    //
    // Static
    //

    private static Map<ServiceType, Service> mSubservices;
    private final static Map<ServiceType, String> mServiceNames;
    static {
        mServiceNames = new EnumMap<ServiceType, String>(ServiceType.class);
        mServiceNames.put(ServiceType.NETWORK, "network monitor");
        mServiceNames.put(ServiceType.REPOSITORY, "repository monitor");
        mServiceNames.put(ServiceType.WEBSITE, "web server");
    }
    private static Logger mLogger;
    private static Status mStatus = Status.IDLE;

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
        mStatus = Status.INITIALIZING;
        if (!initialize()) {
            mLogger.error("Some subsystems failed to initialize, bailing out");

            mStatus = Status.IDLE;
            System.exit(0);
        }

        //
        // Start
        //

        // Subsystems
        mLogger.info("Starting subsystems");
        mStatus = Status.STARTING;
        if (!start()) {
            mLogger.error("Some subsystems failed to start, bailing out");

            mStatus = Status.STOPPING;
            stop();

            mStatus = Status.IDLE;
            System.exit(0);
        }


        //
        // Sleep
        //

        mLogger.info("Entering main loop");
        mStatus = Status.RUNNING;
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                mLogger.warn("Main loop interrupted", e);
                break;
            }
        }

        mLogger.info("Stopping subsystems");
        mStatus = Status.STOPPING;
        stop();

        mLogger.info("Exiting");
        mStatus = Status.IDLE;
        System.exit(0);
    }

    private static boolean initialize() {
        // Map with service objects
        mSubservices = new EnumMap<ServiceType, Service>(ServiceType.class);

        // Process all subservices
        for (ServiceType tServiceType : ServiceType.values()) {
            mLogger.debug("Initializing the " + mServiceNames.get(tServiceType));
            try {
                Service tService = null;
                switch (tServiceType) {
                    case REPOSITORY:
                        tService = new RepositoryMonitor();
                        break;
                    case WEBSITE:
                        tService = new EmbeddedTomcat();
                        ((EmbeddedTomcat)tService).addWebapp("status");
                        break;
                    case NETWORK:
                        tService = new NetworkMonitor();
                        break;
                    default:
                        throw new ServiceSetupException("I don't know how to initialize the " + mServiceNames.get(tServiceType));
                }
                mSubservices.put(tServiceType, tService);
            } catch (ServiceSetupException e) {
                mLogger.error("Could not initialize the " + mServiceNames.get(tServiceType), e);
                return false;
            }
        }

        return true;
    }

    private static boolean start() {
        for (ServiceType tServiceType : mSubservices.keySet()) {
            Service tService = mSubservices.get(tServiceType);
            mLogger.debug("Starting the " + mServiceNames.get(tServiceType));
            try {
                tService.run();
            } catch (ServiceRunException e) {
                mLogger.error("Could not start the " + mServiceNames.get(tServiceType), e);
                return false;
            }
        }

        return true;
    }

    private static void stop() {
        for (ServiceType tServiceType : mSubservices.keySet()) {
            Service tService = mSubservices.get(tServiceType);
            mLogger.debug("Stopping the " + mServiceNames.get(tServiceType));
            try {
                tService.stop();
                mSubservices.remove(tServiceType);
            } catch (ServiceRunException e) {
                mLogger.error("Could not stop the " + mServiceNames.get(tServiceType), e);
            }
        }
    }
}
