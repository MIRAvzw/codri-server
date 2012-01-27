/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.bo.repository.processors;

import be.mira.adastra3.server.exceptions.InvalidStateException;
import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.bo.repository.configuration.Configuration;
import be.mira.adastra3.server.bo.repository.configuration.SoundConfiguration;
import be.mira.adastra3.spring.Slf4jLogger;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

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
    
    private Configuration mConfiguration;
    private final long mRevision;
    private final String mPath;
    
    
    //
    // Construction and destruction
    //
    
    public ConfigurationProcessor(final long iRevision, final String iPath, final File iFile) throws RepositoryException {
        super(iFile, "configuration.xsd");
        mRevision = iRevision;
        mPath = iPath;
    }
    
    
    //
    // Public API
    //
    
    public final void process() throws RepositoryException {
        try {
            // Setup parsing
            if (getParser().getEventType() != XmlPullParser.START_DOCUMENT) {
                throw new RepositoryException("not at start of document");
            }
            
            // Process tags
            getParser().next();
            loop: while (getParser().getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (getParser().getEventType()) {
                    case (XmlPullParser.END_TAG):
                        getParser().next();
                        break loop;
                    case (XmlPullParser.START_TAG):
                        if (getParser().getName().equals("configuration")) {
                            mConfiguration = parseConfiguration();
                        } else {
                            throw new InvalidStateException("inconsistency detected between validator and processor (unknown tag)");
                        }
                        break;
                    default:                        
                        getParser().next();
                }
            }
        } catch (XmlPullParserException tException) {
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
    
    private Configuration parseConfiguration() throws RepositoryException, XmlPullParserException, IOException {        
        // Process the tags
        SoundConfiguration tSoundConfiguration = null;
        getParser().next();
        loop: while (getParser().getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (getParser().getEventType()) {
                case (XmlPullParser.END_TAG):
                    getParser().next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    if (getParser().getName().equals("sound")) {
                        tSoundConfiguration = parseSoundConfiguration();
                    } else {
                        throw new InvalidStateException("inconsistency detected between validator and processor (unknown tag)");
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
    
    private SoundConfiguration parseSoundConfiguration() throws RepositoryException, XmlPullParserException, IOException {
        // Process the tags
        Integer tVolume = null;
        getParser().next();
        loop: while (getParser().getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (getParser().getEventType()) {
                case (XmlPullParser.END_TAG):
                    getParser().next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    if (getParser().getName().equals("volume")) {
                        tVolume = Integer.parseInt(parseTextElement());
                    } else {
                        throw new InvalidStateException("inconsistency detected between validator and processor (unknown tag)");
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
