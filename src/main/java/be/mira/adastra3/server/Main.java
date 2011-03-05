package be.mira.adastra3.server;

import be.mira.adastra3.server.discovery.ServiceDiscoverer;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.website.EmbeddedTomcat;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
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
        System.out.println("Signal handler called for signal " + signal);
        try {
            signalAction(signal);

            // Chain back to previous handler, if one exists
            if (oldHandler != SIG_DFL && oldHandler != SIG_IGN) {
                oldHandler.handle(signal);
            }

        } catch (Exception e) {
            System.out.println("handle|Signal handler failed, reason " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void signalAction(Signal signal) {
        System.out.println("Handling " + signal.getName());

        if (mTomcat != null) {
            try {
                mTomcat.stop();
                mTomcat = null;
            } catch (ServiceRunException e) {
                System.out.println("Could not stop Tomcat");
            }
        }

        if (mServiceDiscoverer != null) {
            try {
                mServiceDiscoverer.stop();
                mServiceDiscoverer = null;
            } catch (ServiceRunException e) {
                System.out.println("Could not stop service discoverer");
            }
        }

        System.exit(0);
    }


    //
    // Static
    //
    
    private static EmbeddedTomcat mTomcat;
    private static ServiceDiscoverer mServiceDiscoverer;

    public static SignalHandler install(String signalName) {
        Signal diagSignal = new Signal(signalName);
        Main instance = new Main();
        instance.oldHandler = Signal.handle(diagSignal, instance);
        return instance;
    }

    public static void main(String[] args) throws ServletException, LifecycleException {
        //
        // Set-up
        //

        // Install handlers
        Main.install("TERM");
        Main.install("INT");
        Main.install("ABRT");

        // Configure Tomcat
        try {
            mTomcat = new EmbeddedTomcat();
            mTomcat.addWebapp("status");
        } catch (ServiceSetupException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }

        // Configure service discoverer
        try {
            mServiceDiscoverer = new ServiceDiscoverer();
        } catch (ServiceSetupException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }


        //
        // Start
        //

        // Run Tomcat
        try {
            mTomcat.run();
        } catch (ServiceRunException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
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

        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
