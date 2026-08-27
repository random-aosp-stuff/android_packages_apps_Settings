/*
 * Copyright (C) 2025 The LineageOS Project
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

package com.android.settings.development;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.settingslib.development.DevelopmentSettingsEnabler;

/**
 * Runs the same developer-options teardown as the Settings dashboard switch, so privileged
 * callers (SystemUI QS) can disable ADB and the rest of the flags without opening Settings.
 */
public class DisableDevelopmentSettingsReceiver extends BroadcastReceiver {

    private static final String TAG = "DisableDevSettings";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!DevelopmentSettingsEnabler.ACTION_DISABLE_DEVELOPMENT_SETTINGS.equals(
                intent.getAction())) {
            return;
        }
        final PendingResult result = goAsync();
        new Thread(() -> {
            try {
                DevelopmentSettingsDashboardFragment.disableDeveloperOptionsFromExternal(context);
            } catch (RuntimeException e) {
                Log.e(TAG, "Failed to disable developer options", e);
            } finally {
                result.finish();
            }
        }, "DisableDevSettings").start();
    }
}
