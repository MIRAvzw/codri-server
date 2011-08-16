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
    //
    // Data members
    //
    
    private Tomcat mTomcat;
    private String mServerRoot;


    //
    // Construction and destruction
    //

    public EmbeddedTomcat() throws ServiceSetupException {
        super();
        
        mTomcat = new Tomcat();

        // Connector protocol
        Connector tConnector;
        String tConnectorProtocol = getConfiguration().getString("website.protocol");
        if (tConnectorProtocol.equalsIgnoreCase("http")) {
            getLogger().debug("Using HTTP connector");
            tConnector = new Connector("HTTP/1.1");
        } else if (tConnectorProtocol.equalsIgnoreCase("ajp")) {
            getLogger().debug("Using AJP connector");
            tConnector = new Connector("AJP/1.3");
        } else {
            throw new ServiceSetupException("Unknown connector type");
        }
        mTomcat.getService().addConnector(tConnector);
        mTomcat.setConnector(tConnector);

        // Port
        try {
            Integer tPort = getConfiguration().getInt("website.port");
            if (tPort <= 0 || tPort > 65536) {
                throw new ServiceSetupException("Server port out of valid range");
            }
            getLogger().debug("Using port " + tPort);
            tConnector.setPort(tPort);
        } catch (NumberFormatException tException) {
            throw new ServiceSetupException("Non-integer port specification");
        }

        // Server root
        mServerRoot = getConfiguration().getString("website.root");
        if (! new File(mServerRoot).isDirectory()) {
            throw new ServiceSetupException("Cannot read server root directory");
        }
    }


    //
    // Service interface
    //

    @Override
    public final void run() throws ServiceRunException {
        try {
            mTomcat.start();
            // TODO: if the address is not in use, tomcat prints an error
            // but doesn't die (happens in a thread)!!
        } catch (LifecycleException tException) {
            throw new ServiceRunException(tException);
        }
    }

    @Override
    final public void stop() throws ServiceRunException {
        try {
            mTomcat.stop();
        } catch (LifecycleException tException) {
            throw new ServiceRunException(tException);
        }
    }


    //
    // Auxiliary methods
    //

    public final Wrapper addServlet(final String iName, final Servlet iServlet, final String iMountpoint) throws ServiceSetupException {
        File tDocumentBase = new File(System.getProperty("java.io.tmpdir"));
        Context tContext = mTomcat.addContext("/" + iMountpoint, tDocumentBase.getAbsolutePath());
        Wrapper tWrapperWrapper = mTomcat.addServlet(tContext, iName, iServlet);
        tContext.addServletMapping("/*", iName);
        return tWrapperWrapper;
    }
}
