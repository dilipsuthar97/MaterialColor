package com.techflow.materialcolor

import android.app.Application
import com.techflow.materialcolor.utils.ThemeUtils

class MaterialColor : Application() {

    enum class AdType {
        BANNER,
        INTERSTITIAL
    }

    companion object {

        private lateinit var instance: MaterialColor

        // Firebase
        const val FIREBASE_EVENT_TAB_COLOR = "tab_color_"
        const val FIREBASE_EVENT_TAB_GRADIENT = "tab_gradient_"
        const val FIREBASE_EVENT_TAB_COLOR_PICKER = "tab_color_picker_"
        const val FIREBASE_EVENT_TAB_BOOKMARKED_COLOR = "tab_bookmarked_color_"
        const val FIREBASE_EVENT_REMOVE_ADS = "remove_ads_"
        const val FIREBASE_EVENT_CUSTOM_COLOR_MAKER = "custom_color_maker_"
        const val FIREBASE_EVENT_CUSTOM_GRADIENT_MAKER = "custom_gradient_maker"
        const val FIREBASE_EVENT_ABOUT_ME = "about_me_"
        const val FIREBASE_EVENT_READ_POLICY = "read_policy_"
        const val FIREBASE_EVENT_MATERIAL_DESIGN_TOOL = "material_design_tool_"
        const val FIREBASE_EVENT_SUPPORT_DEVELOPMENT = "support_development_"
        const val FIREBASE_EVENT_DARK_MODE = "dark_mode_"

        fun getInstance(): MaterialColor {
            return instance
        }

        fun getAdId(adType: AdType): String {
            return when (adType) {
                AdType.BANNER -> BuildConfig.AUDIENCE_BANNER_ID
                AdType.INTERSTITIAL -> BuildConfig.AUDIENCE_INTERSTITIAL_ID
            }
        }
    }

    override fun onCreate() {
        when(ThemeUtils.getTheme(this)) {
            ThemeUtils.LIGHT -> setTheme(R.style.MaterialColor_Base_Light)
            ThemeUtils.DARK -> setTheme(R.style.MaterialColor_Base_Dark)
        }
        super.onCreate()
        instance = this
    }

}