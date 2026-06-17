/*
 * Copyright (C) 2026 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.security;

import android.content.Context;
import android.content.DialogInterface;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;

public class CustomCarrierLabelPreferenceController extends BasePreferenceController {

    private String mCustomCarrierText;

    public CustomCarrierLabelPreferenceController(Context context, String key) {
        super(context, key);
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public void updateState(Preference preference) {
        super.updateState(preference);
        mCustomCarrierText = Settings.System.getStringForUser(
                mContext.getContentResolver(),
                Settings.System.LOCKSCREEN_SHOW_CUSTOM_CARRIER_TEXT, UserHandle.USER_CURRENT);
        if (TextUtils.isEmpty(mCustomCarrierText)) {
            preference.setSummary(R.string.custom_carrier_label_summary);
        } else {
            preference.setSummary(mCustomCarrierText);
        }
        int showCarrier = Settings.System.getIntForUser(mContext.getContentResolver(),
                Settings.System.LOCKSCREEN_SHOW_CARRIER, 1, UserHandle.USER_CURRENT);
        preference.setEnabled(showCarrier != 0);
    }

    @Override
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (TextUtils.equals(getPreferenceKey(), preference.getKey())) {
            showDialog(preference);
            return true;
        }
        return false;
    }

    private void showDialog(Preference preference) {
        AlertDialog.Builder alert = new AlertDialog.Builder(preference.getContext());
        alert.setTitle(R.string.custom_carrier_label_title);
        alert.setMessage(R.string.custom_carrier_label_dialog_message);

        LinearLayout container = new LinearLayout(preference.getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // Convert dips to pixels for margins
        int margin = (int) (24 * preference.getContext().getResources().getDisplayMetrics().density);
        lp.setMargins(margin, margin / 2, margin, margin / 2);

        final EditText input = new EditText(preference.getContext());
        input.setText(TextUtils.isEmpty(mCustomCarrierText) ? "" : mCustomCarrierText);
        input.setSelection(input.getText().length());
        input.setLayoutParams(lp);
        input.setGravity(Gravity.START | Gravity.TOP);
        container.addView(input);
        alert.setView(container);

        alert.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = input.getText().toString();
                        Settings.System.putStringForUser(mContext.getContentResolver(),
                                Settings.System.LOCKSCREEN_SHOW_CUSTOM_CARRIER_TEXT, value, UserHandle.USER_CURRENT);
                        updateState(preference);
                    }
                });
        alert.setNegativeButton(android.R.string.cancel, null);
        alert.show();
    }
}
