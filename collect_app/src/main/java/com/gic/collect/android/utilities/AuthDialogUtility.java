/*
 * Copyright 2016 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gic.collect.android.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gic.collect.android.activities.InstanceUploaderList;
import com.gic.collect.android.application.Collect;
import com.gic.collect.android.preferences.GeneralSharedPreferences;
import com.gic.collect.android.preferences.PreferenceKeys;

/**
 * Used to present auth dialog and update credentials in the system as needed.
 */
public class AuthDialogUtility {
    private static final String TAG = "AuthDialogUtility";

    private Button registerUserButton;

    public AlertDialog createDialog(final Context context,
                                    final AuthDialogUtilityResultListener resultListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final View dialogView = LayoutInflater.from(context)
                .inflate(com.gic.collect.android.R.layout.server_auth_dialog, null);

        final EditText username = (EditText) dialogView.findViewById(com.gic.collect.android.R.id.username_edit);
        final EditText password = (EditText) dialogView.findViewById(com.gic.collect.android.R.id.password_edit);

        username.setText(getUserName());
        password.setText(getPassword());

        builder.setTitle(context.getString(com.gic.collect.android.R.string.server_requires_auth));
        builder.setMessage(context.getString(com.gic.collect.android.R.string.server_auth_credentials, getServer()));
        builder.setView(dialogView);
//        builder.setPositiveButton(context.getString(com.gic.collect.android.R.string.ok), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Collect.getInstance().getActivityLogger().logAction(this, TAG, "OK");
//
//                String userNameValue = username.getText().toString();
//                String passwordValue = password.getText().toString();
//
//                saveCredentials(userNameValue, passwordValue);
//                setWebCredentialsFromPreferences();
//
//                resultListener.updatedCredentials();
//            }
//        });

        builder.setNegativeButton(context.getString(com.gic.collect.android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Collect.getInstance().getActivityLogger().logAction(this, TAG, "Cancel");

                        resultListener.cancelledUpdatingCredentials();
                    }
                });

        // login button. expects a result.
        registerUserButton = (Button) dialogView.findViewById(com.gic.collect.android.R.id.login_edit);
        registerUserButton.setText("Login");
        registerUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collect.getInstance().getActivityLogger().logAction(this, TAG, "Login");

                String userNameValue = username.getText().toString();
                String passwordValue = password.getText().toString();

                saveCredentials(userNameValue, passwordValue);
                setWebCredentialsFromPreferences();

                resultListener.updatedCredentials();
            }
        });

        // register user button. expects a result.
        registerUserButton = (Button) dialogView.findViewById(com.gic.collect.android.R.id.register_user);
        registerUserButton.setText("Register");
        registerUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collect.getInstance().getActivityLogger().logAction(this, TAG, "Register");

                resultListener.registerUser();
            }
        });

        builder.setCancelable(false);

        return builder.create();
    }

    public static void setWebCredentialsFromPreferences() {

        String username = getUserName();
        String password = getPassword();

        if (username == null || username.isEmpty()) {
            return;
        }

        String host = Uri.parse(getServer()).getHost();
        WebUtils.addCredentials(username, password, host);
    }

    private static String getServer() {
        return (String) GeneralSharedPreferences.getInstance().get(PreferenceKeys.KEY_SERVER_URL);
    }

    private static String getPassword() {
        return (String) GeneralSharedPreferences.getInstance().get(PreferenceKeys.KEY_PASSWORD);
    }

    private static String getUserName() {
        return (String) GeneralSharedPreferences.getInstance().get(PreferenceKeys.KEY_USERNAME);
    }

    private void saveCredentials(String userName, String password) {
        GeneralSharedPreferences.getInstance().save(PreferenceKeys.KEY_USERNAME, userName);
        GeneralSharedPreferences.getInstance().save(PreferenceKeys.KEY_PASSWORD, password);
    }

    public interface AuthDialogUtilityResultListener {
        void updatedCredentials();

        void cancelledUpdatingCredentials();

        void registerUser();
    }
}
