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
    
    private TreeItem mParent;
    private List<TreeItem> mChildren;
    
    
    //
    // Construction and destruction
    //
    
    public TreeItem() {
        this(null);
    }
    
    public TreeItem(final TreeItem iParent) {
        mParent = iParent;
        mChildren = new ArrayList<TreeItem>();
    }
    
    
    //
    // Item interface
    //
    
    public final int getRow() {
        if (mParent != null) {
            return mParent.mChildren.indexOf(this);
        }
        return 0;
    }
    
    public final TreeItem getParent() {
        return mParent;
    }
    
    public final TreeItem getChild(final int iRow) {
        return mChildren.get(iRow);
    }
    
    public final int getChildCount() {
        return mChildren.size();
    }
    
    public final void appendChild(final TreeItem iChild) {
        mChildren.add(iChild);
    }
    
    public final boolean insertChildren(final int iPosition, final List<? extends TreeItem> iChildren) {
        if (iPosition < 0 || iPosition > mChildren.size()) {
            return false;
        }
        
        mChildren.addAll(iPosition, iChildren);
        return true;     
    }
    
    public final boolean removeChildren(final int iPosition, final int iCount) {
        if (iPosition < 0 || iPosition + iCount > mChildren.size()) {
            return false;
        }
        
        for (int tRow = 0; tRow < iCount; ++tRow) {
            mChildren.remove(iPosition);
        }
        return true;
    }
    
    public abstract Object getField(int iField);
    
    public abstract int getFieldCount();
}
