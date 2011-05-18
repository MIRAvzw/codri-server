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
    
    List<? extends Object> mHeaders;
    
    
    //
    // Construction and destruction
    //

    public RootItem(List<? extends Object> iHeaders) {
        mHeaders = iHeaders;
    }
    
    
    //
    // Item implementation
    //

    @Override
    public Object getField(int iField) {
        return mHeaders.get(iField);
    }

    @Override
    public int getFieldCount() {
        return mHeaders.size();
    }
    
    
    //
    // Auxiliary
    //
    
    public SectionItem addSection(String iName) {        
        SectionItem oSection = new SectionItem(iName, this);
        appendChild(oSection);
        return oSection;
    }
    
}
