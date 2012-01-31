/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.bo.repository.processors;

import be.mira.codri.server.exceptions.InvalidStateException;
import be.mira.codri.server.exceptions.RepositoryException;
import be.mira.codri.server.bo.repository.connection.Connection;
import be.mira.codri.spring.Slf4jLogger;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;

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
    private final String mPath;
    
    
    
    
    //
    // Construction and destruction
    //
    
    public ConnectionProcessor(final long iRevision, final String iPath, final File iFile) throws RepositoryException {
        super(iFile, "connection.xsd");
        mRevision = iRevision;
        mPath = iPath;
    }
    
    
    //
    // Public API
    //
    
    public final void process() throws RepositoryException {
        try {
            // Setup parsing
            if (getParser().getEventType() != XMLStreamConstants.START_DOCUMENT) {
                throw new RepositoryException("not at start of document");
            }
            
            // Process tags
            getParser().next();
            loop: while (getParser().getEventType() != XMLStreamConstants.END_DOCUMENT) {
                switch (getParser().getEventType()) {
                    case (XMLStreamConstants.END_ELEMENT):
                        getParser().next();
                        break loop;
                    case (XMLStreamConstants.START_ELEMENT):
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
        } catch (XMLStreamException tException) {
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
    
    private Connection parseConnection() throws RepositoryException, XMLStreamException, IOException {       
        // Process the tags
        UUID tKiosk = null;
        String tConfiguration = null;
        String tPresentation = null;
        getParser().next();
        loop: while (getParser().getEventType() != XMLStreamConstants.END_DOCUMENT) {
            switch (getParser().getEventType()) {
                case (XMLStreamConstants.END_ELEMENT):
                    getParser().next();
                    break loop;
                case (XMLStreamConstants.START_ELEMENT):
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
                mPath,
                tKiosk,
                tConfiguration,
                tPresentation);
        return tConnection;
    }
}
