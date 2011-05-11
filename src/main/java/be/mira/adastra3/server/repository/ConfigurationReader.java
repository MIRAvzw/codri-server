/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.configurations.ApplicationConfiguration;
import be.mira.adastra3.server.repository.configurations.application.InterfaceConfiguration;
import be.mira.adastra3.server.repository.configurations.application.MediaConfiguration;
import be.mira.adastra3.server.repository.configurations.DeviceConfiguration;
import be.mira.adastra3.server.repository.configurations.KioskConfiguration;
import be.mira.adastra3.server.repository.configurations.device.SoundConfiguration;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;
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
            // Validate the file
            String schemaLang = "http://www.w3.org/2001/XMLSchema";
            SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
            Schema schema = factory.newSchema(this.getClass().getClassLoader().getResource("configuration.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(iStream));
            iStream.reset();
            
            // Setup the parser factory
            if (mParserFactory == null)
                mParserFactory = XmlPullParserFactory.newInstance();            
            mParserFactory.setNamespaceAware(true);
            mParserFactory.setValidating(false);
            
            // Aquire a parser
            mParser = mParserFactory.newPullParser();
            mParser.setInput(iStream, null);
        } catch (XmlPullParserException iException) {
            throw new RepositoryException(iException);
        } catch (SAXException iException) {
            throw new RepositoryException("could not validate file", iException);
        } catch (IOException iException) {
            throw new RepositoryException("could not open schema", iException);
        }
    }
    
    
    //
    // Public API
    //
    
    public void process() throws RepositoryException {
        try {
            // Setup parsing
            if (mParser.getEventType() != XmlPullParser.START_DOCUMENT)
                throw new RepositoryException("not at start of document");
            
            // Process tags
            mParser.next();
            while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (mParser.getEventType()) {
                    case (XmlPullParser.END_TAG):
                        mParser.next();
                        break;
                    case (XmlPullParser.START_TAG):
                        if (mParser.getName().equals("root"))
                            parseRoot();
                        else
                            throw new RepositoryException("unknown tag " + mParser.getName());
                    break;
                    default:                        
                        mParser.next();
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
                        Repository.getInstance().addConfiguration(parseKioskConfiguration());
                    else
                        throw new RepositoryException("unknown tag " + mParser.getName());
                    break;
                default:                        
                    mParser.next();
            }
        }
        
    }
    
    private String parseTextElement()  throws RepositoryException, XmlPullParserException, IOException {   
        // Parse the contents
        mParser.next();
        if (mParser.getEventType() != XmlPullParser.TEXT)
            throw new XmlPullParserException("asked to parse text where there is no text");
        String oText = oText = mParser.getText();
        
        // If there is an end tag after the text, skip it
        mParser.next();
        if (mParser.getEventType() == XmlPullParser.END_TAG)
            mParser.next();
        
        return oText;
    }
    
    private KioskConfiguration parseKioskConfiguration() throws RepositoryException, XmlPullParserException, IOException {
        // Process the attributes
        String tName = null;
        for (int i = 0; i < mParser.getAttributeCount(); i++) {
            String tAttributeName = mParser.getAttributeName(i);
            String tAttributeValue = mParser.getAttributeValue(i);
            
            if (tAttributeName.equals("name"))
                tName = tAttributeValue;
            else
                throw new RepositoryException("unknown attribute " + tAttributeName);
        }
        
        // Process the tags
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
                    else if (mParser.getName().equals("application"))
                        tApplicationConfiguration = parseApplicationConfiguration();
                    else if (mParser.getName().equals("device"))
                        tDeviceConfiguration = parseDeviceConfiguration();
                    else
                        throw new RepositoryException("unknown tag " + mParser.getName());
                    break;
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        KioskConfiguration tKioskConfiguration = new KioskConfiguration(tName);
        tKioskConfiguration.setTarget(tTarget);
        tKioskConfiguration.setApplicationConfiguration(tApplicationConfiguration);
        tKioskConfiguration.setDeviceConfiguration(tDeviceConfiguration);
        return tKioskConfiguration;
    }
        
    
    private DeviceConfiguration parseDeviceConfiguration() throws RepositoryException, XmlPullParserException, IOException {
        // Process the tags
        SoundConfiguration tSound = null;
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
                    break;
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        DeviceConfiguration tDeviceConfiguration = new DeviceConfiguration();
        tDeviceConfiguration.setSoundConfiguration(tSound);
        return tDeviceConfiguration;
    }
    
    private SoundConfiguration parseDeviceSound() throws RepositoryException, XmlPullParserException, IOException {
        // Process the tags
        Integer tVolume = null;
        mParser.next();
        loop: while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (mParser.getEventType()) {
                case (XmlPullParser.END_TAG):
                    mParser.next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    if (mParser.getName().equals("volume"))
                        tVolume = Integer.parseInt(parseTextElement());
                    else
                        throw new RepositoryException("unknown tag " + mParser.getName());
                    break;
                default:
                    mParser.next();
            }
        }
        
        // Create the obhect
        SoundConfiguration oSound = new SoundConfiguration();
        oSound.setVolume(tVolume);
        return oSound;
    }
    
    private ApplicationConfiguration parseApplicationConfiguration() throws RepositoryException, XmlPullParserException, IOException {
        // Process the tags
        InterfaceConfiguration tInterface = null;
        MediaConfiguration tMedia = null;
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
                    break;
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        ApplicationConfiguration tApplicationConfiguration = new ApplicationConfiguration();
        tApplicationConfiguration.setMediaConfiguration(tMedia);
        tApplicationConfiguration.setInterfaceConfiguration(tInterface);
        return tApplicationConfiguration;
    }
    
    private InterfaceConfiguration parseApplicationInterface() throws RepositoryException, XmlPullParserException, IOException {
        // Process the tags
        String tLocation = null;
        mParser.next();
        loop: while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (mParser.getEventType()) {
                case (XmlPullParser.END_TAG):
                    mParser.next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    if (mParser.getName().equals("location"))
                        tLocation = parseTextElement();
                    else
                        throw new RepositoryException("unknown tag " + mParser.getName());
                    break;
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        InterfaceConfiguration oApplicationInterface = new InterfaceConfiguration();
        oApplicationInterface.setLocation(tLocation);
        return oApplicationInterface;
    }
    
    private MediaConfiguration parseApplicationMedia() throws RepositoryException, XmlPullParserException, IOException {
        // Process the tags
        String tLocation = null;
        mParser.next();
        loop: while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (mParser.getEventType()) {
                case (XmlPullParser.END_TAG):
                    mParser.next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    if (mParser.getName().equals("location"))
                        tLocation = parseTextElement();
                    else
                        throw new RepositoryException("unknown tag " + mParser.getName());
                    break;
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        MediaConfiguration oApplicationMedia = new MediaConfiguration();
        oApplicationMedia.setLocation(tLocation);
        return oApplicationMedia;
    }
}
