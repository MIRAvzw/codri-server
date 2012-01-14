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
import java.util.Arrays;

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
    
    
    //
    // Set-up
    //
    
    @BeforeClass
    public void createNetworkEntities() {
        mDevice1 = new DummyEntity("Test device 1");
        mDevice2 = new DummyEntity("Test device 3");
    }
    
    
    //
    // Tests
    //
    
    @BeforeMethod
    public void getNetworkInstance() {
        mNetwork = Network.getInstance();
    }
    
    @AfterMethod
    public void resetNetworkInstance() {
        mNetwork.reset();
    }
    
    @Test(groups="io.devices")
    public void addAndRemoveDevice() throws NetworkException {        
        mNetwork.addDevice(mDevice1);
        mNetwork.removeDevice(mDevice1);
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
}
