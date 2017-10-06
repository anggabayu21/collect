package com.gic.collect.android.activities;

import com.gic.collect.android.utilities.ApplicationConstants;
import com.gic.collect.android.provider.InstanceProviderAPI.InstanceColumns;

abstract class InstanceListActivity extends AppListActivity {
    protected String getSortingOrder() {
        String sortingOrder = InstanceColumns.DISPLAY_NAME + " ASC, " + InstanceColumns.STATUS + " DESC";
        switch (getSelectedSortingOrder()) {
            case ApplicationConstants.SortingOrder.BY_NAME_ASC:
                sortingOrder = InstanceColumns.DISPLAY_NAME + " ASC, " + InstanceColumns.STATUS + " DESC";
                break;
            case ApplicationConstants.SortingOrder.BY_NAME_DESC:
                sortingOrder = InstanceColumns.DISPLAY_NAME + " DESC, " + InstanceColumns.STATUS + " DESC";
                break;
            case ApplicationConstants.SortingOrder.BY_DATE_ASC:
                sortingOrder = InstanceColumns.LAST_STATUS_CHANGE_DATE + " ASC";
                break;
            case ApplicationConstants.SortingOrder.BY_DATE_DESC:
                sortingOrder = InstanceColumns.LAST_STATUS_CHANGE_DATE + " DESC";
                break;
            case ApplicationConstants.SortingOrder.BY_STATUS_ASC:
                sortingOrder = InstanceColumns.STATUS + " ASC, " + InstanceColumns.DISPLAY_NAME + " ASC";
                break;
            case ApplicationConstants.SortingOrder.BY_STATUS_DESC:
                sortingOrder = InstanceColumns.STATUS + " DESC, " + InstanceColumns.DISPLAY_NAME + " ASC";
                break;
        }
        return sortingOrder;
    }
}