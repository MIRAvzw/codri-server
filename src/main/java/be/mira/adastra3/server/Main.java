package be.mira.adastra3.server;

import be.mira.adastra3.server.exceptions.ServiceSetupException;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.website.EmbeddedTomcat;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;

/**
 * Hello world!
 *
 */



public class Main 
{
    public static void main(String[] args) throws ServletException, LifecycleException {
        EmbeddedTomcat mTomcat;

        // Configure Tomcat
        try {
            mTomcat = new EmbeddedTomcat();
            mTomcat.addWebapp("status");
        } catch (ServiceSetupException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }

        // Run Tomcat
        try {
            mTomcat.run();
        } catch (ServiceRunException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
    }
}
