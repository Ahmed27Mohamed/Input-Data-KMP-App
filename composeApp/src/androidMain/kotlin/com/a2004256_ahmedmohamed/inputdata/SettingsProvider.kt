package com.a2004256_ahmedmohamed.inputdata

import android.annotation.SuppressLint
import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import android.preference.PreferenceManager

@SuppressLint("StaticFieldLeak")
actual object SettingsProvider {
    lateinit var context: Context

    actual val settings: Settings by lazy {
        SharedPreferencesSettings(PreferenceManager.getDefaultSharedPreferences(context))
    }
}