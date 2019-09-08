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
        when(ThemeUtils.getTheme(this)) {
            ThemeUtils.LIGHT -> setTheme(R.style.MaterialColor_Base_Light)
            ThemeUtils.DARK -> setTheme(R.style.MaterialColor_Base_Dark)
        }
        super.onCreate(savedInstanceState)

        // Initialize emoji first
        val config: EmojiCompat.Config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)

        // Set recent app header color
        ThemeUtils.setRecentAppsHeaderColor(this)
        customizeStatusBar()

    }

    /** @method customize status bar bg & icon color */
    private fun customizeStatusBar() {
        when(ThemeUtils.getTheme(this)) {
            ThemeUtils.LIGHT -> Tools.setSystemBarLight(this)
            ThemeUtils.DARK -> Tools.clearSystemBarLight(this)
        }

        Tools.setSystemBarColorById(this, ThemeUtils.getThemeAttrColor(this, R.attr.statusBarColor))
    }
}