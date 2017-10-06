package com.gic.collect.android.externalintents;

import android.support.test.filters.Suppress;
import android.support.test.rule.ActivityTestRule;

import com.gic.collect.android.activities.SplashScreenActivity;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

@Suppress
// Frequent failures: https://github.com/opendatakit/collect/issues/796
public class SplashScreenActivityTest {

    @Rule
    public ActivityTestRule<SplashScreenActivity> splashScreenActivityRule =
            new ExportedActivityTestRule<>(SplashScreenActivity.class);

    @Test
    public void splashScreenActivityMakesDirsTest() throws IOException {
        ExportedActivitiesUtils.testDirectories();
    }

}
