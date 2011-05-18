/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mira.adastra3.server.website.data;

import eu.webtoolkit.jwt.ItemDataRole;
import eu.webtoolkit.jwt.Orientation;
import eu.webtoolkit.jwt.WAbstractItemModel;
import eu.webtoolkit.jwt.WModelIndex;
import java.util.List;

/**
 *
 * @author tim
 */
public class TreeModel<E extends TreeItem> extends WAbstractItemModel {
    //
    // Data members
    //
    
    private RootItem mRoot;
    
    
    //
    // Construction and destruction
    //
    
    public TreeModel(List<String> iHeaders) {
        // Set-up tree root
        mRoot = new RootItem(iHeaders);
    }
    
    
    //
    // Getters and setters
    //
    
    public RootItem getRoot() {
        return mRoot;
    }
    
    
    //
    // Model implementation
    //

    @Override
    public int getColumnCount(WModelIndex parent) {
        if (parent != null) {
            return ((TreeItem) (parent.getInternalPointer())).getFieldCount();
        } else {
            return mRoot.getFieldCount();
        }
    }

    @Override
    public Object getData(WModelIndex index, int role) {
        if (index == null) {
            return null;
        }

        if (role != ItemDataRole.DisplayRole) {
            return null;
        }

        TreeItem item = (TreeItem) (index.getInternalPointer());

        return item.getField(index.getColumn());
    }

    @Override
    public WModelIndex getIndex(int row, int column, WModelIndex parent) {
        if (! this.hasIndex(row, column, parent))
            return null;
        
        // Get the actual parent
        TreeItem tParent;
        if (parent != null)
            tParent = (TreeItem) parent.getInternalPointer();
        else
            tParent = mRoot;
        
        // Get the child
        TreeItem tChild = tParent.getChild(row);
        if (tChild != null)
            return createIndex(row, column, tChild);
        else
            return null;
    }

    @Override
    public WModelIndex getParent(WModelIndex index) {
        if (index == null) {
            return null;
        }

        TreeItem childItem = (TreeItem) index.getInternalPointer();
        TreeItem parentItem = childItem.getParent();

        if (parentItem == mRoot) {
            return null;
        }

        return createIndex(parentItem.getRow(), 0, parentItem);
    }

    @Override
    public int getRowCount(WModelIndex parent) {
        if (parent != null && parent.getColumn() > 0) {
            return 0;
        }

        TreeItem parentItem;
        if (parent == null)
            parentItem = mRoot;
        else
            parentItem = (TreeItem) (parent.getInternalPointer());

        return parentItem.getChildCount();
    }

    @Override
    public Object getHeaderData(int section, Orientation orientation, int role) {
        if (orientation == Orientation.Horizontal && role == ItemDataRole.DisplayRole)
            return mRoot.getField(section);
        return null;
    }
    
    
}
