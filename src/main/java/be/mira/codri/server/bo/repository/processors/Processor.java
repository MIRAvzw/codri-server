/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.server.bo.repository.processors;

import be.mira.codri.server.exceptions.RepositoryException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.stream.*;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 *
 * @author tim
 */
public abstract class Processor {
    //
    // Member data
    //
    
    private static XMLInputFactory PARSER_FACTORY;
    private final XMLStreamReader mParser;
            
            
    //
    // Construction and destruction
    //
    
    public Processor(final File iFile, final String iValidationFilename) throws RepositoryException {           
        // Setup the parser factory
        try {
            if (PARSER_FACTORY == null) {
                PARSER_FACTORY = XMLInputFactory.newInstance();   

                //PARSER_FACTORY.setNamespaceAware(true);
                //PARSER_FACTORY.setValidating(false);       
            }
        } catch (FactoryConfigurationError tException) {
            throw new RepositoryException("could not set-up the XML parser", tException);
        }
        
        // Parse the file
        try {    
            mParser = PARSER_FACTORY.createXMLStreamReader(new FileInputStream(iFile));
        } catch (XMLStreamException tException) {
            throw new RepositoryException("could not parse XML file", tException);
        } catch (FileNotFoundException tException) {
            throw new RepositoryException("could not open XML file", tException);
        }  
        
        // Validate the file
        // TODO: do this within the stream parser (use Stax2, http://stackoverflow.com/questions/5793087/stax-xml-validation)
        if (iValidationFilename != null) {
            try {
                String tSchemaLanguage = "http://www.w3.org/2001/XMLSchema";
                SchemaFactory tSchemaFactory = SchemaFactory.newInstance(tSchemaLanguage);
                Schema tSchema = tSchemaFactory.newSchema(this.getClass().getClassLoader().getResource(iValidationFilename));
                Validator tValidator = tSchema.newValidator();
                tValidator.validate(new StAXSource(mParser));
            } catch (SAXException tException) {
                throw new RepositoryException("could not validate file", tException);
            } catch (IOException tException) {
                throw new RepositoryException("could not open schema", tException);
            }
        }
    }
    
    
    //
    // Basic I/O
    //
    
    protected final XMLStreamReader getParser() {
        return mParser;
    }
    
    
    //
    // Parsing methods
    //    
    
    protected final String parseTextElement()  throws RepositoryException, XMLStreamException, IOException {   
        // Parse the contents
        mParser.next();
        if (mParser.getEventType() != XMLStreamConstants.CHARACTERS) {
            throw new XMLStreamException("asked to parse text where there is no text");
        }
        String tText = mParser.getText();
        
        // If there is an end tag after the text, skip it
        mParser.next();
        if (mParser.getEventType() == XMLStreamConstants.END_ELEMENT) {
            mParser.next();
        }
        
        return tText;
    }
    
}
