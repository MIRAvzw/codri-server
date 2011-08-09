/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.auxiliary;

import eu.webtoolkit.jwt.Signal2;

/**
 *
 * @author tim
 */
public class Signal2Bubbler<A1, A2> implements Signal2.Listener<A1, A2> {
    private final Signal2<A1, A2> mBubble;

    public Signal2Bubbler(final Signal2<A1, A2> iBubble) {
        mBubble = iBubble;
    }

    @Override
    public void trigger(final A1 iArgument1, final A2 iArgument2) {
        mBubble.trigger(iArgument1, iArgument2);
    }
}