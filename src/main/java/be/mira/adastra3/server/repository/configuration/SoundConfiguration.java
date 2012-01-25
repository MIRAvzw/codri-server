/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tim
 */
@XmlRootElement(name="sound")
public final class SoundConfiguration {
    //
    // Data members
    //
    
    private Integer mVolume;
    
    
    //
    // Constructors
    //
    
    // FIXME: dummy constructor for JAXB (shouldn't be neccesary as JAXB never
    // has to unmarshal this class)
    public SoundConfiguration() {
        this(null);
    }
    
    public SoundConfiguration(final Integer iVolume) {
        mVolume = iVolume;
    }
    
    
    //
    // Getters
    //
    
    @XmlElement
    public Integer getVolume() {
        return mVolume;
    }
}
