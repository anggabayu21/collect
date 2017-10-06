package com.gic.collect.android;

import com.gic.collect.android.utilities.TextUtilsTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import com.gic.collect.android.activities.MainActivityTest;
import com.gic.collect.android.utilities.CompressionTest;
import com.gic.collect.android.utilities.PermissionsTest;

/**
 * Suite for running all unit tests from one place
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        //Name of tests which are going to be run by suite
        MainActivityTest.class,
        PermissionsTest.class,
        TextUtilsTest.class,
        CompressionTest.class
})

public class AllTestsSuite {
    // the class remains empty,
    // used only as a holder for the above annotations
}
