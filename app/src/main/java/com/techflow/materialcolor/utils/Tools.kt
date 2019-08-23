package com.techflow.materialcolor.utils

import android.app.Activity
import android.os.Build
import androidx.annotation.ColorRes
import com.techflow.materialcolor.R
import android.content.ClipData
import androidx.core.content.ContextCompat.getSystemService
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
/**
 * Created by Dilip on 16/02/19
 */
object Tools {

    fun setSystemBarColor(act: Activity) {
        if (Build.VERSION.SDK_INT >= 22) {
            val window = act.window
            window.addFlags(Int.MIN_VALUE)
            window.clearFlags(0)
            window.statusBarColor = ContextCompat.getColor(act, R.color.colorPrimaryDark)
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

    fun copyToClipboard(context: Context, data: String) {
        val clip = "clipboard"
        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText(clip, data))
    }

}