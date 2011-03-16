/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.common.repository;

import org.ini4j.Ini;

/**
 *
 * @author tim
 */
public class Configuration {
    //
    // Subclasses
    //

    public class Sound {
        public Boolean enabled;
        public Integer volume;

        void merge(Sound iSound) {
            if (enabled == null)
                enabled = iSound.enabled;
            if (volume == null)
                volume = iSound.volume;
        }
    }


    //
    // Data members
    //

    String mDescription;
    
    Sound mSound;


    //
    // Construction and destruction
    //

    public Configuration() {
        
    }

    public Configuration(Ini iIniReader) throws TopologyException {
        // Configuration section
        Ini.Section tConfigConfiguration = iIniReader.get("configuration");
        if (tConfigConfiguration != null)
            processConfiguration(tConfigConfiguration);

        // Sound section
        Ini.Section tConfigSound = iIniReader.get("sound");
        if (tConfigSound != null)
            processSound(tConfigSound);
    }


    //
    // Configuration processing
    //

    final void processConfiguration(Ini.Section tIniSection) throws TopologyException {
        mDescription = tIniSection.fetch("description");
    }

    final void processSound(Ini.Section tIniSection) throws TopologyException {
        Sound tSound = new Sound();

        String tVolumeString = tIniSection.fetch("volume");
        if (tVolumeString != null)
            tSound.volume = Integer.parseInt(tVolumeString);

        String tEnabledString = tIniSection.fetch("enabled");
        if (tEnabledString != null)
            tSound.enabled = Boolean.parseBoolean(tEnabledString);

        setSound(tSound);
    }


    //
    // Getters and setters
    //

    String getDescription() {
        return mDescription;
    }

    Sound getSound() {
        return mSound;
    }

    void setSound(Sound iSound) throws TopologyException {
        if (mSound == null)
            mSound = iSound;
        else {
            // Log how it got ignored
            return;
        }
    }
}
