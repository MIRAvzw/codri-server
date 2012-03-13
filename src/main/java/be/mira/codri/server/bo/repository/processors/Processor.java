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
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.validation.XMLValidationSchema;
import org.codehaus.stax2.validation.XMLValidationSchemaFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author tim
 */
public abstract class Processor<T> {
    //
    // Member data
    //
    
    private String mValidationFilename;
    
    private static XMLInputFactory2 PARSER_FACTORY;
    private XMLStreamReader2 mParser;
    private static XMLValidationSchemaFactory VALIDATOR_FACTORY;
    private XMLValidationSchema mValidator;
            
            
    //
    // Construction and destruction
    //
    
    @Required
    public void setValidationFilename(final String iValidationFilename) {
        mValidationFilename = iValidationFilename;
    }
    
    
    //
    // Public API
    //
    
    public final T process(final File iFile, final long iRevision, final String iPath) throws RepositoryException {
        // Create a validator
        try {           
            // Acquire the validator factory
            if (VALIDATOR_FACTORY == null) {
                VALIDATOR_FACTORY = XMLValidationSchemaFactory.newInstance(XMLValidationSchema.SCHEMA_ID_W3C_SCHEMA);
            }
            
            // Create a schema instance
            mValidator = VALIDATOR_FACTORY.createSchema(this.getClass().getClassLoader().getResource(mValidationFilename));
        } catch (FactoryConfigurationError tException) {
            throw new RepositoryException("could not acquire schema validator factory", tException);
        } catch (XMLStreamException tException) {
            throw new RepositoryException("could not create schema validator", tException);
        }
        
        // Create a parser
        try {
            // Acquire the parser factory
            if (PARSER_FACTORY == null) {
                PARSER_FACTORY = (XMLInputFactory2) XMLInputFactory2.newInstance(); 
            }
            
            // Create a parser
            mParser = (XMLStreamReader2) PARSER_FACTORY.createXMLStreamReader(new FileInputStream(iFile));
            mParser.validateAgainst(mValidator);
        } catch(FactoryConfigurationError tException) {
            throw new RepositoryException("could not acquire XML parser factory", tException);
        } catch (XMLStreamException tException) {
            throw new RepositoryException("could not create XML parser", tException);
        } catch (FileNotFoundException tException) {
            throw new RepositoryException("could not open XML file", tException);
        }
        
        return parseDocument(iRevision, iPath);
    }
    
    protected abstract T parseDocument(final long iRevision, final String iPath) throws RepositoryException;
    
    
    //
    // Basic I/O
    //
    
    protected final XMLStreamReader2 getParser() {
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
    
    
    //
    // Auxiliary
    //
    
    public String getTag() {
        return getParser().getName().getLocalPart();
    }
    
}
