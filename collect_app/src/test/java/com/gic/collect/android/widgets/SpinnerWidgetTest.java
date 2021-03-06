package com.gic.collect.android.widgets;

import android.support.annotation.NonNull;

import com.gic.collect.android.widgets.base.GeneralSelectOneWidgetTest;

import org.robolectric.RuntimeEnvironment;

/**
 * @author James Knight
 */

public class SpinnerWidgetTest extends GeneralSelectOneWidgetTest<SpinnerWidget> {
    @NonNull
    @Override
    public SpinnerWidget createWidget() {
        return new SpinnerWidget(RuntimeEnvironment.application, formEntryPrompt);
    }
}
