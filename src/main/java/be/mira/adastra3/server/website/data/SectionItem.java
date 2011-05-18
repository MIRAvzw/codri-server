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
    
    public SectionItem(String iName, TreeItem iParent) {
        super(iParent);
        mName = iName;
    }
    
    
    //
    // Item implementation
    //

    @Override
    public Object getField(int iField) {
        if (iField == 0)
            return mName;
        return null;
    }

    @Override
    public int getFieldCount() {
        return 1;
    }
    
}
