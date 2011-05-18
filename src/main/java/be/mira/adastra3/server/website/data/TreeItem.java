/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
    
    public int getRow() {
        if (mParent != null)
            return mParent.mChildren.indexOf(this);
        return 0;
    }
    
    public TreeItem getParent() {
        return mParent;
    }
    
    public TreeItem getChild(int iRow) {
        return mChildren.get(iRow);
    }
    
    public int getChildCount() {
        return mChildren.size();
    }
    
    void appendChild(TreeItem iChild) {
        mChildren.add(iChild);
    }
    
    boolean insertChildren(int position, List<? extends TreeItem> iChildren)            
    {
        if (position < 0 || position > mChildren.size())
            return false;
        
        mChildren.addAll(position, iChildren);
        return true;     
    }
    
    boolean removeChildren(int position, int count)
    {
        if (position < 0 || position + count > mChildren.size())
            return false;
        
        for (int row = 0; row < count; ++row)
            mChildren.remove(position);
        return true;
    }
    
    public abstract Object getField(int iField);
    
    public abstract int getFieldCount();
}
