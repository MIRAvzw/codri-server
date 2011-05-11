/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server;

import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author tim
 */
public abstract class Service {
    //
    // Data members
    //
    
    private Properties mProperties;
    private Logger mLogger;
    
    
    //
    // Construction and destruction
    //

    public Service() throws ServiceSetupException {
        mLogger = Logger.getLogger(this.getClass());
        
        try {
            mProperties = getProperties(this.getClass().getSimpleName());
        } catch (Exception e) {
            throw new ServiceSetupException(e);
        }
    }

    //
    // Service interface
    //
    
    abstract public void run() throws ServiceRunException;

    abstract public void stop() throws ServiceRunException;
    
    
    //
    // Getters and setters
    //

    protected Logger getLogger() {
        return mLogger;
    }

    private Properties getProperties(String iBasename) throws Exception {
        String tFilename = iBasename + ".properties";
        
        InputStream tStream = this.getClass().getClassLoader().getResourceAsStream(tFilename);
        if (tStream == null) {
                getLogger().warn("Could not open properties file '" + tFilename + "'");
                return new java.util.Properties();
        }

        Properties oProperties = new java.util.Properties();
        try {
            oProperties.load(tStream);
            return oProperties;
        } catch (IOException e) {
            throw e;
        }
    }

    protected final String getProperty(String iKey, String iDefaultValue) {
        return mProperties.getProperty(iKey, iDefaultValue);
    }
}
