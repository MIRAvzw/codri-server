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
    
    public TreeModel(final List<String> iHeaders) {
        // Set-up tree root
        mRoot = new RootItem(iHeaders);
    }
    
    
    //
    // Getters and setters
    //
    
    public final RootItem getRoot() {
        return mRoot;
    }
    
    
    //
    // Model implementation
    //

    @Override
    public final int getColumnCount(final WModelIndex iParent) {
        if (iParent != null) {
            return ((TreeItem) (iParent.getInternalPointer())).getFieldCount();
        } else {
            return mRoot.getFieldCount();
        }
    }

    @Override
    public final Object getData(final WModelIndex iIndex, final int iRole) {
        if (iIndex == null) {
            return null;
        }

        if (iRole != ItemDataRole.DisplayRole) {
            return null;
        }

        TreeItem tItem = (TreeItem) (iIndex.getInternalPointer());

        return tItem.getField(iIndex.getColumn());
    }

    @Override
    public final WModelIndex getIndex(final int iRow, final int iColumn, final WModelIndex iParent) {
        if (! this.hasIndex(iRow, iColumn, iParent)) {
            return null;
        }
        
        // Get the actual parent
        TreeItem tParentItem = getItem(iParent);
        
        // Get the child
        TreeItem tChildItem = tParentItem.getChild(iRow);
        if (tChildItem != null) {
            return createIndex(iRow, iColumn, tChildItem);
        } else {
            return null;
        }
    }

    @Override
    public final WModelIndex getParent(final WModelIndex iIndex) {
        if (iIndex == null) {
            return null;
        }

        TreeItem tChildItem = getItem(iIndex);
        TreeItem tParentItem = tChildItem.getParent();

        if (tParentItem == mRoot) {
            return null;
        }

        return createIndex(tParentItem.getRow(), 0, tParentItem);
    }

    @Override
    public final int getRowCount(final WModelIndex iParent) {
        return getItem(iParent).getChildCount();
    }

    @Override
    public final Object getHeaderData(final int iSection, final Orientation iOrientation, final int iRole) {
        if (iOrientation == Orientation.Horizontal && iRole == ItemDataRole.DisplayRole) {
            return mRoot.getField(iSection);
        }
        return null;
    }
    
    // Custom
    public final boolean insertRows(final int iPosition, final List<? extends TreeItem> iItems, final WModelIndex iParent) {
        TreeItem tParentItem = getItem(iParent);
        boolean tSuccess;
        
        beginInsertRows(iParent, iPosition, iPosition+iItems.size()-1);
        tSuccess = tParentItem.insertChildren(iPosition, iItems);
        endInsertRows();
        
        return tSuccess;
    }
    
    @Override
    public final boolean removeRows(final int iPosition, final int iRows, final WModelIndex iParent) {
        TreeItem tParentItem = getItem(iParent);
        boolean tSuccess;
        
        beginRemoveRows(iParent, iPosition, iPosition+iRows-1);
        tSuccess = tParentItem.removeChildren(iPosition, iRows);
        endRemoveRows();
        
        return tSuccess;        
    }
    
    public final boolean removeRow(final TreeItem iItem, final WModelIndex iParent) {
        TreeItem tParentItem = getItem(iParent);
        
        for (int tRow = 0; tRow < tParentItem.getChildCount(); ++tRow) {
            TreeItem tChildItem = tParentItem.getChild(tRow);
            if (tChildItem == iItem) {
                removeRows(tChildItem.getRow(), 1, iParent);
                return true;
            } else if (removeRow(iItem, getIndex(tChildItem.getRow(), 0))) {
                return true;
            }
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
    
    public final TreeItem getItem(final WModelIndex iIndex) {
        if (iIndex != null) {
            TreeItem tItem = (TreeItem) iIndex.getInternalPointer();
            if (tItem != null) {
                return tItem;
            }
        }
        return mRoot;
    }
}
