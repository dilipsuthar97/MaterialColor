package com.techflow.materialcolor.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.BitmapFactory
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.techflow.materialcolor.R

object ThemeUtils {

    const val TAG: String = "ThemeUtils"
    const val LIGHT = "light"
    const val DARK = "dark"

    fun getTheme(context: Context): String {
        val sharedPreferences = SharedPref.getInstance(context)
        return sharedPreferences.getString(Preferences.THEME, LIGHT)!!
    }

    fun setTheme(context: Context, theme: String) {
        val sharedPreferences = SharedPref.getInstance(context)
        when (theme) {
            LIGHT -> sharedPreferences.saveData(Preferences.THEME, LIGHT)
            DARK -> sharedPreferences.saveData(Preferences.THEME, DARK)
        }
    }

    @ColorInt
    fun getThemeAttrColor(context: Context, @AttrRes colorAttr: Int): Int {
        val array = context.obtainStyledAttributes(null, intArrayOf(colorAttr))
        try {
            return array.getColor(0, 0)
        } finally {
            array.recycle()
        }
    }

    fun setRecentAppsHeaderColor(activity: Activity) {
        val icon = BitmapFactory.decodeResource(activity.resources, R.mipmap.ic_launcher)
        val taskDescription = ActivityManager.TaskDescription(
            activity.getString(R.string.app_name),
            icon, ContextCompat.getColor(activity, R.color.colorPrimaryDark)
        )
        activity.setTaskDescription(taskDescription)
        icon?.recycle()
    }
}