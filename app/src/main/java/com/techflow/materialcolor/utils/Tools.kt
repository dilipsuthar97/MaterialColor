package com.techflow.materialcolor.utils

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.os.Build
import androidx.annotation.ColorRes
import com.techflow.materialcolor.R
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.net.ConnectivityManager
import android.view.Menu
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.ColorUtils
import com.techflow.materialcolor.activity.WelcomeActivity
import com.techflow.materialcolor.helpers.displayToast
import kotlin.system.exitProcess

/**
 * Modified by Dilip Suthar on 21/12/19
 */
object Tools {
    private var toast: Toast? = null

    enum class InvisibilityType {
        GONE,
        INVISIBLE
    }

    fun setSystemBarColor(act: Activity) {
        if (Build.VERSION.SDK_INT >= 22) {
            val window = act.window
            window.addFlags(Int.MIN_VALUE)
            window.clearFlags(0)
            window.statusBarColor = ThemeUtils.getThemeAttrColor(act, R.attr.colorSecondaryDark)
        }
    }

    fun setSystemBarColor(act: Activity, @ColorRes color: Int) {
        if (Build.VERSION.SDK_INT >= 22) {
            val window = act.window
            window.addFlags(Int.MIN_VALUE)
            window.clearFlags(0)
            window.statusBarColor = ContextCompat.getColor(act, color)
        }
    }

    fun setSystemBarColorById(act: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= 22) {
            val window = act.window
            window.addFlags(Int.MIN_VALUE)
            window.clearFlags(0)
            window.statusBarColor = color
        }
    }

    fun changeMenuIconColor(menu: Menu, @ColorInt color: Int) {
        for (i in 0 until menu.size()) {
            val drawable = menu.getItem(i).icon ?: continue
            drawable.mutate()
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    fun changeNavigationIconColor(toolbar: Toolbar, @ColorInt color: Int) {
        val drawable = toolbar.navigationIcon
        drawable?.mutate()
        drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    fun setSystemBarLight(act: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            act.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    fun clearSystemBarLight(act: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            act.window.decorView.systemUiVisibility = 0
        }
    }

    fun visibleViews(vararg views: View) {
        for (v in views) {
            v.visibility = View.VISIBLE
        }
    }

    fun inVisibleViews(vararg views: View, type: InvisibilityType) {

        if (type == InvisibilityType.INVISIBLE) {
            for (v in views) {
                v.visibility = View.INVISIBLE
            }
        } else {
            for (v in views) {
                v.visibility = View.GONE
            }
        }
    }

    fun enableViews(vararg views: View) {
        for (v in views) {
            v.isEnabled = true
        }
    }

    fun disableViews(vararg views: View) {
        for (v in views) {
            v.isEnabled = false
        }
    }

    fun copyToClipboard(context: Context, data: String, msg: String) {
        val clip = "clipboard"
        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(ClipData.newPlainText(clip, data))
        toast?.cancel()
        toast = context.displayToast("$msg copied to clipboard")
    }

    fun hasNetwork(ctx: Context): Boolean {
        val networkInfo =
            (ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return !(networkInfo == null || !networkInfo.isConnected)
    }

    fun restartApp(ctx: Context) {
        val startIntent = Intent(ctx, WelcomeActivity::class.java)
        val pendingIntentId = 100
        val pendingIntent = PendingIntent.getActivity(ctx, pendingIntentId, startIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC, 50, pendingIntent)
        exitProcess(0)
    }

    fun isLolliopopOrGreater(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1

}