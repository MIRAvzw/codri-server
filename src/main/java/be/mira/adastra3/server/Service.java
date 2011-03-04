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

/**
 *
 * @author tim
 */
public abstract class Service {
    private Properties mProperties;

    public Service() throws ServiceSetupException {
        try {
            mProperties = getProperties(this.getClass().getSimpleName());
        } catch (Exception e) {
            throw new ServiceSetupException(e);
        }
    }

    private Properties getProperties(String iBasename) throws Exception {
        String tFilename = iBasename + ".properties";
        
        InputStream tStream = this.getClass().getClassLoader().getResourceAsStream(tFilename);
        if (tStream == null)
                throw new Exception("Could not open properties file '" + tFilename + "'");

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

    abstract public void run() throws ServiceRunException;
}
