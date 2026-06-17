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
import android.os.UserHandle;
import android.provider.Settings;

import androidx.preference.ListPreference;
import androidx.preference.Preference;

import com.android.settings.core.BasePreferenceController;

public class CarrierLabelPreferenceController extends BasePreferenceController
        implements Preference.OnPreferenceChangeListener {

    public CarrierLabelPreferenceController(Context context, String key) {
        super(context, key);
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference instanceof ListPreference) {
            ListPreference listPref = (ListPreference) preference;
            int value = Settings.System.getIntForUser(mContext.getContentResolver(),
                    Settings.System.LOCKSCREEN_SHOW_CARRIER, 1, UserHandle.USER_CURRENT);
            listPref.setValue(String.valueOf(value));
            listPref.setSummary(listPref.getEntry());
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        int value = Integer.parseInt((String) newValue);
        Settings.System.putIntForUser(mContext.getContentResolver(),
                Settings.System.LOCKSCREEN_SHOW_CARRIER, value, UserHandle.USER_CURRENT);
        updateState(preference);
        // Update dependent custom carrier text preference
        Preference customPref = preference.getPreferenceManager()
                .findPreference("lockscreen_show_custom_carrier_text");
        if (customPref != null) {
            customPref.setEnabled(value != 0);
        }
        return true;
    }
}
