package com.onpu.core.data.local

import android.content.Context
import androidx.preference.PreferenceManager
import com.onpu.domain.component.AppListeners
import com.onpu.domain.component.Preferences

class PreferencesImpl(
    context: Context,
    private val appListeners: AppListeners
) : Preferences {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    override var userId: Int
        get() = preferences.getInt("user_id", -1)
        set(value) = put("user_id", value)

    override var lastLinkWithSpecifier: String
        get() = preferences.getString("last_link_with_specifier", "") ?: ""
        set(value) = put("last_link_with_specifier", value)

    private fun get(key: String, default: Int) = preferences.getInt(key, default)
    private fun get(key: String, default: Long) = preferences.getLong(key, default)
    private fun get(key: String, default: Float) = preferences.getFloat(key, default)
    private fun get(key: String, default: String) = preferences.getString(key, default) ?: default
    private fun get(key: String, default: Boolean) = preferences.getBoolean(key, default)
    private fun put(key: String, value: Int) = preferences.edit().putInt(key, value).apply()
    private fun put(key: String, value: Long) = preferences.edit().putLong(key, value).apply()
    private fun put(key: String, value: Float) = preferences.edit().putFloat(key, value).apply()
    private fun put(key: String, value: String) = preferences.edit().putString(key, value).apply()
    private fun put(key: String, value: Boolean) = preferences.edit().putBoolean(key, value).apply()
}
