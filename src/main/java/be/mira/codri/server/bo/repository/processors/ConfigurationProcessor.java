/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.bo.repository.processors;

import be.mira.codri.server.exceptions.InvalidStateException;
import be.mira.codri.server.exceptions.RepositoryException;
import be.mira.codri.server.bo.repository.configuration.Configuration;
import be.mira.codri.server.bo.repository.configuration.SoundConfiguration;
import be.mira.codri.server.spring.Slf4jLogger;
import java.io.IOException;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;

/**
 *
 * @author tim
 */
public class ConfigurationProcessor extends Processor {
    //
    // Member data
    //
    
    @Slf4jLogger
    private Logger mLogger;
    
    private long mRevision;
    private String mPath;
    
    private Configuration mConfiguration;
    
    
    //
    // Construction and destruction
    //
    
    // TODO: use typical bean construction
    
    //@Required
    public void setRevision(final long iRevision) {
        mRevision = iRevision;
    }
    
    //@Required
    public void setPath(final String iPath) {
        mPath = iPath;
    }
    
    //@PostConstruct
    @Override
    public void init() throws RepositoryException {
        setValidationFilename("configuration.xsd");
        super.init();
    }
    
    
    //
    // Public API
    //
    
    public final void process() throws RepositoryException {
        try {
            // Setup parsing
            if (getParser().getEventType() != XMLStreamConstants.START_DOCUMENT
                    && getParser().getEventType() != XMLStreamConstants.START_ELEMENT) {
                throw new RepositoryException("not at start of document or element");
            }
            
            // Process tags
            getParser().next();
            loop: while (getParser().getEventType() != XMLStreamConstants.END_DOCUMENT) {
                switch (getParser().getEventType()) {
                    case (XMLStreamConstants.END_ELEMENT):
                        getParser().next();
                        break loop;
                    case (XMLStreamConstants.START_ELEMENT):
                        if (getTag().equals("configuration")) {
                            mConfiguration = parseConfiguration();
                        } else {
                            throw new InvalidStateException("inconsistency detected between validator and processor (unknown tag '" + getParser().getName() + "')");
                        }
                        break;
                    default:                        
                        getParser().next();
                }
            }
        } catch (XMLStreamException tException) {
            throw new RepositoryException(tException);
        } catch (IOException tException) {
            throw new RepositoryException(tException);
        }
    }
    
    public final Configuration getConfiguration() {
        return mConfiguration;
    }
    
    
    //
    // Parsing helpers
    //
    
    private Configuration parseConfiguration() throws RepositoryException, XMLStreamException, IOException {        
        // Process the tags
        SoundConfiguration tSoundConfiguration = null;
        getParser().next();
        loop: while (getParser().getEventType() != XMLStreamConstants.END_DOCUMENT) {
            switch (getParser().getEventType()) {
                case (XMLStreamConstants.END_ELEMENT):
                    getParser().next();
                    break loop;
                case (XMLStreamConstants.START_ELEMENT):
                    if (getTag().equals("sound")) {
                        tSoundConfiguration = parseSoundConfiguration();
                    } else {
                            throw new InvalidStateException("inconsistency detected between validator and processor (unknown tag '" + getParser().getName() + "')");
                    }
                    break;
                default:
                    getParser().next();
            }
        }
        
        // Create the object
        // TODO: null check of soundconfiguration? can it be missing?
        Configuration tConfiguration = new Configuration(
                mRevision,
                mPath,
                tSoundConfiguration);
        return tConfiguration;
    }
    
    private SoundConfiguration parseSoundConfiguration() throws RepositoryException, XMLStreamException, IOException {
        // Process the tags
        Integer tVolume = null;
        getParser().next();
        loop: while (getParser().getEventType() != XMLStreamConstants.END_DOCUMENT) {
            switch (getParser().getEventType()) {
                case (XMLStreamConstants.END_ELEMENT):
                    getParser().next();
                    break loop;
                case (XMLStreamConstants.START_ELEMENT):
                    if (getTag().equals("volume")) {
                        tVolume = Integer.parseInt(parseTextElement());
                    } else {
                            throw new InvalidStateException("inconsistency detected between validator and processor (unknown tag '" + getParser().getName() + "')");
                    }
                    break;
                default:
                    getParser().next();
            }
        }
        
        // Create the object
        // TODO: null check? can volume be missing?
        SoundConfiguration tSound = new SoundConfiguration(tVolume);
        return tSound;
    }
}
