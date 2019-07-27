package com.techflow.materialcolor.utils

import android.content.Context
import android.content.SharedPreferences
/**
 * Created by Dilip on 16/02/19
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

    public fun saveBooleanData(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    public fun getBooleanData(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

}