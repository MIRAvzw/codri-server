/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tim
 */
public abstract class Configuration {
    //
    // Member data
    //
    
    private Map<String, Object> mProperties;
    
    
    //
    // Construction and destruction
    //
    
    public Configuration() {
        mProperties = new HashMap<String, Object>();
    }
    
    
    //
    // Getters and setters
    //
    
    protected Object getProperty(String iName) {
        if (mProperties.containsKey(iName))
            return mProperties.get(iName);
        else
            return null;
    }
    
    protected void setProperty(String iName, Object iProperty) {
        if (iProperty == null)
            return;
        mProperties.put(iName, iProperty);
    }
}
