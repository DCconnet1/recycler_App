package com.dcconnet.sum

import android.content.Context
import android.content.SharedPreferences

class SharedPrefUtil private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(USER_EMAIL, Context.MODE_PRIVATE)

    companion object {
        const val USER_EMAIL = "user_email"
        private var instance: SharedPrefUtil? = null

        fun getInstance(context: Context): SharedPrefUtil {
            if (instance == null) {
                instance = SharedPrefUtil(context.applicationContext)
            }
            return instance as SharedPrefUtil
        }
    }

    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}