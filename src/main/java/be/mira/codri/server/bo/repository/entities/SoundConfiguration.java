/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.bo.repository.entities;

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
