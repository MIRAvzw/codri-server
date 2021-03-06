/**
 * Copyright (C) 2011-2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */
package be.mira.codri.server.bo.repository.processors;

import be.mira.codri.server.exceptions.InvalidStateException;
import be.mira.codri.server.exceptions.RepositoryException;
import be.mira.codri.server.bo.repository.entities.Connection;
import be.mira.codri.server.spring.Slf4jLogger;
import java.io.IOException;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author tim
 */
public class ConnectionProcessor extends Processor<Connection> implements ApplicationContextAware {
    //
    // Member data
    //
    
    @Slf4jLogger
    private Logger mLogger;
    
    private ApplicationContext mApplicationContext;
    
    
    //
    // Construction and destruction
    //

    // FIXME: can't we instantiate prototype beans without being application context aware?
    @Override
    public final void setApplicationContext(final ApplicationContext iApplicationContext) {
        mApplicationContext = iApplicationContext;
    }
    
    
    //
    // Parsing functionality
    //
    
    @Override
    protected final Connection parseDocument(final long iRevision, final String iPath) throws RepositoryException {
        try {    
            Connection tConnection = null;
            
            // Setup parsing
            if (getParser().getEventType() != XMLStreamConstants.START_DOCUMENT
                    && getParser().getEventType() != XMLStreamConstants.START_ELEMENT) {
                throw new RepositoryException("not at start of document or element");
            }
            
            // Process tags
            getParser().next();
            loop: while (getParser().getEventType() != XMLStreamConstants.END_DOCUMENT) {
                switch (getParser().getEventType()) {
                    case (XMLStreamConstants.END_ELEMENT):
                        getParser().next();
                        break loop;
                    case (XMLStreamConstants.START_ELEMENT):
                        if (getTag().equals("connection")) {
                            tConnection = parseConnection(iRevision, iPath);
                        } else {
                            throw new InvalidStateException("inconsistency detected between validator and processor (unknown tag '" + getParser().getName() + "')");
                        }
                        break;
                    default:                        
                        getParser().next();
                }
            }
            
            return tConnection;
        } catch (XMLStreamException tException) {
            throw new RepositoryException(tException);
        } catch (IOException tException) {
            throw new RepositoryException(tException);
        }
    }
    
    private Connection parseConnection(final long iRevision, final String iPath) throws RepositoryException, XMLStreamException, IOException {       
        // Process the tags
        String tKiosk = null;
        String tConfiguration = null;
        String tPresentation = null;
        getParser().next();
        loop: while (getParser().getEventType() != XMLStreamConstants.END_DOCUMENT) {
            switch (getParser().getEventType()) {
                case (XMLStreamConstants.END_ELEMENT):
                    getParser().next();
                    break loop;
                case (XMLStreamConstants.START_ELEMENT):
                    if (getTag().equals("kiosk")) {
                        tKiosk = parseTextElement();
                    } else if (getTag().equals("configuration")) {
                        tConfiguration = parseTextElement();
                    } else if (getTag().equals("presentation")) {
                        tPresentation = parseTextElement();
                    } else {
                            throw new InvalidStateException("inconsistency detected between validator and processor (unknown tag '" + getParser().getName() + "')");
                    }
                    break;
                default:
                    getParser().next();
            }
        }
        
        // Create the object
        Connection tConnection = (Connection) mApplicationContext.getBean("connection", new Object[]{
                iRevision,
                iPath,
                tKiosk,
                tConfiguration,
                tPresentation});
        return tConnection;
    }
}
