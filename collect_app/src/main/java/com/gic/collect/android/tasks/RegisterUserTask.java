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

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.gic.collect.android.R;
import com.gic.collect.android.application.Collect;
import com.gic.collect.android.preferences.PreferenceKeys;
import com.gic.collect.android.utilities.WebUtils;
import com.gic.collect.android.logic.FormRegisterUserDetails;

import org.kxml2.kdom.Element;
import org.opendatakit.httpclientandroidlib.client.HttpClient;
import org.opendatakit.httpclientandroidlib.protocol.HttpContext;

import java.util.HashMap;

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
public class RegisterUserTask extends AsyncTask<Void, String, FormRegisterUserDetails> {

    // used to store error message if one occurs
    public static final String RU_ERROR_MSG = "ruerrormessage";

    private RegisterUserListener stateListener;
    private FormRegisterUserDetails formRegisterUserDetails;

    private static final String NAMESPACE_OPENROSA_ORG_XFORMS_XFORMS_LIST =
            "http://openrosa.org/xforms/xformsList";


    private boolean isXformsListNamespacedElement(Element e) {
        return e.getNamespace().equalsIgnoreCase(NAMESPACE_OPENROSA_ORG_XFORMS_XFORMS_LIST);
    }


    @Override
    protected FormRegisterUserDetails doInBackground(Void... values) {
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(
                        Collect.getInstance().getBaseContext());
        String serverUrl =
                settings.getString(PreferenceKeys.KEY_SERVER_URL,
                        Collect.getInstance().getString(R.string.default_server_url));
        // NOTE: /formlist must not be translated! It is the well-known path on the server.
        String registerUserUrl = "/accounts/register/";
        registerUserUrl = serverUrl + registerUserUrl;

        Collect.getInstance().getActivityLogger().logAction(this, serverUrl, registerUserUrl);

        // We populate this with available forms from the specified server.
        // <formname, details>
        // We populate this with available forms from the specified server.
        // <formname, details>
        FormRegisterUserDetails formRegisterUser = new FormRegisterUserDetails();

        // get shared HttpContext so that authentication and cookies are retained.
        HttpContext localContext = Collect.getInstance().getHttpContext();
        HttpClient httpclient = WebUtils.createHttpClient(WebUtils.CONNECTION_TIMEOUT);

        FormRegisterUserDetails result =
                WebUtils.sendRegisterFormToServer(this.formRegisterUserDetails,registerUserUrl, localContext, httpclient);
        formRegisterUser = result;

        Timber.i("Test Result of accessing web");
        Timber.i(result.errorStr);

        return formRegisterUser;
    }


    @Override
    protected void onPostExecute(FormRegisterUserDetails value) {
        synchronized (this) {
            if (stateListener != null) {
                stateListener.registerUserComplete(value);
            }
        }
    }

    public void setFormRegisterUserDetails(FormRegisterUserDetails formRegisterUserDetails){
        this.formRegisterUserDetails = formRegisterUserDetails;
    }

    public FormRegisterUserDetails getFormRegisterUserDetails(){
        return this.formRegisterUserDetails;
    }

    public void setRegisterUserListener(RegisterUserListener sl) {
        synchronized (this) {
            stateListener = sl;
        }
    }

    public interface RegisterUserListener {
        void registerUserComplete(FormRegisterUserDetails value);
    }

}
