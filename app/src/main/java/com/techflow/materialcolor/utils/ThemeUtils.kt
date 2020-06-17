package com.techflow.materialcolor.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.BitmapFactory
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.techflow.materialcolor.R
import com.techflow.materialcolor.helpers.RemoteConfigHelper

/**
 * Modified by Dilip Suthar 15/12/19
 */
object ThemeUtils {

    const val TAG: String = "ThemeUtils"
    const val LIGHT = "light"
    const val DARK = "dark"

    /**
     * @func get theme type
     * @param context Context
     *
     * @return return string value for theme type
     */
    fun getTheme(context: Context): String {
        // Default theme value is coming from remote config
        val defaultTheme = RemoteConfigHelper.getInstance().remoteConfig.getString(RemoteConfigHelper.KEY_DEFAULT_THEME)
        return SharedPref.getInstance(context).getString(StorageKey.THEME, defaultTheme)!!
    }

    /**
     * @func save theme type into shared preference
     * @param context Context
     * @param theme string value of theme type LIGHT or DARK
     */
    fun setTheme(context: Context, theme: String) {
        val sharedPreferences = SharedPref.getInstance(context)
        when (theme) {
            LIGHT -> sharedPreferences.saveData(StorageKey.THEME, LIGHT)
            DARK -> sharedPreferences.saveData(StorageKey.THEME, DARK)
        }
    }

    /**
     * @func get attr value for given color
     * @param context Context
     * @param colorAttr res id of color attr
     *
     * @return return int value
     */
    @ColorInt
    fun getThemeAttrColor(context: Context, @AttrRes colorAttr: Int): Int {
        val array = context.obtainStyledAttributes(null, intArrayOf(colorAttr))
        try {
            return array.getColor(0, 0)
        } finally {
            array.recycle()
        }
    }

    /**
     * @func set header color of app in recent app mode
     * @param activity Activity
     */
    fun setRecentAppsHeaderColor(activity: Activity) {
        val icon = BitmapFactory.decodeResource(activity.resources, R.mipmap.ic_launcher)
        val taskDescription = ActivityManager.TaskDescription(
            activity.getString(R.string.app_name),
            icon, getThemeAttrColor(activity, R.attr.colorPrimary)
        )
        activity.setTaskDescription(taskDescription)
        icon?.recycle()
    }
}