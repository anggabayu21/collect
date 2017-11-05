/*
 * Copyright (C) 2009 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.gic.collect.android.tasks;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.gic.collect.android.R;
import com.gic.collect.android.activities.FormEntryActivity;
import com.gic.collect.android.application.Collect;
import com.gic.collect.android.location.LocationClient;
import com.gic.collect.android.location.LocationClients;
import com.gic.collect.android.logic.LocationDetails;
import com.gic.collect.android.utilities.ToastUtils;
import com.gic.collect.android.widgets.GeoPointWidget;
import com.google.android.gms.location.LocationListener;

import java.text.DecimalFormat;

import timber.log.Timber;

/**
 * Background task for downloading forms from urls or a formlist from a url. We overload this task
 * a
 * bit so that we don't have to keep track of two separate downloading tasks and it simplifies
 * interfaces. If LIST_URL is passed to doInBackground(), we fetch a form list. If a hashmap
 * containing form/url pairs is passed, we download those forms.
 *
 * @author Angga Bayu (angga.bayu.marthafifsa@gmail.com)
 */
public class GeoPointTask extends AsyncTask<Void, String, LocationDetails> implements LocationListener,
        LocationClient.LocationClientListener  {

    // used to store error message if one occurs
    public static final String LOC_ERROR_MSG = "locerrormessage";

    private LocationListener stateListener;
    private LocationDetails locationDetails;

    // Default values for requesting Location updates.
    private static final long LOCATION_UPDATE_INTERVAL = 100;
    private static final long LOCATION_FASTEST_UPDATE_INTERVAL = 50;

    private static final String LOCATION_COUNT = "locationCount";

    private LocationClient locationClient;
    private Location location;
    private Context contextParent;

    private double locationAccuracy = 0;
    private int locationCount = 0;


    @Override
    protected LocationDetails doInBackground(Void... values) {

        Collect.getInstance().getActivityLogger().logAction(this, "Start GeoPointTask", "Starting");

        if(locationAccuracy == 0){
            locationAccuracy = GeoPointWidget.DEFAULT_LOCATION_ACCURACY;
        }

        locationClient = LocationClients.clientForContext(contextParent);
        if (locationClient.canSetUpdateIntervals()) {
            locationClient.setUpdateIntervals(LOCATION_UPDATE_INTERVAL, LOCATION_FASTEST_UPDATE_INTERVAL);
        }

        locationClient.setListener(this);

        Timber.i("Test start loop to get location");
        while (!this.isCancelled()) {
            //loop invinite until get location
        }

        Timber.i("Test end task on get location");
        return null;
    }

    //listener location
    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        Timber.i("Test onLocationChanged");
        if (location != null) {
            // Bug report: cached GeoPoint is being returned as the first value.
            // Wait for the 2nd value to be returned, which is hopefully not cached?
            ++locationCount;
            Timber.i("onLocationChanged(%d) location: %s", locationCount, location);

            if (locationCount > 1) {
                //locationDialog.setMessage(getProviderAccuracyMessage(location));
                Timber.i("Test locationCount > 1 and result = "+getProviderAccuracyMessage(location));

                if (location.getAccuracy() <= locationAccuracy) {
                    returnLocation();
                }

            } else {
                //locationDialog.setMessage(getAccuracyMessage(location));
                Timber.i("Test locationCount <1 and result = "+getAccuracyMessage(location));
            }

        } else {
            Timber.i("onLocationChanged(%d)", locationCount);
        }
    }

    public void setLocationAccuracy(double locationAccuracy){
        this.locationAccuracy = locationAccuracy;
    }

    public void setContextParent(Context contextParent){
        this.contextParent = contextParent;
    }

    public void setFormRegisterUserDetails(LocationDetails locationDetails){
        this.locationDetails = locationDetails;
    }

    public LocationDetails getLocationDetails(){
        return this.locationDetails;
    }


    private void logLastLocation() {
        Location loc = locationClient.getLastLocation();

        if (loc != null) {
            Timber.i("lastKnownLocation() lat: %f long: %f acc: %f", loc.getLatitude(), loc.getLongitude(), loc.getAccuracy());

        } else {
            Timber.i("lastKnownLocation() null location");
        }
    }

    private void finishOnError() {
//        ToastUtils.showShortToast(R.string.provider_disabled_error);
//        Intent onGPSIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//
//        startActivity(onGPSIntent);
//        finish();
        Timber.i("Test finishOnError get GPS");
        this.cancel(false);
    }

    private void returnLocation() {
        if (location != null) {
            //Intent i = new Intent();

            //i.putExtra(FormEntryActivity.LOCATION_RESULT, getResultStringForLocation(location));

            //setResult(RESULT_OK, i);
            Timber.i("Test Finish returnLocation get GPS");
        }
        this.cancel(false);
        //finish();
    }

    // LocationClientListener:

    @Override
    public void onClientStart() {
        Timber.i("Test onClientStart");
        locationClient.requestLocationUpdates(this);

        if (locationClient.isLocationAvailable()) {
            logLastLocation();

        } else {
            finishOnError();
        }
    }

    @Override
    public void onClientStartFailure() {
        finishOnError();
    }

    @Override
    public void onClientStop() {

    }

    public String getAccuracyMessage(@NonNull Location location) {
        return contextParent.getString(R.string.location_accuracy, location.getAccuracy());
    }

    public String getProviderAccuracyMessage(@NonNull Location location) {
        return contextParent.getString(R.string.location_provider_accuracy, location.getProvider(), truncateDouble(location.getAccuracy()));
    }

    public String getResultStringForLocation(@NonNull Location location) {
        return String.format("%s %s %s %s", location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy());
    }

    private String truncateDouble(float number) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number);
    }

    public void setLocationListener(LocationListener sl) {
        synchronized (this) {
            stateListener = sl;
        }
    }

    @Override
    protected void onPostExecute(LocationDetails value) {
        synchronized (this) {
            if (stateListener != null) {
                stateListener.locationComplete(value);
            }
        }
    }

    public interface LocationListener {
        void locationComplete(LocationDetails value);
    }

}
