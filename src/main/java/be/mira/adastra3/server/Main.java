package be.mira.adastra3.server;

/**
 * Hello world!
 *
 */

import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.LifecycleException;
import javax.servlet.ServletException;

public class Main 
{
    public static void main(String[] args) throws ServletException, LifecycleException {
        // Initialise and configure tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        
        // Load the status web application
        File docBase = new File(".", "webapps/status");
        tomcat.addWebapp(null, "/status", docBase.getAbsolutePath());
        
        // Start the server
        tomcat.start();
        tomcat.getServer().await();
    }
}
