/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tim
 */
public interface DeferredExecution {
    List<DeferredExecution> cDeferrees = new ArrayList<DeferredExecution>();
    
    void execute();
}
