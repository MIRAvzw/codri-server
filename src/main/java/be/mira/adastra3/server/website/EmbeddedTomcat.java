/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.website;

import be.mira.adastra3.server.Service;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import java.io.File;
import javax.servlet.Servlet;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;

/**
 *
 * @author tim
 */
public class EmbeddedTomcat extends Service {
    private Tomcat mTomcat;
    private String mServerRoot;

    public EmbeddedTomcat() throws ServiceSetupException {
        super();
        
        mTomcat = new Tomcat();

        // Connector type
        Connector tConnector;
        String iConnectorType = getProperty("type", "http");
        if (iConnectorType.equalsIgnoreCase("http")) {
            getLogger().debug("Using HTTP connector");
            tConnector = new Connector("HTTP/1.1");
        }
        else if (iConnectorType.equalsIgnoreCase("ajp")) {
            getLogger().debug("Using AJP connector");
            tConnector = new Connector("AJP/1.3");
        }
        else
            throw new ServiceSetupException("Unknown connector type");
        mTomcat.getService().addConnector(tConnector);
        mTomcat.setConnector(tConnector);

        // Port
        try {
            Integer iPort = Integer.parseInt(getProperty("port", "8080"));
            if (iPort <= 0 || iPort > 65536)
                throw new ServiceSetupException("Server port out of valid range");
            getLogger().debug("Using port " + iPort);
            tConnector.setPort(iPort);
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
