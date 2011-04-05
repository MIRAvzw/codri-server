/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.common.repository;

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


    //
    // Construction and destruction
    //

    public KioskConfiguration(Ini iIniReader) throws RepositoryException {
        super();
        mDependantConfigurations = new ArrayList<String>();
        process(iIniReader);
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

    void flatten() throws RepositoryException {
        Sound tSound = new Sound();

        for (String tConfigurationName : mDependantConfigurations) {
            Configuration tConfiguration = Repository.getInstance().getConfiguration(tConfigurationName);
            if (tConfiguration == null)
                throw new RepositoryException("Could not find dependant configuration '" + tConfigurationName + "'");

            tSound.apply(tConfiguration.getSound());
        }

        tSound.apply(mSound);
        mSound = tSound;
    }


    //
    // Getters and setters
    //

    List<String> getDependantConfigurations() {
        return mDependantConfigurations;
    }
}
