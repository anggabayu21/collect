/*
 * Copyright 2017 Angga Bayu
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
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.gic.collect.android.application.Collect;
import com.gic.collect.android.logic.FormRegisterUserDetails;
import com.gic.collect.android.preferences.GeneralSharedPreferences;
import com.gic.collect.android.preferences.PreferenceKeys;

import timber.log.Timber;

/**
 * Used to present auth dialog and update credentials in the system as needed.
 * @author Angga Bayu (angga.bayu.marthafifsa@gmail.com)
 */
public class RegisterUserDialogUtility {
    private static final String TAG = "RegisterUserDialogUtility";

    public AlertDialog createDialog(final Context context,
                                    final RegisterUserDialogUtilityListener resultListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final View dialogView = LayoutInflater.from(context)
                .inflate(com.gic.collect.android.R.layout.server_register_user_dialog, null);

        final EditText username = (EditText) dialogView.findViewById(com.gic.collect.android.R.id.username_register);
        final EditText email = (EditText) dialogView.findViewById(com.gic.collect.android.R.id.email_register);
        final EditText firstname = (EditText) dialogView.findViewById(com.gic.collect.android.R.id.firstname_register);
        final EditText password = (EditText) dialogView.findViewById(com.gic.collect.android.R.id.password_register);
        final EditText password2 = (EditText) dialogView.findViewById(com.gic.collect.android.R.id.password2_register);

        username.setText("");
        email.setText("");
        firstname.setText("");
        password.setText("");
        password2.setText("");

        builder.setTitle("Register New User");
        builder.setMessage("Register New User in " + getServer());
        builder.setView(dialogView);
        builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Collect.getInstance().getActivityLogger().logAction(this, TAG, "Register");

                String userNameValue = username.getText().toString();
                String passwordValue = password.getText().toString();
                String password2Value = password2.getText().toString();
                String emailValue = email.getText().toString();
                String firstnameValue = firstname.getText().toString();

                FormRegisterUserDetails formRegisterUserDetails = new FormRegisterUserDetails("", userNameValue, emailValue, passwordValue, password2Value, firstnameValue);

                Timber.i("Test click registration");

                resultListener.sendRegisterUserForm(formRegisterUserDetails);
            }
        });
        builder.setNegativeButton(context.getString(com.gic.collect.android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Collect.getInstance().getActivityLogger().logAction(this, TAG, "Cancel");

                        resultListener.cancelledRegisterUser();
                    }
                });

        builder.setCancelable(false);

        return builder.create();
    }

    private static String getServer() {
        return (String) GeneralSharedPreferences.getInstance().get(PreferenceKeys.KEY_SERVER_URL);
    }

    public interface RegisterUserDialogUtilityListener {
        void sendRegisterUserForm(FormRegisterUserDetails formRegisterUserDetails);
        void cancelledRegisterUser();
    }
}
