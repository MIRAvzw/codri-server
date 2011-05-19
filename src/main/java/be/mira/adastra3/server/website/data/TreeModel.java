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
        if (index == null)
            return null;

        TreeItem childItem = getItem(index);
        TreeItem parentItem = childItem.getParent();

        if (parentItem == mRoot)
            return null;

        return createIndex(parentItem.getRow(), 0, parentItem);
    }

    @Override
    public int getRowCount(WModelIndex parent) {
        return getItem(parent).getChildCount();
    }

    @Override
    public Object getHeaderData(int section, Orientation orientation, int role) {
        if (orientation == Orientation.Horizontal && role == ItemDataRole.DisplayRole)
            return mRoot.getField(section);
        return null;
    }
    
    // Custom
    public boolean insertRows(int position, List<? extends TreeItem> items, WModelIndex parent) {
        TreeItem parentItem = getItem(parent);
        boolean success;
        
        beginInsertRows(parent, position, position+items.size()-1);
        success = parentItem.insertChildren(position, items);
        endInsertRows();
        
        return success;
    }
    
    @Override
    public boolean removeRows(int position, int rows, WModelIndex parent) {
        TreeItem parentItem = getItem(parent);
        boolean success;
        
        beginRemoveRows(parent, position, position+rows-1);
        success = parentItem.removeChildren(position, rows);
        endRemoveRows();
        
        return success;        
    }
    
    public boolean removeRow(TreeItem iItem, WModelIndex parent) {
        TreeItem parentItem = getItem(parent);
        
        for (int row = 0; row < parentItem.getChildCount(); ++row) {
            TreeItem childItem = parentItem.getChild(row);
            if (childItem == iItem) {
                removeRows(childItem.getRow(), 1, parent);
                return true;
            }
            else if (removeRow(iItem, getIndex(childItem.getRow(), 0)))
                return true;
        }
        return false;
    }
    
    /*
    public boolean setData(WModelIndex index, Object value, int role) {
        if (role != ItemDataRole.EditRole)
            return false;
        
        TreeItem item = getItem(index);
        boolean result = item.setData(index.getColumn(), value);
        
        if (result)
            this.dataChanged().trigger(index, index);
        
        return result;
    }
     */
    
    
    //
    // Auxiliary
    //    
    
    public TreeItem getItem(WModelIndex index) {
        if (index != null) {
            TreeItem tItem = (TreeItem) index.getInternalPointer();
            if (tItem != null)
                return tItem;
        }
        return mRoot;
    }
    
    public WModelIndex index(int row, int column, WModelIndex parent)
    {
        if (parent != null && parent.getColumn() != 0)
            return null;
        
        TreeItem parentItem = getItem(parent);
        TreeItem childItem = parentItem.getChild(row);
        
        if (childItem != null)
            return createIndex(row, column, childItem);
        else
            return null;
    }
}
