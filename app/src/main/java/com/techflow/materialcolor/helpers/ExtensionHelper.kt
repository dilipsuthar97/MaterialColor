package com.techflow.materialcolor.helpers

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.graphics.ColorUtils
import com.google.android.material.snackbar.Snackbar

/**
 * @func compare the given boolean value with user's check predicate
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
fun Context.displayToast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
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