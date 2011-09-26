/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server;

import be.mira.adastra3.server.exceptions.ServiceRunException;
import be.mira.adastra3.server.exceptions.ServiceSetupException;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.log4j.Logger;

/**
 *
 * @author tim
 */
public abstract class Service {
    //
    // Data members
    //
    
    private static CompositeConfiguration mConfiguration;
    private Logger mLogger;
    
    
    //
    // Static functionality
    //
    
    public static Configuration getConfiguration() throws ServiceSetupException {
        if (mConfiguration == null) {
            mConfiguration = new CompositeConfiguration();
            mConfiguration.addConfiguration(new SystemConfiguration());
            try {
                mConfiguration.addConfiguration(new PropertiesConfiguration("aa3server.properties"));
            } catch (ConfigurationException tException) {
                // Do nothing
            }
            try {
                mConfiguration.addConfiguration(new PropertiesConfiguration("defaults.properties"));
            } catch (ConfigurationException tException) {
                throw new ServiceSetupException("could not load defaults", tException);
            }
        }
        
        return mConfiguration;
    }
    
    
    //
    // Construction and destruction
    //

    public Service() throws ServiceSetupException {
        mLogger = Logger.getLogger(this.getClass());
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
    
    public static Logger getLogger(Object iObject) {
        return Logger.getLogger(iObject.getClass());
    }
    
    public static Logger getLogger(Class iClass) {
        return Logger.getLogger(iClass);
    }
}
