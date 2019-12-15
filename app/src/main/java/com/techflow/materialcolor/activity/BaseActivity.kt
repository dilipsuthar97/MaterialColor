package com.techflow.materialcolor.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ads.AudienceNetworkAds
import com.techflow.materialcolor.R
import com.techflow.materialcolor.utils.ThemeUtils
import com.techflow.materialcolor.utils.Tools

/**
 * @author Dilip Suthar
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        when(ThemeUtils.getTheme(this)) {
            ThemeUtils.LIGHT -> setTheme(R.style.MaterialColor_Base_Light)
            ThemeUtils.DARK -> setTheme(R.style.MaterialColor_Base_Dark)
        }
        super.onCreate(savedInstanceState)

        // Set recent app header color
        ThemeUtils.setRecentAppsHeaderColor(this)
        customizeStatusBar()

        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this)

    }

    /** @func customize status bar bg & icon color */
    private fun customizeStatusBar() {
        when(ThemeUtils.getTheme(this)) {
            ThemeUtils.LIGHT -> Tools.setSystemBarLight(this)
            ThemeUtils.DARK -> Tools.clearSystemBarLight(this)
        }

        Tools.setSystemBarColorById(this, ThemeUtils.getThemeAttrColor(this, R.attr.statusBarColor))
    }
}