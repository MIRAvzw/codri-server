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
public abstract class TreeItem {
    //
    // Data members
    //
    
    TreeItem mParent;
    List<TreeItem> mChildren;
    
    
    //
    // Construction and destruction
    //
    
    public TreeItem() {
        this(null);
    }
    
    public TreeItem(TreeItem iParent) {
        mParent = iParent;
        mChildren = new ArrayList<TreeItem>();
    }
    
    
    //
    // Item interface
    //
    
    void appendChild(TreeItem iChild) {
        mChildren.add(iChild);
    }
    
    public int getChildCount() {
        return mChildren.size();
    }
    
    public abstract int getFieldCount();
    
    public abstract Object getField(int iField);
    
    public int getRow() {
        if (mParent != null)
            return mParent.mChildren.indexOf(this);
        return 0;
    }
    
    public TreeItem getParent() {
        return mParent;
    }
    
    public TreeItem getChild(int row) {
        return mChildren.get(row);
    }
}
