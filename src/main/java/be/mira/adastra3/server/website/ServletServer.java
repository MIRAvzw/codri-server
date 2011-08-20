/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.website;

import be.mira.adastra3.server.Service;
import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import javax.servlet.Servlet;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 *
 * @author tim
 */
public class ServletServer extends Service {
    //
    // Data members
    //
    
    private Server mServer;
    private Context mServerContext;


    //
    // Construction and destruction
    //

    public ServletServer() throws ServiceSetupException {
        super();
        
        mServer = new Server();

        // Connector
        SocketConnector tConnector = new SocketConnector();
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
        mServer.setConnectors(new Connector[]{tConnector});

        // Server context
        mServerContext = new Context(mServer, "/", Context.SESSIONS);
    }


    //
    // Service interface
    //

    @Override
    public final void run() throws ServiceRunException {
        try {
            mServer.start();
            // TODO: if the address is not in use, tomcat prints an error
            // but doesn't die (happens in a thread)!!
        } catch (Exception tException) {
            throw new ServiceRunException(tException);
        }
    }

    @Override
    final public void stop() throws ServiceRunException {
        try {
            mServer.stop();
        } catch (Exception tException) {
            throw new ServiceRunException(tException);
        }
    }


    //
    // Auxiliary methods
    //

    public final void addServlet(final Servlet iServlet, final String iMountpoint) throws ServiceSetupException {
        mServerContext.addServlet(new ServletHolder(iServlet), iMountpoint);
    }
}
