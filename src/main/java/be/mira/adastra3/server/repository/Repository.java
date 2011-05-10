/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.repository.configurations.OldKioskConfiguration;
import be.mira.adastra3.server.repository.configurations.OldConfiguration;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tim
 */
public class Repository {
    //
    // Member data
    //

    private Map<String, OldConfiguration> mConfigurations;
    private Map<String, OldKioskConfiguration> mKioskConfigurations;


    //
    // Static functionality
    //

    private static Repository mInstance;

    public static Repository getInstance() {
        if (mInstance == null)
            mInstance = new Repository();
        return mInstance;
    }


    //
    // Construction and destruction
    //

    private Repository() {
        mConfigurations = new HashMap<String, OldConfiguration>();
        mKioskConfigurations = new HashMap<String, OldKioskConfiguration>();
    }


    //
    // Getters and setters
    //

    public OldConfiguration getConfiguration(String iName) {
        return mConfigurations.get(iName);
    }

    public void addConfiguration(String iName, OldConfiguration iConfiguration) {
        mConfigurations.put(iName, iConfiguration);
    }

    public OldKioskConfiguration getKioskConfiguration(String iName) {
        return mKioskConfigurations.get(iName);
    }

    public void addKioskConfiguration(String iName, OldKioskConfiguration iKioskConfiguration) {
        mKioskConfigurations.put(iName, iKioskConfiguration);
    }
}
