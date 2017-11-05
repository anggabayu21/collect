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

package com.gic.collect.android.widgets;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.gic.collect.android.application.Collect;

import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.StringData;
import org.javarosa.form.api.FormEntryPrompt;

import com.gic.collect.android.activities.ScannerWithFlashlightActivity;

/**
 * Widget that allows user to scan barcodes and add them to the form.
 *
 * @author Yaw Anokwa (yanokwa@gmail.com)
 */
public class BarcodeWidget extends QuestionWidget implements BinaryWidget {
    private Button getBarcodeButton;
    private TextView stringAnswer;

    public BarcodeWidget(Context context, FormEntryPrompt prompt) {
        super(context, prompt);

        getBarcodeButton = getSimpleButton(getContext().getString(com.gic.collect.android.R.string.get_barcode));
        getBarcodeButton.setEnabled(!prompt.isReadOnly());
        getBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collect.getInstance()
                        .getActivityLogger()
                        .logInstanceAction(this, "recordBarcode", "click",
                                formEntryPrompt.getIndex());

                Collect.getInstance().getFormController()
                        .setIndexWaitingForData(formEntryPrompt.getIndex());

                new IntentIntegrator((Activity) getContext())
                        .setCaptureActivity(ScannerWithFlashlightActivity.class)
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                        .setOrientationLocked(false)
                        .setPrompt(getContext().getString(com.gic.collect.android.R.string.barcode_scanner_prompt))
                        .initiateScan();
            }
        });

        stringAnswer = getCenteredAnswerTextView();

        String s = prompt.getAnswerText();
        if (s != null) {
            getBarcodeButton.setText(getContext().getString(
                    com.gic.collect.android.R.string.replace_barcode));
            stringAnswer.setText(s);
        }
        // finish complex layout
        LinearLayout answerLayout = new LinearLayout(getContext());
        answerLayout.setOrientation(LinearLayout.VERTICAL);
        answerLayout.addView(getBarcodeButton);
        answerLayout.addView(stringAnswer);
        addAnswerView(answerLayout);
    }

    @Override
    public void clearAnswer() {
        stringAnswer.setText(null);
        getBarcodeButton.setText(getContext().getString(com.gic.collect.android.R.string.get_barcode));
    }

    @Override
    public IAnswerData getAnswer() {
        String s = stringAnswer.getText().toString();
        if (s == null || s.equals("")) {
            return null;
        } else {
            return new StringData(s);
        }
    }

    /**
     * Allows answer to be set externally in {@Link FormEntryActivity}.
     */
    @Override
    public void setBinaryData(Object answer) {
        String response = (String) answer;
        if (response != null) {      // It looks like the answer is not set to null even if no barcode captured, however it seems prudent to check
            response = response.replaceAll("\\p{C}", "");
        }
        stringAnswer.setText(response);
        Collect.getInstance().getFormController().setIndexWaitingForData(null);
    }

    @Override
    public void setFocus(Context context) {
        // Hide the soft keyboard if it's showing.
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getWindowToken(), 0);
    }

    @Override
    public boolean isWaitingForBinaryData() {
        return formEntryPrompt.getIndex().equals(
                Collect.getInstance().getFormController()
                        .getIndexWaitingForData());
    }

    @Override
    public void cancelWaitingForBinaryData() {
        Collect.getInstance().getFormController().setIndexWaitingForData(null);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        stringAnswer.setOnLongClickListener(l);
        getBarcodeButton.setOnLongClickListener(l);
    }

    @Override
    public void cancelLongPress() {
        super.cancelLongPress();
        getBarcodeButton.cancelLongPress();
        stringAnswer.cancelLongPress();
    }
}