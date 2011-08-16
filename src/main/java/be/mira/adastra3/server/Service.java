/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server;

import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
            // HACK
            String tClassName = this.getClass().getSimpleName();
            if (System.getProperty("java.vendor").equals("GNU Classpath")) {
                tClassName = tClassName.substring(tClassName.lastIndexOf(".")+1);
            }
            
            mProperties = getProperties(tClassName);
        } catch (Exception tException) {
            throw new ServiceSetupException(tException);
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

    final protected Logger getLogger() {
        return mLogger;
    }

    private Properties getProperties(final String iBasename) throws Exception {
        String tFilename = iBasename + ".properties";
        
        // Load 
        InputStream tStream = this.getClass().getClassLoader().getResourceAsStream(tFilename);
        if (tStream == null) {
            try {
                tStream = new FileInputStream("/etc/MIRA/" + tFilename);
            } catch (FileNotFoundException tException) {
                getLogger().warn("Could not open properties file '" + tFilename + "'");
                return new java.util.Properties();
            }
        }

        Properties tProperties = new java.util.Properties();
        try {
            tProperties.load(tStream);
            return tProperties;
        } catch (IOException tException) {
            throw tException;
        }
    }

    protected final String getProperty(final String iKey, final String iDefaultValue) {
        return mProperties.getProperty(iKey, iDefaultValue);
    }
}
