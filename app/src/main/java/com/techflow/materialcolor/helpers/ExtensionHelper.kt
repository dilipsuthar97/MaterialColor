package com.techflow.materialcolor.helpers

import android.content.Context
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.snackbar.Snackbar
import com.techflow.materialcolor.R

/**
 * @func check if color's luminance value is dark or light
 * @param this The boolean value to check predicate
 */
fun Int.isDark(): Boolean = ColorUtils.calculateLuminance(this) <= 0.5

/**
 * @func It compare boolean value with another boolean value
 * @param value The secondary boolean value to check equality
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
 */
fun Context.displayToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(
        this,
        message,
        duration
    ).show()
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
        .setToolbarColor(ContextCompat.getColor(this, R.color.colorAccent))
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
 */
fun Context.isTablet(): Boolean {
    return (this.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
}