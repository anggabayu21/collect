package com.gic.collect.android.location.activities;

import android.content.Intent;
import android.location.Location;

import com.gic.collect.android.BuildConfig;
import com.gic.collect.android.activities.FormEntryActivity;
import com.gic.collect.android.activities.GeoPointOsmMapActivity;
import com.gic.collect.android.spatial.MapHelper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.gic.collect.android.location.LocationClient;
import com.gic.collect.android.location.LocationClients;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static android.app.Activity.RESULT_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.gic.collect.android.location.activities.GeoPointActivityTest.newMockLocation;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class GeoPointOsmMapActivityTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private ActivityController<GeoPointOsmMapActivity> activityController;

    private GeoPointOsmMapActivity activity;
    private ShadowActivity shadowActivity;

    @Mock
    LocationClient locationClient;

    @Mock
    MapHelper mapHelper;

    /**
     * Runs {@link Before} each test.
     */
    @Before
    public void setUp() throws Exception {
        activityController = Robolectric.buildActivity(GeoPointOsmMapActivity.class);
        activity = activityController.get();
        shadowActivity = shadowOf(activity);

        LocationClients.setTestClient(locationClient);
    }

    @Test
    public void testLocationClientLifecycle() {
        activity.setHelper(mapHelper);

        activityController.create();
        activityController.start();

        verify(locationClient).start();

        when(locationClient.isLocationAvailable()).thenReturn(true);

        activity.onClientStart();

        verify(locationClient).requestLocationUpdates(activity);

        Location location = newMockLocation();
        activity.onLocationChanged(location);

        assertNull(activity.getZoomDialog());

        // Second location should do something:
        Location secondLocation = newMockLocation();
        when(secondLocation.getProvider()).thenReturn("GPS");
        when(secondLocation.getAccuracy()).thenReturn(1.0f);
        when(secondLocation.getLatitude()).thenReturn(2.0);
        when(secondLocation.getLongitude()).thenReturn(3.0);
        when(secondLocation.getAltitude()).thenReturn(4.0);

        activity.onLocationChanged(secondLocation);

        assertNotNull(activity.getZoomDialog());
        activity.getZoomDialog().dismiss();

        activity.returnLocation();

        assertEquals(shadowActivity.getResultCode(), RESULT_OK);
        Intent resultIntent = shadowActivity.getResultIntent();

        assertEquals(resultIntent.getStringExtra(FormEntryActivity.LOCATION_RESULT),
                activity.getResultString(secondLocation));

    }

    
}