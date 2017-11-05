/*
Copyright 2017 Shobhit
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.gic.collect.android.fragments;

import com.gic.collect.android.utilities.ApplicationConstants;
import com.gic.collect.android.provider.InstanceProviderAPI;

public abstract class InstanceListFragment extends FileManagerFragment {
    protected String getSortingOrder() {
        String sortOrder = InstanceProviderAPI.InstanceColumns.DISPLAY_NAME + " ASC, " + InstanceProviderAPI.InstanceColumns.STATUS + " DESC";
        switch (getSelectedSortingOrder()) {
            case ApplicationConstants.SortingOrder.BY_NAME_ASC:
                sortOrder = InstanceProviderAPI.InstanceColumns.DISPLAY_NAME + " ASC, " + InstanceProviderAPI.InstanceColumns.STATUS + " DESC";
                break;
            case ApplicationConstants.SortingOrder.BY_NAME_DESC:
                sortOrder = InstanceProviderAPI.InstanceColumns.DISPLAY_NAME + " DESC, " + InstanceProviderAPI.InstanceColumns.STATUS + " DESC";
                break;
            case ApplicationConstants.SortingOrder.BY_DATE_ASC:
                sortOrder = InstanceProviderAPI.InstanceColumns.LAST_STATUS_CHANGE_DATE + " ASC";
                break;
            case ApplicationConstants.SortingOrder.BY_DATE_DESC:
                sortOrder = InstanceProviderAPI.InstanceColumns.LAST_STATUS_CHANGE_DATE + " DESC";
                break;
            case ApplicationConstants.SortingOrder.BY_STATUS_ASC:
                sortOrder = InstanceProviderAPI.InstanceColumns.STATUS + " ASC, " + InstanceProviderAPI.InstanceColumns.DISPLAY_NAME + " ASC";
                break;
            case ApplicationConstants.SortingOrder.BY_STATUS_DESC:
                sortOrder = InstanceProviderAPI.InstanceColumns.STATUS + " DESC, " + InstanceProviderAPI.InstanceColumns.DISPLAY_NAME + " ASC";
                break;
        }
        return sortOrder;
    }
}