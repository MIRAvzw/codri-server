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
        super(iIniReader);
        mDependantConfigurations = new ArrayList<String>();

        Ini.Section tConfigKiosk =  iIniReader.get("kiosk");
        if (tConfigKiosk != null)
            processKiosk(tConfigKiosk);
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

    Configuration flatten() throws RepositoryException {
        Configuration oConfiguration = new Configuration();

        oConfiguration.setSound(flattenSound());

        return oConfiguration;
    }

    Sound flattenSound() throws RepositoryException {
        Sound oSound = (mSound != null ? mSound : new Sound());
        for (String tConfigurationName : mDependantConfigurations) {
            Configuration tConfiguration = Repository.getInstance().getConfiguration(tConfigurationName);
            if (tConfiguration == null)
                throw new RepositoryException("Configuration does not exist");
            oSound.merge(tConfiguration.getSound());
        }
        return oSound;
    }


    //
    // Getters and setters
    //

    List<String> getDependantConfigurations() {
        return mDependantConfigurations;
    }
}
