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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.gic.collect.android.application.Collect;
import com.gic.collect.android.logic.FormRegisterUserDetails;
import com.gic.collect.android.logic.FormResetPasswordDetails;
import com.gic.collect.android.preferences.GeneralSharedPreferences;
import com.gic.collect.android.preferences.PreferenceKeys;

import timber.log.Timber;

/**
 * Used to present auth dialog and update credentials in the system as needed.
 * @author Angga Bayu (angga.bayu.marthafifsa@gmail.com)
 */
public class ResetPasswordDialogUtility {
    private static final String TAG = "ResetPasswordDialogUtility";

    public AlertDialog createDialog(final Context context,
                                    final ResetPasswordDialogUtilityListener resultListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final View dialogView = LayoutInflater.from(context)
                .inflate(com.gic.collect.android.R.layout.server_reset_password_dialog, null);

        final EditText email = (EditText) dialogView.findViewById(com.gic.collect.android.R.id.email_reset);

        email.setText("");

        builder.setTitle("Reset Password");
        builder.setMessage("Reset Password in " + getServer());
        builder.setView(dialogView);
        builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Collect.getInstance().getActivityLogger().logAction(this, TAG, "Reset");

                String emailValue = email.getText().toString();

                FormResetPasswordDetails formResetPasswordDetails = new FormResetPasswordDetails("", emailValue);

                Timber.i("Test click reset password");

                resultListener.sendResetPasswordForm(formResetPasswordDetails);
            }
        });
        builder.setNegativeButton(context.getString(com.gic.collect.android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Collect.getInstance().getActivityLogger().logAction(this, TAG, "Cancel");

                        resultListener.cancelledResetPassword();
                    }
                });

        builder.setCancelable(false);

        return builder.create();
    }

    private static String getServer() {
        return (String) GeneralSharedPreferences.getInstance().get(PreferenceKeys.KEY_SERVER_URL);
    }

    public interface ResetPasswordDialogUtilityListener {
        void sendResetPasswordForm(FormResetPasswordDetails formResetPasswordDetails);
        void cancelledResetPassword();
    }
}
