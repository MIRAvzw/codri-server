/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.mira.adastra3.common;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tim
 */
public class Machine {
    //
    // Enums and classes
    //


    public enum State {
        ONLINE,
        OFFLINE,
        UNKNOWN
    }


    //
    // Member data
    //

    private final String mName;
    private List<InetAddress> mInetAddresses;
    private State mState;


    //
    // Construction and destruction
    //

    public Machine(String iName) {
        mInetAddresses = new ArrayList<InetAddress>();
        mState = State.UNKNOWN;
        mName = iName;
    }


    //
    // Getters and setters
    //

    public String getName() {
        return mName;
    }

    public List<InetAddress> getInetAddresses() {
        return mInetAddresses;
    }

    public void addInetAddress(InetAddress iInetAddress) {
        mInetAddresses.add(iInetAddress);
    }

    public void clearInetAddresses() {
        mInetAddresses.clear();
    }

    public State getState() {
        return mState;
    }

    public void setState(State iState) {
        mState = iState;
    }
}
