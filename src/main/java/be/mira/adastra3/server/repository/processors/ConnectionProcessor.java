/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.processors;

import be.mira.adastra3.server.exceptions.InvalidStateException;
import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.connection.Connection;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
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
    
    private Connection mConnection;
    private long mRevision;
    private String mRepositoryPath;
    private String mId;
    
    
    
    
    //
    // Construction and destruction
    //
    
    public ConnectionProcessor(final long iRevision, final String iRepositoryPath, final String iId, final File iFile) throws RepositoryException {
        super(iFile, "connection.xsd");
        mRevision = iRevision;
        mRepositoryPath = iRepositoryPath;
        mId = iId;
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
                        if (mParser.getName().equals("connection")) {
                            mConnection = parseConnection();
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
        mParser.next();
        loop: while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (mParser.getEventType()) {
                case (XmlPullParser.END_TAG):
                    mParser.next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    if (mParser.getName().equals("kiosk")) {
                        tKiosk = UUID.fromString(parseTextElement());
                    } else if (mParser.getName().equals("configuration")) {
                        tConfiguration = parseTextElement();
                    } else if (mParser.getName().equals("presentation")) {
                        tPresentation = parseTextElement();
                    } else {
                        throw new InvalidStateException("inconsistency detected between validator and processor (unknown tag)");
                    }
                    break;
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        Connection tConnection = new Connection(
                mId,
                mRevision,
                mRepositoryPath,
                tKiosk,
                tConfiguration,
                tPresentation);
        return tConnection;
    }
}
