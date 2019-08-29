package com.techflow.materialcolor.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import com.techflow.materialcolor.R
import com.techflow.materialcolor.utils.ThemeUtils
import com.techflow.materialcolor.utils.Tools

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize emoji first
        val config: EmojiCompat.Config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)

        // Set recent app header color
        ThemeUtils.setRecentAppsHeaderColor(this)

        customizeStatusBar()

    }

    private fun customizeStatusBar() {
        Tools.setSystemBarColor(this, R.color.colorPrimary)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}