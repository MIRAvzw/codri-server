/*
 * Copyright (C) 2011 Teleal GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package be.mira.adastra3.server.network.cling;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.Inet6Address;
import org.teleal.cling.transport.spi.InitializationException;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.teleal.cling.transport.impl.NetworkAddressFactoryImpl;

/**
 * Default implementation of network interface and address configuration/discovery.
 * <p
 * This implementation has been tested on Windows XP, Windows Vista, Mac OS X 10.6,
 * and whatever kernel ships in Ubuntu 9.04. This implementation does not support IPv6.
 * </p>
 *
 * @author Christian Bauer
 */
public class CompatNetworkAddressFactory extends NetworkAddressFactoryImpl {
    // Patterns
    private final static Pattern mAddressInet4 = Pattern.compile("inet addr:([\\d\\.]+) ");
    private final static Pattern mAddressBroadcast = Pattern.compile("Bcast:(\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3})");
    private final static Pattern mAddressMask = Pattern.compile("Mask:(\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3})");
    private final static Pattern mAddressInet6 = Pattern.compile("inet6 addr: ([\\p{XDigit}:]+)\\/(\\d+)");
    
    List<InterfaceAddress> mInterfaceAddresses = null;
    
    private Logger mLogger = Logger.getLogger(CompatNetworkAddressFactory.class);

    public CompatNetworkAddressFactory() throws InitializationException {
        super();
    }

    public CompatNetworkAddressFactory(int streamListenPort) throws InitializationException {
        super(streamListenPort);
    }

    @Override
    public List<InterfaceAddress> getInterfaceAddresses(NetworkInterface iInterface) {
        if (mInterfaceAddresses == null) {
            mInterfaceAddresses = new ArrayList<InterfaceAddress>();
            mLogger.warn("Extremely expensive call to compatible getInterfaceAddresses");

            // Set-up objects
            Inet4Address tInet4Address = null, tInet4Broadcast = null, tInet4Netmask = null;
            Inet6Address tInet6Address = null;
            short tInet6Prefixlength = 64;

            // Process ifconfig output
            Runtime r = Runtime.getRuntime();
            try {
                // Execute
                Process tProcess = r.exec("ifconfig " + iInterface.getName());
                InputStream tProcessStream = tProcess.getInputStream();
                BufferedInputStream tProcessBufferedStream = new BufferedInputStream(tProcessStream);
                InputStreamReader tProcessReader = new InputStreamReader(tProcessBufferedStream);
                BufferedReader tProcessBufferedReadet = new BufferedReader(tProcessReader);

                // Read
                String tLine;
                while ((tLine = tProcessBufferedReadet.readLine()) != null) {
                    Matcher tAddressInet4 = mAddressInet4.matcher(tLine);
                    if (tAddressInet4.find()) {
                        tInet4Address = (Inet4Address) InetAddress.getByName(tAddressInet4.group(1));;
                    }
                    Matcher tAddressBroadcast = mAddressBroadcast.matcher(tLine);
                    if (tAddressBroadcast.find()) {
                        tInet4Broadcast = (Inet4Address) InetAddress.getByName(tAddressBroadcast.group(1));
                    }
                    Matcher tAddressMask = mAddressMask.matcher(tLine);
                    if (tAddressMask.find()) {
                        tInet4Netmask = (Inet4Address) InetAddress.getByName(tAddressMask.group(1));
                    }
                    Matcher tAddressInet6 = mAddressInet6.matcher(tLine);
                    if (tAddressInet6.find()) {
                        tInet6Address = (Inet6Address) InetAddress.getByName(tAddressInet6.group(1));
                        tInet6Prefixlength = Short.parseShort(tAddressInet6.group(2));
                    }
                }

                // Check the return value
                try {
                    if (tProcess.waitFor() != 0) {
                        mLogger.warn("ifconfig didn't return success");
                        return null;
                    }
                } catch (InterruptedException tException) {
                        mLogger.warn("Call to ifconfig got interrupted", tException);
                    return null;
                } finally {
                    // Close streams
                    tProcessBufferedReadet.close();
                    tProcessReader.close();
                    tProcessBufferedStream.close();
                    tProcessStream.close();
                }
            } catch (IOException tException) {
                mLogger.warn("Could not get a hook to the runtime environment", tException);
                return null;
            }

            // Construct some objects
            if (tInet6Address != null) {
                InterfaceAddress tInet6 = new InterfaceAddress(tInet6Address, tInet6Prefixlength);
                mInterfaceAddresses.add(tInet6);
            }
            if (tInet4Address != null) {
                InterfaceAddress tInet4 = new InterfaceAddress(tInet4Address, tInet4Broadcast, tInet4Netmask);
                mInterfaceAddresses.add(tInet4);
            }
        }

        mLogger.warn("Using cached result for extremely expensive call to compatible getInterfaceAddresses");
        return mInterfaceAddresses;
    }

    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        System.out.printf("Parent Info:%s\n", netint.getParent());
        System.out.printf("Display name: %s\n", netint.getDisplayName());
        System.out.printf("Name: %s\n", netint.getName());

        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();

        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            System.out.printf("InetAddress: %s\n", inetAddress);
        }

        List<InterfaceAddress> ias = netint.getInterfaceAddresses();

        Iterator<InterfaceAddress> iias = ias.iterator();
        while (iias.hasNext()) {
            InterfaceAddress ia = iias.next();

            System.out.println(" Interface Address");
            System.out.println("  Address: " + ia.getAddress());
            System.out.println("  Broadcast: " + ia.getBroadcast());
            System.out.println("  Prefix length: " + ia.getNetworkPrefixLength());
        }

        Enumeration<NetworkInterface> subIfs = netint.getSubInterfaces();

        for (NetworkInterface subIf : Collections.list(subIfs)) {
            System.out.printf("\tSub Interface Display name: %s\n", subIf.getDisplayName());
            System.out.printf("\tSub Interface Name: %s\n", subIf.getName());
        }
        System.out.printf("Up? %s\n", netint.isUp());
        System.out.printf("Loopback? %s\n", netint.isLoopback());
        System.out.printf("PointToPoint? %s\n", netint.isPointToPoint());
        System.out.printf("Supports multicast? %s\n", netint.supportsMulticast());
        System.out.printf("Virtual? %s\n", netint.isVirtual());
        System.out.printf("Hardware address: %s\n", Arrays.toString(netint.getHardwareAddress()));
        System.out.printf("MTU: %s\n", netint.getMTU());
        System.out.printf("\n");

    }
    
    @Override
    public byte[] getHardwareAddress(InetAddress inetAddress) {
        return null;
    }
}
