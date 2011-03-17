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

    public Configuration(Ini iIniReader) throws RepositoryException {
        // Process all sections
        for (String tSectionName: iIniReader.keySet()) {
            Ini.Section tSection = iIniReader.get(tSectionName);

            if (tSectionName.equalsIgnoreCase("configuration"))
                processConfiguration(tSection);
            else if (tSectionName.equalsIgnoreCase("sound"))
                processSound(tSection);
            else
                throw new RepositoryException("Configuration contains unknown section '" + tSectionName + "'");
        }
    }


    //
    // Configuration processing
    //

    final void processConfiguration(Ini.Section tSection) throws RepositoryException {
        for (String tOptionKey: tSection.keySet()) {
            String tOptionValue = tSection.fetch(tOptionKey);
            if(tOptionKey.equalsIgnoreCase("description"))
                mDescription = tOptionValue;
            else
                throw new RepositoryException("Configuration contains unknown option '" + tOptionKey + "'");
            // TODO: how to handle child sections? this currently crashes
        }
    }

    final void processSound(Ini.Section tSection) throws RepositoryException {
        Sound tSound = new Sound();

        for (String tOptionKey: tSection.keySet()) {
            String tOptionValue = tSection.fetch(tOptionKey);
            if (tOptionKey.equalsIgnoreCase("volume"))
                tSound.volume = Integer.parseInt(tOptionValue);
            else if(tOptionKey.equalsIgnoreCase("enabled"))
                tSound.enabled = Boolean.parseBoolean(tOptionValue);
            else
                throw new RepositoryException("Configuration contains unknown option '" + tOptionKey + "'");
        }

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

    void setSound(Sound iSound) throws RepositoryException {
        if (iSound.volume != null && (iSound.volume < 0 || iSound.volume > 100))
            throw new RepositoryException("Sound.volume option contains invalid value");
        mSound = iSound;
    }
}
