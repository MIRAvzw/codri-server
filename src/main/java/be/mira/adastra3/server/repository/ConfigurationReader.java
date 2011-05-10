/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.configurations.ApplicationConfiguration;
import be.mira.adastra3.server.repository.configurations.DeviceConfiguration;
import be.mira.adastra3.server.repository.configurations.KioskConfiguration;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 *
 * @author tim
 */
public class ConfigurationReader {
    //
    // Member data
    //
    
    static private XmlPullParserFactory mParserFactory;
    static XmlPullParser mParser;
    
    
    
    //
    // Construction and destruction
    //
    
    public ConfigurationReader(InputStream iStream) throws RepositoryException {
        try {
            if (mParserFactory == null)
                mParserFactory = XmlPullParserFactory.newInstance();
            mParser = mParserFactory.newPullParser();
            mParser.setInput(iStream, null);
        } catch (XmlPullParserException iException) {
            throw new RepositoryException(iException);
        }
    }
    
    
    //
    // Public API
    //
    
    public void process() throws RepositoryException {
        try {
            // Setup parsing
            int tEventType = mParser.getEventType();
            if (tEventType != XmlPullParser.START_DOCUMENT)
                throw new RepositoryException("not at start of document");
            
            // Process tags
            tEventType = mParser.next();
            while (tEventType != XmlPullParser.END_DOCUMENT) {
                switch (tEventType) {
                    case (XmlPullParser.END_TAG):
                        tEventType = mParser.next();
                        break;
                    case (XmlPullParser.START_TAG):
                        if (mParser.getName().equals("root"))
                            parseRoot();
                        else
                            throw new RepositoryException("unknown tag " + mParser.getName());
                    default:                        
                        tEventType = mParser.next();
                }
            }
        }
        catch (XmlPullParserException iException) {
            throw new RepositoryException(iException);
        }
        catch (IOException iException) {
            throw new RepositoryException(iException);
        }
    }
    
    
    //
    // Parsing helpers
    //
    
    private void parseRoot() throws RepositoryException, XmlPullParserException, IOException {
        // Process the tags
        mParser.next();
        loop: while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (mParser.getEventType()) {
                case (XmlPullParser.END_TAG):
                    mParser.next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    if (mParser.getName().equals("kiosk"))
                        parseKioskConfiguration(); // TODO
                    else
                        throw new RepositoryException("unknown tag " + mParser.getName());
                default:                        
                    mParser.next();
            }
        }
        
    }
    
    private String parseTextElement()  throws RepositoryException, XmlPullParserException, IOException {        
        // Process the tags
        String oText = null;
        mParser.next();
        loop: while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (mParser.getEventType()) {
                case (XmlPullParser.TEXT):
                    oText = mParser.getText();        
                    if (mParser.getEventType() == XmlPullParser.END_TAG)
                        mParser.next();
                    break loop;
                default:
                    mParser.next();
            }
        }
        
        return oText;
    }
    
    private KioskConfiguration parseKioskConfiguration() throws RepositoryException, XmlPullParserException, IOException {
        // Process the attributes
        Boolean tAbstract = null;
        String tName = null;
        for (int i = 0; i < mParser.getAttributeCount(); i++) {
            String tAttributeName = mParser.getAttributeName(i);
            String tAttributeValue = mParser.getAttributeValue(i);
            
            if (tAttributeName.equals("abstract"))
                tAbstract = Boolean.parseBoolean(tAttributeValue);
            else if (tAttributeName.equals("name"))
                tName = tAttributeValue;
            else
                throw new RepositoryException("unknown attribute " + tAttributeName);
        }
        
        // Process the tags
        List<String> tInheritance = new ArrayList<String>();
        UUID tTarget = null;
        DeviceConfiguration tDeviceConfiguration = null;
        ApplicationConfiguration tApplicationConfiguration = null;
        mParser.next();
        loop: while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (mParser.getEventType()) {
                case (XmlPullParser.END_TAG):
                    mParser.next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    if (mParser.getName().equals("name"))
                        tName = parseTextElement();
                    else if (mParser.getName().equals("target"))
                        tTarget = UUID.fromString(parseTextElement());
                    else if (mParser.getName().equals("inherits"))
                        tInheritance.add(parseTextElement());
                    else if (mParser.getName().equals("application"))
                        tApplicationConfiguration = parseApplicationConfiguration();
                    else if (mParser.getName().equals("device"))
                        tDeviceConfiguration = parseDeviceConfiguration();
                    else
                        throw new RepositoryException("unknown tag " + mParser.getName());
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        KioskConfiguration tKioskConfiguration = new KioskConfiguration();
        tKioskConfiguration.setAbstract(tAbstract);
        tKioskConfiguration.setName(tName);
        tKioskConfiguration.setTarget(tTarget);
        // TODO: inherits
        tKioskConfiguration.setApplicationConfiguration(tApplicationConfiguration);
        tKioskConfiguration.setDeviceConfiguration(tDeviceConfiguration);
        return tKioskConfiguration;
    }
        
    
    private DeviceConfiguration parseDeviceConfiguration() throws RepositoryException, XmlPullParserException, IOException {
        // Process the tags
        DeviceConfiguration.Sound tSound = null;
        mParser.next();
        loop: while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (mParser.getEventType()) {
                case (XmlPullParser.END_TAG):
                    mParser.next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    if (mParser.getName().equals("sound"))
                        tSound = parseDeviceSound();
                    else
                        throw new RepositoryException("unknown tag " + mParser.getName());
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        DeviceConfiguration tDeviceConfiguration = new DeviceConfiguration();
        tDeviceConfiguration.setSound(tSound);
        return tDeviceConfiguration;
    }
    
    private ApplicationConfiguration parseApplicationConfiguration() throws RepositoryException, XmlPullParserException, IOException {
        // Process the tags
        ApplicationConfiguration.Interface tInterface = null;
        ApplicationConfiguration.Media tMedia = null;
        mParser.next();
        loop: while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (mParser.getEventType()) {
                case (XmlPullParser.END_TAG):
                    mParser.next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    if (mParser.getName().equals("interface"))
                        tInterface = parseApplicationInterface();
                    else if (mParser.getName().equals("media"))
                        tMedia = parseApplicationMedia();
                    else
                        throw new RepositoryException("unknown tag " + mParser.getName());
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        ApplicationConfiguration tApplicationConfiguration = new ApplicationConfiguration();
        tApplicationConfiguration.setMedia(tMedia);
        tApplicationConfiguration.setInterface(tInterface);
        return tApplicationConfiguration;
    }
}
