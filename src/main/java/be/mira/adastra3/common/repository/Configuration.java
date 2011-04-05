/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.common.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
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

        void apply(Sound iSound) {
            if (iSound == null)
                return;
            
            if (iSound.enabled != null)
                enabled = iSound.enabled;
            if (iSound.volume != null)
                volume = iSound.volume;
        }
    }


    //
    // Data members
    //

    Map<String, Method> mProcessors;

    String mDescription;    
    Sound mSound;


    //
    // Construction and destruction
    //

    public Configuration(Configuration old) {
        mDescription = old.mDescription;
        mSound = old.mSound;
    }

    public Configuration() {
        // Load all processing methods
        Method[] tMethods = this.getClass().getDeclaredMethods();
        mProcessors = new HashMap<String, Method>();
        for (Method tMethod : tMethods) {
            if (tMethod.getName().startsWith("process")) {
                mProcessors.put(tMethod.getName().toLowerCase(), tMethod);
            }
        }        
    }

    public Configuration(Ini iIniReader) throws RepositoryException {
        this();
        process(iIniReader);
    }


    //
    // Configuration processing
    //

    final void process(Ini iIniReader) throws RepositoryException {
        // Process all sections
        for (String tSectionName: iIniReader.keySet()) {
            Ini.Section tSection = iIniReader.get(tSectionName);

            // Find processing method
            String tProcessorName = "process" + tSectionName.toLowerCase();
            if (mProcessors.containsKey(tProcessorName)) {
                Method tProcessor = mProcessors.get(tProcessorName);
                try {
                    tProcessor.invoke(this, tSection);
                } catch (IllegalAccessException e) {
                    throw new RepositoryException("Could not access processor method for section '" + tSectionName + "'", e);
                } catch (InvocationTargetException e) {
                    throw new RepositoryException("Processor method for section '" + tSectionName + "' reported an error", e);
                }
            } else
                throw new RepositoryException("Configuration contains unknown section '" + tSectionName + "'");
        }
    }

    final void processConfiguration(Ini.Section tSection) throws RepositoryException {
        for (String tOptionKey: tSection.keySet()) {
            String tOptionValue = tSection.fetch(tOptionKey);
            if(tOptionKey.equalsIgnoreCase("description"))
                mDescription = tOptionValue;
            else
                throw new RepositoryException("Configuration contains unknown option '" + tOptionKey + "'");
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
