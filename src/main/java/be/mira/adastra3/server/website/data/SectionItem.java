/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data;

/**
 *
 * @author tim
 */
public class SectionItem extends TreeItem {
    //
    // Data members
    //
    
    private String mName;
    
    
    //
    // Construction and destruction
    //
    
    public SectionItem(final String iName, final TreeItem iParent) {
        super(iParent);
        mName = iName;
    }
    
    
    //
    // Item implementation
    //

    @Override
    public final Object getField(final int iField) {
        if (iField == 0) {
            return mName;
        }
        return null;
    }

    @Override
    public final int getFieldCount() {
        return 1;
    }
    
}
