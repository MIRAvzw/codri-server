/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.repository;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.configurations.ApplicationConfiguration;
import be.mira.adastra3.server.repository.configurations.Configuration;
import be.mira.adastra3.server.repository.configurations.DeviceConfiguration;
import be.mira.adastra3.server.repository.configurations.KioskConfiguration;
import be.mira.adastra3.server.repository.configurations.application.MediaConfiguration;
import be.mira.adastra3.server.repository.configurations.device.SoundConfiguration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.log4j.Logger;
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
    
    private static XmlPullParserFactory PARSER_FACTORY;
    private XmlPullParser mParser;
    private Configuration mConfiguration;
    private String mIdentifier;
    private Logger mLogger = Logger.getLogger(ConfigurationReader.class);
    
    
    
    
    //
    // Construction and destruction
    //
    
    public ConfigurationReader(final String iIdentifier, final File iConfigurationFile) throws RepositoryException {
        mIdentifier = iIdentifier;
        
            // Validate the file
            // TODO: do this within the pull parser
        try {
            String tSchemaLanguage = "http://www.w3.org/2001/XMLSchema";
            SchemaFactory tSchemaFactory = SchemaFactory.newInstance(tSchemaLanguage);
            Schema tSchema = tSchemaFactory.newSchema(this.getClass().getClassLoader().getResource("configuration.xsd"));
            Validator tValidator = tSchema.newValidator();
            tValidator.validate(new StreamSource(iConfigurationFile));
        } catch (SAXException tException) {
            throw new RepositoryException("could not validate file", tException);
        } catch (IOException tException) {
            throw new RepositoryException("could not open schema", tException);
        }
        
        // Setup the parser factory
        try {
            if (PARSER_FACTORY == null) {
                PARSER_FACTORY = XmlPullParserFactory.newInstance();     

                PARSER_FACTORY.setNamespaceAware(true);
                PARSER_FACTORY.setValidating(false);       
            }
        } catch (XmlPullParserException tException) {
            throw new RepositoryException("could not set-up the pull parser", tException);
        }
        
        // Parse the file
        try {    
            mParser = PARSER_FACTORY.newPullParser();
            mParser.setInput(new FileInputStream(iConfigurationFile), null);
        } catch (XmlPullParserException tException) {
            throw new RepositoryException("could not parse configuration file", tException);
        } catch (FileNotFoundException tException) {
            throw new RepositoryException("could not open configuration file", tException);
        }
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
                        if (mParser.getName().equals("kiosk")) {
                            if (mConfiguration != null) {
                                throw new RepositoryException("configuration file contains multiple configurations");
                            }
                            mConfiguration = parseKioskConfiguration();
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
    
    private String parseTextElement()  throws RepositoryException, XmlPullParserException, IOException {   
        // Parse the contents
        mParser.next();
        if (mParser.getEventType() != XmlPullParser.TEXT) {
            throw new XmlPullParserException("asked to parse text where there is no text");
        }
        String tText = mParser.getText();
        
        // If there is an end tag after the text, skip it
        mParser.next();
        if (mParser.getEventType() == XmlPullParser.END_TAG) {
            mParser.next();
        }
        
        return tText;
    }
    
    private KioskConfiguration parseKioskConfiguration() throws RepositoryException, XmlPullParserException, IOException {        
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
                    if (mParser.getName().equals("target")) {
                        tTarget = UUID.fromString(parseTextElement());
                    } else if (mParser.getName().equals("application")) {
                        tApplicationConfiguration = parseApplicationConfiguration();
                    } else if (mParser.getName().equals("device")) {
                        tDeviceConfiguration = parseDeviceConfiguration();
                    }
                    break;
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        KioskConfiguration tKioskConfiguration = new KioskConfiguration(mIdentifier);
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
                    if (mParser.getName().equals("sound")) {
                        tSound = parseDeviceSound();
                    }
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
                    if (mParser.getName().equals("volume")) {
                        tVolume = Integer.parseInt(parseTextElement());
                    }
                    break;
                default:
                    mParser.next();
            }
        }
        
        // Create the obhect
        SoundConfiguration tSound = new SoundConfiguration();
        tSound.setVolume(tVolume);
        return tSound;
    }
    
    private ApplicationConfiguration parseApplicationConfiguration() throws RepositoryException, XmlPullParserException, IOException {
        // Process the tags
        MediaConfiguration tMediaConfiguration = null;
        mParser.next();
        loop: while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (mParser.getEventType()) {
                case (XmlPullParser.END_TAG):
                    mParser.next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    if (mParser.getName().equals("media")) {
                        tMediaConfiguration = parseApplicationMedia();
                    }
                    break;
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        ApplicationConfiguration tApplicationConfiguration = new ApplicationConfiguration();
        tApplicationConfiguration.setMediaConfiguration(tMediaConfiguration);
        return tApplicationConfiguration;
    }
    
    private MediaConfiguration parseApplicationMedia() throws RepositoryException, XmlPullParserException, IOException {
        // Process the attributes
        String tId = null;
        for (int tAttributeIndex = 0; tAttributeIndex < mParser.getAttributeCount(); tAttributeIndex++) {
            String tAttributeName = mParser.getAttributeName(tAttributeIndex);
            String tAttributeValue = mParser.getAttributeValue(tAttributeIndex);
            
            if (tAttributeName.equals("id")) {
                tId = tAttributeValue;
            }
        }
        
        // Process the tags
        mParser.next();
        loop: while (mParser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (mParser.getEventType()) {
                case (XmlPullParser.END_TAG):
                    mParser.next();
                    break loop;
                case (XmlPullParser.START_TAG):
                    break;
                default:
                    mParser.next();
            }
        }
        
        // Create the object
        MediaConfiguration tApplicationMedia = new MediaConfiguration(tId);
        return tApplicationMedia;
    }
}
