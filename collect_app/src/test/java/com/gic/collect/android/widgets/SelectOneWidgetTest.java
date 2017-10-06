package com.gic.collect.android.widgets;

import android.support.annotation.NonNull;

import com.gic.collect.android.widgets.base.GeneralSelectOneWidgetTest;

import org.robolectric.RuntimeEnvironment;

/**
 * @author James Knight
 */

public class SelectOneWidgetTest extends GeneralSelectOneWidgetTest<SelectOneWidget> {

    @NonNull
    @Override
    public SelectOneWidget createWidget() {
        return new SelectOneWidget(RuntimeEnvironment.application, formEntryPrompt);
    }
}
