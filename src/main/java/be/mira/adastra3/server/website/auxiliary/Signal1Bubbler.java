/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.auxiliary;

import eu.webtoolkit.jwt.Signal1;

/**
 *
 * @author tim
 */
public final class Signal1Bubbler<A1> implements Signal1.Listener<A1> {
    private final Signal1<A1> mBubble;

    public Signal1Bubbler(final Signal1<A1> iBubble) {
        mBubble = iBubble;
    }

    @Override
    public void trigger(final A1 iArgument) {
        mBubble.trigger(iArgument);
    }
}