/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configurations.application;

import be.mira.adastra3.server.repository.configurations.Configuration;

/**
 *
 * @author tim
 */
public class MediaConfiguration extends Configuration  {
    String getLocation() {
        if (getProperty("location") != null)
            return (String) getProperty("location");
        else
            return null;
    }

    public void setLocation(String iLocation) {
        setProperty("location", iLocation);
    }   
}
