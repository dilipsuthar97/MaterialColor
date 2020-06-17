package com.techflow.materialcolor.helpers

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.PopupMenu
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.snackbar.Snackbar
import com.techflow.materialcolor.R

/**
 * @func check if color's luminance value is dark or light
 * @param this The boolean value to check predicate
 *
 * @return Boolean
 */
fun Int.isDark(): Boolean = ColorUtils.calculateLuminance(this) <= 0.5

/**
 * @func It compare boolean value with another boolean value
 * @param value The secondary boolean value to check equality
 *
 * @return Boolean
 */
infix fun Boolean.eq(value: Boolean): Boolean = this == value

/**
 * @func display toast
 * @param message res id of string value to display message
 */
fun Context.displayToast(@StringRes message: Int) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
}

/**
 * @func display toast
 * @param message string value to display message
 *
 * @return Toast object
 */
fun Context.displayToast(message: String, duration: Int = Toast.LENGTH_SHORT): Toast {
     val toast = Toast.makeText(
        this,
        message,
        duration
    )
    toast.show()

    return toast
}

/**
 * @func display snackbar
 * @param message string value to display message
 * @param duration int duration value for snackbar
 */
fun View.displaySnackbar(message: String, duration: Int) {
    Snackbar.make(
        this,
        message,
        duration
    ).apply {
        show()
    }
}

/**
 * @func It will open URL link into chrome custom tab
 * @param url a url link in a string format
 */
fun Context.openWebView(url: String) {
    val intent = CustomTabsIntent.Builder()
        .addDefaultShareMenuItem()
        .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        .setShowTitle(true)
        .enableUrlBarHiding()
        .addDefaultShareMenuItem()
        .setStartAnimations(this, R.anim.anim_fragment_enter, R.anim.anim_fragment_exit)
        .setExitAnimations(this, R.anim.anim_fragment_enter, R.anim.anim_fragment_exit)
        .setCloseButtonIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_arrow_back))
        .build()

    intent.launchUrl(this, Uri.parse(url))
}

/**
 * @func check weather the device is tablet or phone
 *
 * @return Boolean
 */
fun Context.isTablet(): Boolean {
    return (this.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
}

/**
 * @func check weather app is in debug mode or not
 *
 * @return Boolean
 */
fun Context.isDebug(): Boolean {
    try {
        return (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) !== 0
    } catch (ignored: Exception) {
    }
    return false
}

/**
 * Show popup menu
 * @param view view for display popup with respect to
 * @param menuRes int resId of menu file
 * @param listener Popup menu listener interface
 */
fun Context.showPopup(view: View, menuRes: Int, listener: PopupMenu.OnMenuItemClickListener?) {
    val popup = PopupMenu(this, view)
    popup.menuInflater.inflate(menuRes, popup.menu)
    if (listener != null) popup.setOnMenuItemClickListener(listener)
    popup.show()
}

/**
 * Get app's current version
 *
 * @return String return current ap version
 */
fun Context.getAppVersion(): String? {
    val packageManager = packageManager
    var packageInfo: PackageInfo? = null
    try {
        packageInfo = packageManager.getPackageInfo(packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return packageInfo?.versionName ?: "0.0.0"
}