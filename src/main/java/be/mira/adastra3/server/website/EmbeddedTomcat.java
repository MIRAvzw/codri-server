/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.website;

import be.mira.adastra3.server.Service;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import com.vaadin.terminal.gwt.server.ApplicationServlet;
import java.io.File;
import javax.servlet.Servlet;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;

/**
 *
 * @author tim
 */
public class EmbeddedTomcat extends Service {
    private Tomcat mTomcat;
    private String mServerRoot;

    public EmbeddedTomcat() throws ServiceSetupException {
        super();
        getLogger().debug("Configuring subsystem");
        
        mTomcat = new Tomcat();


        // Server port
        try {
            Integer iPort = Integer.parseInt(getProperty("port", "8080"));
            if (iPort <= 0 || iPort > 65536)
                throw new ServiceSetupException("Server port out of valid range");
            mTomcat.setPort(iPort);
        }
        catch (NumberFormatException e) {
            throw new ServiceSetupException("Non-integer port specification");
        }

        // Server root
        mServerRoot = getProperty("serverroot", ".");
        if (! new File(mServerRoot).isDirectory())
            throw new ServiceSetupException("Cannot read server root directory");
    }

    public void addWebapp(String iDirectory) throws ServiceSetupException {
        addWebapp(iDirectory, iDirectory);
    }

    public void addWebapp(String iDirectory, String iMountpoint) throws ServiceSetupException {
        getLogger().debug("Adding webapplication '" + iDirectory + "', mounted on '" + iMountpoint + "'");

        File tDocumentRoot = new File(mServerRoot, "webapps/" + iDirectory);
        if (! tDocumentRoot.isDirectory())
            throw new ServiceSetupException("Webapplication root not readable");
        mTomcat.addWebapp(null, "/" + iMountpoint, tDocumentRoot.getAbsolutePath());
    }
    
    public Wrapper addServlet(String iName, Servlet iServlet) throws ServiceSetupException {
        return addServlet(iName, iServlet, iName);
    }

    public Wrapper addServlet(String iName, Servlet iServlet, String iMountpoint) throws ServiceSetupException {
        File docBase = new File(System.getProperty("java.io.tmpdir"));
        Context ctxt = mTomcat.addContext("", docBase.getAbsolutePath());
        Wrapper oWrapper = mTomcat.addServlet(ctxt, iName, iServlet);
        ctxt.addServletMapping("/" + iMountpoint, iName);
        return oWrapper;
    }

    public void run() throws ServiceRunException {
        getLogger().debug("Starting subsystem");

        try {
            mTomcat.start();
        } catch (LifecycleException e) {
            throw new ServiceRunException(e);
        }
    }
    
    public void stop() throws ServiceRunException {
        getLogger().debug("Stopping subsystem");

        try {
            mTomcat.stop();
        }
        catch (LifecycleException e) {
            throw new ServiceRunException(e);
        }
    }
}