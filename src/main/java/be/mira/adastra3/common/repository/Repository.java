/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.common.repository;

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

    private Map<String, Configuration> mConfigurations;
    private Map<String, KioskConfiguration> mKioskConfigurations;


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
        mConfigurations = new HashMap<String, Configuration>();
        mKioskConfigurations = new HashMap<String, KioskConfiguration>();
    }


    //
    // Getters and setters
    //

    public Configuration getConfiguration(String iName) {
        return mConfigurations.get(iName);
    }

    public void addConfiguration(String iName, Configuration iConfiguration) {
        mConfigurations.put(iName, iConfiguration);
    }

    public KioskConfiguration getKioskConfiguration(String iName) {
        return mKioskConfigurations.get(iName);
    }

    public void addKioskConfiguration(String iName, KioskConfiguration iKioskConfiguration) {
        mKioskConfigurations.put(iName, iKioskConfiguration);
    }
}
