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

package com.android.settings.notification;

import android.content.Context;

import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.TogglePreferenceController;

import lineageos.providers.LineageSettings;

public class RingAlarmPhoneSpeakerPreferenceController extends TogglePreferenceController {

    public static final String KEY = "ring_alarm_phone_speaker";

    private final AudioHelper mHelper;

    public RingAlarmPhoneSpeakerPreferenceController(Context context, String preferenceKey) {
        this(context, preferenceKey, new AudioHelper(context));
    }

    RingAlarmPhoneSpeakerPreferenceController(Context context, String preferenceKey,
            AudioHelper helper) {
        super(context, preferenceKey);
        mHelper = helper;
    }

    @Override
    public boolean isChecked() {
        return LineageSettings.System.getInt(mContext.getContentResolver(),
                LineageSettings.System.RING_ALARM_PERSONAL_AUDIO_SAFETY, 0) == 1;
    }

    @Override
    public boolean setChecked(boolean isChecked) {
        return LineageSettings.System.putInt(mContext.getContentResolver(),
                LineageSettings.System.RING_ALARM_PERSONAL_AUDIO_SAFETY, isChecked ? 1 : 0);
    }

    @Override
    public int getAvailabilityStatus() {
        return Utils.isVoiceCapable(mContext) && !mHelper.isSingleVolume()
                ? AVAILABLE : UNSUPPORTED_ON_DEVICE;
    }

    @Override
    public int getSliceHighlightMenuRes() {
        return R.string.menu_key_sound;
    }
}
