/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.test.repository;

import be.mira.adastra3.server.exceptions.RepositoryException;
import be.mira.adastra3.server.repository.IRepositoryListener;
import be.mira.adastra3.server.repository.Repository;
import be.mira.adastra3.server.repository.RepositoryEntity;
import be.mira.adastra3.server.repository.configuration.Configuration;
import be.mira.adastra3.server.repository.connection.Connection;
import be.mira.adastra3.server.repository.presentation.Presentation;
import java.util.UUID;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 *
 * @author tim
 */
@Test
public class RepositoryTest {
    //
    // Member data
    //
    
    private Repository mRepository;
    private RepositoryTest.DummyListener mRepositoryListener;
    
    private Configuration mConfiguration1, mConfiguration2;
    private Presentation mPresentation1, mPresentation2;
    private Connection mConnection1, mConnection2;
    
    
    //
    // Private classes
    //
    
    private class DummyEntity extends RepositoryEntity {
        public DummyEntity(String iName, String iPath) {
            super(iName, 0, iPath);
        }
    }
    
    public enum MessageType {
        Warning,
        Error,
        ConfigurationAdded,
        ConfigurationUpdated,
        ConfigurationRemoved,
        PresentationAdded,
        PresentationUpdated,
        PresentationRemoved,
        ConnectionAdded,
        ConnectionUpdated,
        ConnectionRemoved
    }
    
    private class DummyListener implements IRepositoryListener {
        public volatile RepositoryTest.MessageType mType;
        public volatile int mCount;
        public volatile String mMessage;
        public volatile RepositoryException mException;
        public volatile Configuration mConfiguration, mOldConfiguration;
        public volatile Presentation mPresentation, mOldPresentation;
        public volatile Connection mConnection, mOldConnection;
        
        public void reset() {
            mCount = 0;
        }

        @Override
        public void doRepositoryWarning(String iMessage) {
            mCount++;
            mType = MessageType.Warning;
            mMessage = iMessage;
        }

        @Override
        public void doRepositoryError(String iMessage, RepositoryException iException) {
            mCount++;
            mType = MessageType.Error;
            mMessage = iMessage;
            mException = iException;
        }

        @Override
        public void doConfigurationAdded(Configuration iConfiguration) {
            mCount++;
            mType = MessageType.ConfigurationAdded;
            mConfiguration = iConfiguration;
        }

        @Override
        public void doConfigurationRemoved(Configuration iConfiguration) {
            mCount++;
            mType = MessageType.ConfigurationRemoved;
            mConfiguration = iConfiguration;
        }

        @Override
        public void doConfigurationUpdated(Configuration iOldConfiguration, Configuration iConfiguration) {
            mCount++;
            mType = MessageType.ConfigurationUpdated;
            mOldConfiguration = iOldConfiguration;
            mConfiguration = iConfiguration;
        }

        @Override
        public void doConnectionAdded(Connection iConnection) {
            mCount++;
            mType = MessageType.ConnectionAdded;
            mConnection = iConnection;
        }

        @Override
        public void doConnectionRemoved(Connection iConnection) {
            mCount++;
            mType = MessageType.ConnectionRemoved;
            mConnection = iConnection;
        }

        @Override
        public void doConnectionUpdated(Connection iOldConnection, Connection iConnection) {
            mCount++;
            mType = MessageType.ConnectionUpdated;
            mOldConnection = iOldConnection;
            mConnection = iConnection;
        }

        @Override
        public void doPresentationAdded(Presentation iPresentation) {
            mCount++;
            mType = MessageType.PresentationAdded;
            mPresentation = iPresentation;
        }

        @Override
        public void doPresentationRemoved(Presentation iPresentation) {
            mCount++;
            mType = MessageType.PresentationRemoved;
            mPresentation = iPresentation;
        }

        @Override
        public void doPresentationUpdated(Presentation iOldPresentation, Presentation iPresentation) {
            mCount++;
            mType = MessageType.PresentationUpdated;
            mPresentation = iPresentation;
            mOldPresentation = iOldPresentation;
        }
    }
    
    
    //
    // Set-up
    //
    
    @BeforeClass
    public void createRepositoryEntities() {
        mConfiguration1 = new Configuration("Test configuration 1", 1, "/configuration1", null);
        mConfiguration2 = new Configuration("Test configuration 2", 1, "/configuration2", null);
        mPresentation1 = new Presentation("Test presentation 1", 1, "/presentation1");
        mPresentation2 = new Presentation("Test presentation 2", 1, "/presentation2");
        mConnection1 = new Connection("Test connection 1", 1, "/connection1", UUID.randomUUID(), "Test configuration 1", "Test presentation 1");
        mConnection2 = new Connection("Test connection 2", 1, "/connection2", UUID.randomUUID(), "Test configuration 2", "Test presentation 2");
        
        mRepositoryListener = new DummyListener();
    }
    
    
    //
    // Tests
    //
    
    @BeforeMethod
    public void getRepositoryInstance() {
        mRepository = Repository.getInstance();
        mRepository.addListener(mRepositoryListener);
    }
    
    @AfterMethod
    public void resetRepositoryInstance() {
        mRepository.removeListener(mRepositoryListener);
        mRepositoryListener.reset();
        mRepository.reset();
    }
    
    @Test(groups = "io.server")
    public void getAndSetServer() {
        String tServer = "foobar";
        mRepository.setServer(tServer);
        Assert.assertEquals(mRepository.getServer(), tServer);
    }
    
    @Test(groups="io.configurations")
    public void addAndRemoveConfiguration() throws RepositoryException {        
        mRepository.addConfiguration(mConfiguration1);
        Assert.assertEquals(mRepositoryListener.mCount, 1);
        Assert.assertEquals(mRepositoryListener.mType, MessageType.ConfigurationAdded);
        Assert.assertEquals(mRepositoryListener.mConfiguration, mConfiguration1);
        
        mRepository.removeConfiguration(mConfiguration1);
        Assert.assertEquals(mRepositoryListener.mCount, 2);
        Assert.assertEquals(mRepositoryListener.mType, MessageType.ConfigurationRemoved);
        Assert.assertEquals(mRepositoryListener.mConfiguration, mConfiguration1);
    }
    
    @Test(groups="io.configurations", expectedExceptions = RepositoryException.class)  
    public void addDuplicateConfiguration() throws RepositoryException {        
        mRepository.addConfiguration(mConfiguration1);
        mRepository.addConfiguration(mConfiguration1);
    }
    
    @Test(groups="io.configurations", expectedExceptions = RepositoryException.class)  
    public void removeUnexistingConfiguration() throws RepositoryException {
        mRepository.removeConfiguration(mConfiguration1);
    }
    
    @Test(groups="io.configurations")
    public void getConfigurations() throws RepositoryException {
        mRepository.addConfiguration(mConfiguration1);
        mRepository.addConfiguration(mConfiguration2);
        Assert.assertEqualsNoOrder(mRepository.getConfigurations().values().toArray(), new Configuration[]{mConfiguration1, mConfiguration2});
    }
    
    @Test(groups="io.configurations")
    public void getConfigurationByUUID() throws RepositoryException {
        mRepository.addConfiguration(mConfiguration1);
        mRepository.addConfiguration(mConfiguration2);
        Assert.assertEquals(mConfiguration1, mRepository.getConfiguration(mConfiguration1.getId()));
        Assert.assertNotEquals(mConfiguration1, mRepository.getConfiguration(mConfiguration2.getId()));
    }
    
    @Test(groups="io.presentations")
    public void addAndRemovePresentation() throws RepositoryException {        
        mRepository.addPresentation(mPresentation1);
        Assert.assertEquals(mRepositoryListener.mCount, 1);
        Assert.assertEquals(mRepositoryListener.mType, MessageType.PresentationAdded);
        Assert.assertEquals(mRepositoryListener.mPresentation, mPresentation1);
        
        mRepository.removePresentation(mPresentation1);
        Assert.assertEquals(mRepositoryListener.mCount, 2);
        Assert.assertEquals(mRepositoryListener.mType, MessageType.PresentationRemoved);
        Assert.assertEquals(mRepositoryListener.mPresentation, mPresentation1);
    }
    
    @Test(groups="io.presentations", expectedExceptions = RepositoryException.class)  
    public void addDuplicatePresentation() throws RepositoryException {        
        mRepository.addPresentation(mPresentation1);
        mRepository.addPresentation(mPresentation1);
    }
    
    @Test(groups="io.presentations", expectedExceptions = RepositoryException.class)  
    public void removeUnexistingPresentation() throws RepositoryException {
        mRepository.removePresentation(mPresentation1);
    }
    
    @Test(groups="io.presentations")
    public void getPresentations() throws RepositoryException {
        mRepository.addPresentation(mPresentation1);
        mRepository.addPresentation(mPresentation2);
        Assert.assertEqualsNoOrder(mRepository.getPresentations().values().toArray(), new Presentation[]{mPresentation1, mPresentation2});
    }
    
    @Test(groups="io.presentations")
    public void getPresentationByUUID() throws RepositoryException {
        mRepository.addPresentation(mPresentation1);
        mRepository.addPresentation(mPresentation2);
        Assert.assertEquals(mPresentation1, mRepository.getPresentation(mPresentation1.getId()));
        Assert.assertNotEquals(mPresentation1, mRepository.getPresentation(mPresentation2.getId()));
    }
    
    @Test(groups="io.connections")
    public void addAndRemoveConnection() throws RepositoryException {        
        mRepository.addConnection(mConnection1);
        Assert.assertEquals(mRepositoryListener.mCount, 1);
        Assert.assertEquals(mRepositoryListener.mType, MessageType.ConnectionAdded);
        Assert.assertEquals(mRepositoryListener.mConnection, mConnection1);
        
        mRepository.removeConnection(mConnection1);
        Assert.assertEquals(mRepositoryListener.mCount, 2);
        Assert.assertEquals(mRepositoryListener.mType, MessageType.ConnectionRemoved);
        Assert.assertEquals(mRepositoryListener.mConnection, mConnection1);
    }
    
    @Test(groups="io.connections", expectedExceptions = RepositoryException.class)  
    public void addDuplicateConnection() throws RepositoryException {        
        mRepository.addConnection(mConnection1);
        mRepository.addConnection(mConnection1);
    }
    
    @Test(groups="io.connections", expectedExceptions = RepositoryException.class)  
    public void removeUnexistingConnection() throws RepositoryException {
        mRepository.removeConnection(mConnection1);
    }
    
    @Test(groups="io.connections")
    public void getConnections() throws RepositoryException {
        mRepository.addConnection(mConnection1);
        mRepository.addConnection(mConnection2);
        Assert.assertEqualsNoOrder(mRepository.getConnections().values().toArray(), new Connection[]{mConnection1, mConnection2});
    }
    
    @Test(groups="io.connections")
    public void getConnectionByUUID() throws RepositoryException {
        mRepository.addConnection(mConnection1);
        mRepository.addConnection(mConnection2);
        Assert.assertEquals(mConnection1, mRepository.getConnection(mConnection1.getId()));
        Assert.assertNotEquals(mConnection1, mRepository.getConnection(mConnection2.getId()));
    }
    
    @Test(groups="io.messages")
    public void emitWarning() {
        String tMessage = "foobar";
        
        mRepository.emitWarning(tMessage);
        Assert.assertEquals(mRepositoryListener.mCount, 1);
        Assert.assertEquals(mRepositoryListener.mType, RepositoryTest.MessageType.Warning);
        Assert.assertEquals(mRepositoryListener.mMessage, tMessage);
    }
    
    @Test(groups="io.messages")
    public void emitError() {
        String tMessage = "foobar";
        RepositoryException tException = new RepositoryException();
        
        mRepository.emitError(tMessage, tException);
        Assert.assertEquals(mRepositoryListener.mCount, 1);
        Assert.assertEquals(mRepositoryListener.mType, RepositoryTest.MessageType.Error);
        Assert.assertEquals(mRepositoryListener.mMessage, tMessage);
        Assert.assertEquals(mRepositoryListener.mException, tException);
    }
}
