/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.adastra3.server.repository.processors;

import be.mira.adastra3.server.exceptions.RepositoryException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tim
 */
public class Processor {
    //
    // Member data
    //
    
    private Logger mLogger;
    
    protected static XmlPullParserFactory PARSER_FACTORY;
    protected XmlPullParser mParser;
            
            
    //
    // Construction and destruction
    //
    
    public Processor(final File iFile, final String iValidationFilename) throws RepositoryException { 
        // FIXME: acquire a configured logger properly. Bean?
        mLogger = LoggerFactory.getLogger(this.getClass());
        
        // Validate the file
        // TODO: do this within the pull parser
        if (iValidationFilename != null) {
            try {
                String tSchemaLanguage = "http://www.w3.org/2001/XMLSchema";
                SchemaFactory tSchemaFactory = SchemaFactory.newInstance(tSchemaLanguage);
                Schema tSchema = tSchemaFactory.newSchema(this.getClass().getClassLoader().getResource(iValidationFilename));
                Validator tValidator = tSchema.newValidator();
                tValidator.validate(new StreamSource(iFile));
            } catch (SAXException tException) {
                throw new RepositoryException("could not validate file", tException);
            } catch (IOException tException) {
                throw new RepositoryException("could not open schema", tException);
            }
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
            mParser.setInput(new FileInputStream(iFile), null);
        } catch (XmlPullParserException tException) {
            throw new RepositoryException("could not parse configuration file", tException);
        } catch (FileNotFoundException tException) {
            throw new RepositoryException("could not open configuration file", tException);
        }
    }
    
    
    //
    // Parsing methods
    //    
    
    protected final String parseTextElement()  throws RepositoryException, XmlPullParserException, IOException {   
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
    
}
