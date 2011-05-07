/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.network;

/**
 *
 * @author tim
 */
public class Network {
    //
    // Member data
    //


    //
    // Static functionality
    //

    private static Network mInstance;

    public static Network getInstance() {
        if (mInstance == null)
            mInstance = new Network();
        return mInstance;
    }


    //
    // Construction and destruction
    //

    private Network() {
    }


    //
    // Getters and setters
    //
    
    public Kiosk getKiosk(String iName) {
        return null;
    }
    
}
