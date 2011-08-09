/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data;

import java.util.List;

/**
 *
 * @author tim
 */
public class RootItem extends TreeItem {
    //
    // Data members
    //
    
    private List<? extends Object> mHeaders;
    
    
    //
    // Construction and destruction
    //

    public RootItem(final List<? extends Object> iHeaders) {
        mHeaders = iHeaders;
    }
    
    
    //
    // Item implementation
    //

    @Override
    public final Object getField(final int iField) {
        return mHeaders.get(iField);
    }

    @Override
    public final int getFieldCount() {
        return mHeaders.size();
    }
    
    
    //
    // Auxiliary
    //
    
    public final SectionItem addSection(final String iName) {        
        SectionItem tSection = new SectionItem(iName, this);
        appendChild(tSection);
        return tSection;
    }    
}
