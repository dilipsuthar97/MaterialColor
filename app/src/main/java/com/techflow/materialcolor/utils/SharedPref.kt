package com.techflow.materialcolor.utils

import android.content.Context
import android.content.SharedPreferences
/**
 * Created by DILIP SUTHAR on 16/02/19
 */
class SharedPref constructor(context: Context){

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    // Static member & methods
    companion object {

        var instance: SharedPref? = null
        fun getInstance(context: Context): SharedPref {
            if (instance == null) {
                instance = SharedPref(context)
            }
            return instance as SharedPref
        }
    }

    fun getString(key: String, default: String = ""): String? {
        return sharedPreferences.getString(key, default)
    }

    fun getInt(key: String, default: Int = -1): Int {
        return sharedPreferences.getInt(key, default)
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    fun saveData(key: String, value: Any) {
        when (value) {
            is Boolean -> sharedPreferences.edit().putBoolean(key, value).apply()
            is String -> sharedPreferences.edit().putString(key, value).apply()
            is Int -> sharedPreferences.edit().putInt(key, value).apply()
        }
    }

    fun actionShowInterstitialAd(): Boolean {
        var count = getInt("clicks_count", 1)
        var isReset = false
        if (count < 9)
            count++
        else {
            count = 1
            isReset = true
        }

        saveData("clicks_count", count)
        return isReset
    }
}