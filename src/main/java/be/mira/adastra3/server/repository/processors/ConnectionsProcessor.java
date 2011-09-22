/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository.processors;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.configuration.SoundConfiguration;
import be.mira.adastra3.server.repository.connection.Connection;
import be.mira.adastra3.server.repository.connection.KioskConnection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 *
 * @author tim
 */
public class ConnectionsProcessor extends Processor {
    //
    // Member data
    //
    
    private List<Connection> mConnections;
    
    
    
    
    //
    // Construction and destruction
    //
    
    public ConnectionsProcessor(final File iConfigurationFile) throws RepositoryException {
        super(iConfigurationFile, "connections.xsd");
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
                        if (mParser.getName().equals("connections")) {
                            mConnections = parseConnections();
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
    
    public final List<Connection> getConnections() {
        return mConnections;
    }
    
    
    //
    // Parsing helpers
    //
    
    private List<Connection> parseConnections() throws RepositoryException, XmlPullParserException, IOException {
        // Process the tags
        List<Connection> tConnections = new ArrayList<Connection>();
        mParser.next();
        loop: while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (mParser.getEventType()) {
                case (XmlPullParser.END_TAG):
                    mParser.next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    if (mParser.getName().equals("kioskconnection")) {
                        tConnections.add(parseKioskConnection());
                    }
                    break;
                default:
                    mParser.next();
            }
        }
        
        return tConnections;        
    }
    
    private KioskConnection parseKioskConnection() throws RepositoryException, XmlPullParserException, IOException {       
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
                    }
                    break;
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        KioskConnection tKioskConnection = new KioskConnection(
                tKiosk,
                tConfiguration,
                tPresentation);
        return tKioskConnection;
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
                    }
                    break;
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        SoundConfiguration tSound = new SoundConfiguration(tVolume);
        return tSound;
    }
}
