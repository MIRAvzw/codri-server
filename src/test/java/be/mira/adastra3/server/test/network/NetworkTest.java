/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.test.network;

import org.testng.Assert;
import org.testng.annotations.*;
import java.util.UUID;
import be.mira.adastra3.server.network.Network;
import be.mira.adastra3.server.network.NetworkEntity;
import be.mira.adastra3.server.exceptions.NetworkException;
import be.mira.adastra3.server.network.INetworkListener;

/**
 *
 * @author tim
 */
@Test
public class NetworkTest {
    //
    // Mem
    //
    
    private Network mNetwork;
    private DummyListener mNetworkListener;
    
    private NetworkEntity mDevice1;
    private NetworkEntity mDevice2;
    
    
    //
    // Private classes
    //
    
    private class DummyEntity extends NetworkEntity {
        public DummyEntity(String iName) {
            super(UUID.randomUUID());
            setName(iName);
            setMark();
        }
    }
    
    public enum MessageType {
        EntityAdded,
        EntityRemoved,
        NetworkError,
        NetworkWarning
    }
    
    private class DummyListener implements INetworkListener {
        public volatile MessageType mType;
        public volatile int mCount;
        public volatile NetworkEntity mDevice;
        public volatile String mMessage;
        public volatile NetworkException mException;
        
        public void reset() {
            mCount = 0;
        }

        @Override
        public void doEntityAdded(NetworkEntity iDevice) {
            mCount++;
            mType = MessageType.EntityAdded;
            mDevice = iDevice;
        }

        @Override
        public void doEntityRemoved(NetworkEntity iDevice) {
            mCount++;
            mType = MessageType.EntityRemoved;
            mDevice = iDevice;
        }

        @Override
        public void doNetworkError(String iMessage, NetworkException iException) {
            mCount++;
            mType = MessageType.NetworkError;
            mMessage = iMessage;
            mException = iException;
        }

        @Override
        public void doNetworkWarning(String iMessage) {
            mCount++;
            mType = MessageType.NetworkWarning;
            mMessage = iMessage;
        }
        
    }
    
    
    //
    // Set-up
    //
    
    @BeforeClass
    public void createNetworkEntities() {
        mDevice1 = new DummyEntity("Test device 1");
        mDevice2 = new DummyEntity("Test device 2");
        
        mNetworkListener = new DummyListener();
    }
    
    
    //
    // Tests
    //
    
    @BeforeMethod
    public void getNetworkInstance() {
        mNetwork = Network.getInstance();
        mNetwork.addListener(mNetworkListener);
    }
    
    @AfterMethod
    public void resetNetworkInstance() {
        mNetwork.removeListener(mNetworkListener);
        mNetworkListener.reset();
        mNetwork.reset();
    }
    
    @Test(groups="io.devices")
    public void addAndRemoveDevice() throws NetworkException {        
        mNetwork.addDevice(mDevice1);
        Assert.assertEquals(mNetworkListener.mCount, 1);
        Assert.assertEquals(mNetworkListener.mType, MessageType.EntityAdded);
        Assert.assertEquals(mNetworkListener.mDevice, mDevice1);
        
        mNetwork.removeDevice(mDevice1);
        Assert.assertEquals(mNetworkListener.mCount, 2);
        Assert.assertEquals(mNetworkListener.mType, MessageType.EntityRemoved);
        Assert.assertEquals(mNetworkListener.mDevice, mDevice1);
    }
    
    @Test(groups="io.devices", expectedExceptions = NetworkException.class)  
    public void addDuplicateDevice() throws NetworkException {        
        mNetwork.addDevice(mDevice1);
        mNetwork.addDevice(mDevice1);
    }
    
    @Test(groups="io.devices", expectedExceptions = NetworkException.class)  
    public void removeUnexistingDevice() throws NetworkException {
        mNetwork.removeDevice(mDevice1);
    }
    
    @Test(groups="io.devices")
    public void getDevices() throws NetworkException {
        mNetwork.addDevice(mDevice1);
        mNetwork.addDevice(mDevice2);
        Assert.assertEqualsNoOrder(mNetwork.getDevices().toArray(), new NetworkEntity[]{mDevice1, mDevice2});
    }
    
    @Test(groups="io.devices")
    public void getDeviceByUUID() throws NetworkException {
        mNetwork.addDevice(mDevice1);
        mNetwork.addDevice(mDevice2);
        Assert.assertEquals(mDevice1, mNetwork.getDevice(mDevice1.getUuid()));
        Assert.assertNotEquals(mDevice1, mNetwork.getDevice(mDevice2.getUuid()));
    }
    
    @Test(groups="io.messages")
    public void emitWarning() {
        String tMessage = "foobar";
        
        mNetwork.emitWarning(tMessage);
        Assert.assertEquals(mNetworkListener.mCount, 1);
        Assert.assertEquals(mNetworkListener.mType, MessageType.NetworkWarning);
        Assert.assertEquals(mNetworkListener.mMessage, tMessage);
    }
    
    @Test(groups="io.messages")
    public void emitError() {
        String tMessage = "foobar";
        NetworkException tException = new NetworkException();
        
        mNetwork.emitError(tMessage, tException);
        Assert.assertEquals(mNetworkListener.mCount, 1);
        Assert.assertEquals(mNetworkListener.mType, MessageType.NetworkError);
        Assert.assertEquals(mNetworkListener.mMessage, tMessage);
        Assert.assertEquals(mNetworkListener.mException, tException);
    }
}
