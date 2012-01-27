/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.repository.processors;

import be.mira.adastra3.server.exceptions.InvalidStateException;
import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.connection.Connection;
import be.mira.adastra3.spring.Slf4jLogger;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 *
 * @author tim
 */
public class ConnectionProcessor extends Processor {
    //
    // Member data
    //
    
    @Slf4jLogger
    private Logger mLogger;
    
    private Connection mConnection;
    private final long mRevision;
    private final String mRepositoryPath;
    private final String mRepositoryLocation;
    
    
    
    
    //
    // Construction and destruction
    //
    
    public ConnectionProcessor(final long iRevision, final String iRepositoryPath, final String iRepositoryLocation, final File iFile) throws RepositoryException {
        super(iFile, "connection.xsd");
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
                        if (getParser().getName().equals("connection")) {
                            mConnection = parseConnection();
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
    
    public final Connection getConnection() {
        return mConnection;
    }
    
    
    //
    // Parsing helpers
    //
    
    private Connection parseConnection() throws RepositoryException, XmlPullParserException, IOException {       
        // Process the tags
        UUID tKiosk = null;
        String tConfiguration = null;
        String tPresentation = null;
        getParser().next();
        loop: while (getParser().getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (getParser().getEventType()) {
                case (XmlPullParser.END_TAG):
                    getParser().next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    if (getParser().getName().equals("kiosk")) {
                        tKiosk = UUID.fromString(parseTextElement());
                    } else if (getParser().getName().equals("configuration")) {
                        tConfiguration = parseTextElement();
                    } else if (getParser().getName().equals("presentation")) {
                        tPresentation = parseTextElement();
                    } else {
                        throw new InvalidStateException("inconsistency detected between validator and processor (unknown tag)");
                    }
                    break;
                default:
                    getParser().next();
            }
        }
        
        // Create the object
        Connection tConnection = new Connection(
                mRevision,
                mRepositoryPath,
                mRepositoryLocation,
                tKiosk,
                tConfiguration,
                tPresentation);
        return tConnection;
    }
}
