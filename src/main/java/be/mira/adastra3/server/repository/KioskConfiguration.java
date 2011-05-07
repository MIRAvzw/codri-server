/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.exceptions.RepositoryException;
import java.util.ArrayList;
import java.util.List;
import org.ini4j.Ini;

/**
 *
 * @author tim
 */
public class KioskConfiguration extends Configuration {
    //
    // Member data
    //

    List<String> mDependantConfigurations;
    KioskConfiguration mShallowConfiguration;


    //
    // Construction and destruction
    //

    public KioskConfiguration(KioskConfiguration old) {
        super(old);
        mDependantConfigurations = old.mDependantConfigurations;
    }

    public KioskConfiguration(Ini iIniReader) throws RepositoryException {
        super();
        mDependantConfigurations = new ArrayList<String>();
        process(iIniReader);

        mShallowConfiguration = new KioskConfiguration(this);
        flatten();
    }

    
    //
    // Configuration processing
    //

    final void processKiosk(Ini.Section iIniSection) throws RepositoryException {
        String tLoad = iIniSection.fetch("load");
        if (tLoad != null) {
            String[] tConfigurationNames = tLoad.split(";");
            for (String tConfigurationName : tConfigurationNames)
                mDependantConfigurations.add(tConfigurationName);
        }
    }

    final void flatten() throws RepositoryException {
        Sound tSound = new Sound();

        for (String tConfigurationName : mDependantConfigurations) {
            Configuration tConfiguration = Repository.getInstance().getConfiguration(tConfigurationName);
            if (tConfiguration == null)
                throw new RepositoryException("Could not find dependant configuration '" + tConfigurationName + "'");

            tSound.apply(tConfiguration.getSound());
        }

        tSound.apply(mShallowConfiguration.getSound());
        mSound = tSound;
    }


    //
    // Getters and setters
    //

    List<String> getDependantConfigurations() {
        return mDependantConfigurations;
    }
}
