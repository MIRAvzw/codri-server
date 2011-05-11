/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.configurations.KioskConfiguration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author tim
 */
public class Repository {
    //
    // Member data
    //

    private Map<String, KioskConfiguration> mConfigurations;
    private Map<UUID, String> mConcreteMapping;


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
        mConfigurations = new HashMap<String, KioskConfiguration>();
        mConcreteMapping = new HashMap<UUID, String>();
    }


    //
    // Getters and setters
    //

    public void addConfiguration(KioskConfiguration iConfiguration) throws RepositoryException {
        if (mConfigurations.containsKey(iConfiguration.getName()))
            throw new RepositoryException("configuration " + iConfiguration.getName() + " already present in repository");
        mConfigurations.put(iConfiguration.getName(), iConfiguration);
        if (! iConfiguration.isAbstract()) {
            if (mConcreteMapping.containsKey(iConfiguration.getTarget()))
                throw new RepositoryException("configuration target " + iConfiguration.getTarget() + " in configuration " + iConfiguration.getName() + " already present in repository");
            mConcreteMapping.put(iConfiguration.getTarget(), iConfiguration.getName());
        }
    }

    public KioskConfiguration getConfiguration(String iName) throws RepositoryException {
        return mConfigurations.get(iName);
    }

    public KioskConfiguration getConfigurationByTarget(UUID iUuid) throws RepositoryException {
        if (!mConcreteMapping.containsKey(iUuid))
            return null;
        return mConfigurations.get(mConcreteMapping.get(iUuid));
    }
}
