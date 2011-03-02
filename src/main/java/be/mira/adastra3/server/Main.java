package be.mira.adastra3.server;

/**
 * Hello world!
 *
 */

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.LifecycleException;
import javax.servlet.ServletException;

public class Main 
{
    public static void main(String[] args) throws ServletException, LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
 
        tomcat.setBaseDir(".");
 
        // Zonder manuele configuratie: http://www.copperykeenclaws.com/embedding-tomcat-7/
        Context ctx = tomcat.addWebapp("/examples", "examples");
        Tomcat.addServlet(ctx, "helloWorldServlet", "be.mira.adastra3.server.website.HelloWorld");
 
        ctx.addServletMapping("/helloworld", "helloWorldServlet");
 
        tomcat.start();
        tomcat.getServer().await();
    }
}
