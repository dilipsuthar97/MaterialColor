package com.techflow.materialcolor.utils

import android.content.Context
import android.content.SharedPreferences
/**
 * modified by Dilip Suthar on 15/12/19
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

    /**
     * @func get string value from shared preference
     * @param key key for value
     * @param default default value to return
     *
     * @return return string value for given key
     */
    fun getString(key: String, default: String = ""): String? {
        return sharedPreferences.getString(key, default)
    }

    /**
     * @func get int value from shared preference
     * @param key key for value
     * @param default default value to return
     *
     * @return return int value for given key
     */
    fun getInt(key: String, default: Int = -1): Int {
        return sharedPreferences.getInt(key, default)
    }

    /**
     * @func get boolean value from shared preference
     * @param key key for value
     * @param default default value to return
     *
     * @return return boolean value for given key
     */
    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    /**
     * @func save data into shared preference
     * @param key key for value
     * @param value value to save into shared preference
     */
    fun saveData(key: String, value: Any) {
        when (value) {
            is Boolean -> sharedPreferences.edit().putBoolean(key, value).apply()
            is String -> sharedPreferences.edit().putString(key, value).apply()
            is Int -> sharedPreferences.edit().putInt(key, value).apply()
        }
    }

    /**
     * @func check and increase count value for serving full screen ad
     *
     * @return return boolean value
     */
    fun actionShowInterstitialAd(): Boolean {
        var count = getInt("clicks_count", 1)
        var isReset = false

        if (count >= 9) {
            count = 1
            saveData("clicks_count", count)
            isReset = true
        }

        return isReset
    }

    /**
     * @func it'll increment the count value of full screen ad showing
     */
    fun increaseInterstitialAdCounter() {
        var count = getInt("clicks_count", 1)
        count += 1
        saveData("clicks_count", count)
    }
}