package com.civilcam.data.local

import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferencesStorage(private val sharedPreferences: SharedPreferences) {

    operator fun set(key: String, value: Any?) = sharedPreferences.edit {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
        }
    }

    operator fun <T> get(key: String): T? = sharedPreferences.all[key] as? T

    companion object Keys {
        const val fcmTokenKey = "FcmTokenKey"
    }
}