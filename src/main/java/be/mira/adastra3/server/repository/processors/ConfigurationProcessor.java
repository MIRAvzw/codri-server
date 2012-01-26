/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.repository.processors;

import be.mira.adastra3.server.exceptions.InvalidStateException;
import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.configuration.Configuration;
import be.mira.adastra3.server.repository.configuration.SoundConfiguration;
import java.io.File;
import java.io.IOException;
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
    
    private Configuration mConfiguration;
    private final long mRevision;
    private final String mRepositoryPath;
    private final String mRepositoryLocation;
    
    
    //
    // Construction and destruction
    //
    
    public ConfigurationProcessor(final long iRevision, final String iRepositoryPath, final String iRepositoryLocation, final File iFile) throws RepositoryException {
        super(iFile, "configuration.xsd");
        mRevision = iRevision;
        mRepositoryPath = iRepositoryPath;
        mRepositoryLocation = iRepositoryLocation;
    }
    
    
    //
    // Public API
    //
    
    public final void process() throws RepositoryException {
        try {
            // Setup parsing
            if (mParser.getEventType() != XmlPullParser.START_DOCUMENT) {
                throw new RepositoryException("not at start of document");
            }
            
            // Process tags
            mParser.next();
            loop: while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (mParser.getEventType()) {
                    case (XmlPullParser.END_TAG):
                        mParser.next();
                        break loop;
                    case (XmlPullParser.START_TAG):
                        if (mParser.getName().equals("configuration")) {
                            mConfiguration = parseConfiguration();
                        } else {
                            throw new InvalidStateException("inconsistency detected between validator and processor (unknown tag)");
                        }
                        break;
                    default:                        
                        mParser.next();
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
        mParser.next();
        loop: while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (mParser.getEventType()) {
                case (XmlPullParser.END_TAG):
                    mParser.next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    if (mParser.getName().equals("sound")) {
                        tSoundConfiguration = parseSoundConfiguration();
                    } else {
                        throw new InvalidStateException("inconsistency detected between validator and processor (unknown tag)");
                    }
                    break;
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        // TODO: null check of soundconfiguration? can it be missing?
        Configuration tConfiguration = new Configuration(
                mRevision,
                mRepositoryPath,
                mRepositoryLocation,
                tSoundConfiguration);
        return tConfiguration;
    }
    
    private SoundConfiguration parseSoundConfiguration() throws RepositoryException, XmlPullParserException, IOException {
        // Process the tags
        Integer tVolume = null;
        mParser.next();
        loop: while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (mParser.getEventType()) {
                case (XmlPullParser.END_TAG):
                    mParser.next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    if (mParser.getName().equals("volume")) {
                        tVolume = Integer.parseInt(parseTextElement());
                    } else {
                        throw new InvalidStateException("inconsistency detected between validator and processor (unknown tag)");
                    }
                    break;
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        // TODO: null check? can volume be missing?
        SoundConfiguration tSound = new SoundConfiguration(tVolume);
        return tSound;
    }
}
