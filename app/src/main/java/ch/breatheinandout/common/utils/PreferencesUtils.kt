package ch.breatheinandout.common.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import java.lang.reflect.Type
import javax.inject.Inject

class PreferencesUtils @Inject constructor(
    val context: Context,
    val gson: Gson
) {
    companion object {
        const val PREF_NAME = "prefs_name"
        const val KEY_LAST_UPDATED = "LastUpdatedLocation"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE)

    fun put(key: String, value: String) {
        prefs.edit().apply {
            putString(key, value)
            apply()
        }
    }

    fun put(key: String, value: Boolean) {
        prefs.edit().apply {
            putBoolean(key, value)
            apply()
        }
    }

    fun putObject(key: String, obj: Any) {
        put(key, gson.toJson(obj))
    }

    fun getObject(key: String, default: String, typeToken: Type) : Any {
        return gson.fromJson(getValue(key, default), typeToken)
    }

    fun getValue(key: String, defaultValue: String): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }

    fun getValue(key: String, defaultValue: Boolean) : Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    fun removeValue(key: String) {
        prefs.edit().apply {
            remove(key)
            apply()
        }
    }

    fun clear() {
        prefs.edit().apply {
            clear()
            apply()
        }
    }
}