/*
 *  Copyright 2020 Dilip Suthar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package com.techflow.materialcolor.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.facebook.ads.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.techflow.materialcolor.MaterialColor
import com.techflow.materialcolor.MaterialColor.AdType
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivityHomeBinding
import com.techflow.materialcolor.fragment.BookmarkedColorFragment
import com.techflow.materialcolor.fragment.ColorPickerFragment
import com.techflow.materialcolor.fragment.GradientFragment
import com.techflow.materialcolor.fragment.HomeFragment
import com.techflow.materialcolor.helpers.AnalyticsHelper
import com.techflow.materialcolor.helpers.displaySnackbar
import com.techflow.materialcolor.helpers.displayToast
import com.techflow.materialcolor.helpers.isDebug
import com.techflow.materialcolor.utils.SharedPref
import com.techflow.materialcolor.utils.StorageKey
import com.techflow.materialcolor.utils.ThemeUtils
import com.techflow.materialcolor.utils.Tools

/**
 * @author Dilip Suthar
 * Modified by Dilip Suthar on 29/05/2020
 */
class HomeActivity : BaseActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val TAG = HomeActivity::class.java.simpleName
    private val HIGH_PRIORITY_UPDATE = 5

    val REQUEST_CODE_UPDATE = 201

    // STATIC
    companion object {

        // Audience network
        private lateinit var interstitialAd: InterstitialAd

        fun showInterstitialAd(context: Context) {

            if (!MaterialColor.getInstance().isDebug()) {
                if (SharedPref.getInstance(context).actionShowInterstitialAd()) {
                    Log.d(HomeActivity::class.java.simpleName, "serving ads")
                    if (interstitialAd.isAdLoaded)
                        interstitialAd.show()
                    else if (Tools.hasNetwork(context))
                        interstitialAd.loadAd()
                } else
                    SharedPref.getInstance(context).increaseInterstitialAdCounter()
            }
        }
    }

    private lateinit var binding: ActivityHomeBinding

    private lateinit var sharedPref: SharedPref

    private lateinit var homeFragment: HomeFragment
    private lateinit var gradientFragment: GradientFragment
    private lateinit var colorPickerFragment: ColorPickerFragment
    private lateinit var bookmarkedColorFragment: BookmarkedColorFragment
    private lateinit var activeFragment: Fragment

    private var bottomSheet: MaterialDialog? = null

    private var adView: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        sharedPref = SharedPref.getInstance(this)

        initToolbar()
        initComponent()
        initIntro()
        initBottomNavigation()
        initMenuSheet()
        if (!this.isDebug()) initAd()
        initInAppUpdate()   // Init in-app update
    }

    override fun onStart() {
        super.onStart()
        this.getSharedPreferences(packageName, Context.MODE_PRIVATE)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        if (!sharedPref.getBoolean(StorageKey.BOTTOM_SHEET_CONFIG, false))
            bottomSheet?.cancel()
    }

    override fun onDestroy() {
        if (adView != null)
            adView?.destroy()
        super.onDestroy()
        this.getSharedPreferences(packageName, Context.MODE_PRIVATE)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onBackPressed() {
        if (activeFragment != homeFragment) {

            binding.justBar.setSelected(0)
            displayFragment(homeFragment)
            activeFragment = homeFragment

        } else
            appCloser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        Tools.changeMenuIconColor(menu!!, ThemeUtils.getThemeAttrColor(this, R.attr.colorTextPrimary))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_menu) {
            bottomSheet?.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key!! == StorageKey.THEME)
            recreate()
    }

    /**
     * @func init toolbar config
     */
    private fun initToolbar() {
        setSupportActionBar(binding.toolbar as MaterialToolbar)
        supportActionBar?.title = resources.getString(R.string.app_name)
    }

    /**
     * @func init all component's config
     */
    private fun initComponent() {
        homeFragment = HomeFragment.getInstance()
        gradientFragment = GradientFragment.getInstance()
        colorPickerFragment = ColorPickerFragment.getInstance()
        bookmarkedColorFragment = BookmarkedColorFragment.getInstance()

        displayFragment(homeFragment)
    }

    /**
     * @func init app intro for first use
     */
    private fun initIntro() {
        if (sharedPref.getBoolean(StorageKey.isFirstRun, true)) {
            Snackbar.make(binding.rootLayout, "Long press on card to copy code", Snackbar.LENGTH_INDEFINITE).apply {
                setAction("NEXT") {
                    Snackbar.make(binding.rootLayout, "Tap on card to see different shades", Snackbar.LENGTH_INDEFINITE).apply {
                        setAction("CLOSE") {}
                        show()
                    }
                }
                show()
            }

            sharedPref.saveData(StorageKey.isFirstRun, false)
        }
    }

    /**
     * @func init bottom navigation config
     */
    private fun initBottomNavigation() {
        binding.justBar.setOnBarItemClickListener { barItem, position ->

            // Load ad
            if (SharedPref.getInstance(this).getBoolean(StorageKey.SHOW_AD, true))
                showInterstitialAd(this)

            when(barItem.id) {
                R.id.nav_home -> {
                    AnalyticsHelper.getInstance()?.logEvent(MaterialColor.FIREBASE_EVENT_TAB_COLOR, null)
                    displayFragment(homeFragment)
                    supportActionBar?.title = "MaterialColor"
                }
                R.id.nav_gradients -> {
                    AnalyticsHelper.getInstance()?.logEvent(MaterialColor.FIREBASE_EVENT_TAB_GRADIENT, null)
                    displayFragment(gradientFragment)
                    supportActionBar?.title = "Gradients"
                }
                R.id.nav_color_picker -> {
                    AnalyticsHelper.getInstance()?.logEvent(MaterialColor.FIREBASE_EVENT_TAB_COLOR_PICKER, null)
                    displayFragment(colorPickerFragment)
                    supportActionBar?.title = "Color Picker"
                }
                R.id.nav_bookmarked_color -> {
                    AnalyticsHelper.getInstance()?.logEvent(MaterialColor.FIREBASE_EVENT_TAB_BOOKMARKED_COLOR, null)
                    displayFragment(bookmarkedColorFragment)
                    supportActionBar?.title = "Bookmarked Color"
                }
            }
        }
    }

    /**
     * @func change fragment in frame layout
     * @param fragment fragment value
     */
    private fun displayFragment(fragment: Fragment?) {
        val fragmentManager = supportFragmentManager
        if (fragment != null) {
            activeFragment = fragment

            val transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.anim_fragment_enter, R.anim.anim_fragment_exit)
            transaction.replace(R.id.host_fragment, fragment)
            transaction.commit()
        }
    }

    /**
     * @func init bottom sheet menu config
     */
    private fun initMenuSheet() {
        bottomSheet = MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT))
            .cornerRadius(16f)
            .customView(R.layout.bottom_sheet_menu, scrollable = true)

        val view = bottomSheet?.getCustomView()

        view?.findViewById<ImageButton>(R.id.btn_settings)?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        view?.findViewById<LinearLayout>(R.id.btn_custom_color_maker)?.setOnClickListener {
            AnalyticsHelper.getInstance()?.logEvent(MaterialColor.FIREBASE_EVENT_CUSTOM_COLOR_MAKER, null)
            startActivity(Intent(this, CustomColorActivity::class.java))
        }

        view?.findViewById<LinearLayout>(R.id.btn_gradient_maker)?.setOnClickListener {
            AnalyticsHelper.getInstance()?.logEvent(MaterialColor.FIREBASE_EVENT_CUSTOM_GRADIENT_MAKER, null)
            startActivity(Intent(this, CustomGradientActivity::class.java))
        }

        view?.findViewById<LinearLayout>(R.id.btn_material_design_tool)?.setOnClickListener {
            AnalyticsHelper.getInstance()?.logEvent(MaterialColor.FIREBASE_EVENT_MATERIAL_DESIGN_TOOL, null)
            startActivity(Intent(this, DesignToolActivity::class.java))
        }

        view?.findViewById<LinearLayout>(R.id.btn_flat_ui_colors)?.setOnClickListener {
            AnalyticsHelper.getInstance()?.logEvent(MaterialColor.FIREBASE_EVENT_FLAT_UI_COLORS, null)
            startActivity(Intent(this, FlatUIColorsActivity::class.java))
        }

        view?.findViewById<LinearLayout>(R.id.btn_social_colors)?.setOnClickListener {
            AnalyticsHelper.getInstance()?.logEvent(MaterialColor.FIREBASE_EVENT_SOCIAL_COLORS, null)
            startActivity(Intent(this, SocialColorsActivity::class.java))
        }

        view?.findViewById<LinearLayout>(R.id.btn_metro_colors)?.setOnClickListener {
            AnalyticsHelper.getInstance()?.logEvent(MaterialColor.FIREBASE_EVENT_METRO_COLORS, null)
            startActivity(Intent(this, MetroColorsActivity::class.java))
        }

        view?.findViewById<MaterialButton>(R.id.btn_support_development)?.setOnClickListener {
            AnalyticsHelper.getInstance()?.logEvent(MaterialColor.FIREBASE_EVENT_SUPPORT_DEVELOPMENT, null)
            startActivity(Intent(this, SupportDevelopmentActivity::class.java))
        }
    }

    /**
     * @func app closer functionality
     */
    private var exitTime: Long = 0
    private fun appCloser() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            this.displayToast("Press again to close app")
            exitTime = System.currentTimeMillis()
            return
        }
        finish()
    }

    /**
     * @func init audience network config for ad ---------------------------------------------------
     */
    private fun initAd() {
        Log.d(TAG, "initAd: called")
        // Interstitial Ad
        interstitialAd = InterstitialAd(this, MaterialColor.getAdId(AdType.INTERSTITIAL))
        if (Tools.hasNetwork(this))
            interstitialAd.loadAd()

        interstitialAd.setAdListener(object : InterstitialAdListener {

            override fun onInterstitialDisplayed(p0: Ad?) {

            }

            override fun onInterstitialDismissed(p0: Ad?) {

            }

            override fun onAdLoaded(p0: Ad?) {
                Log.d(TAG, "onAdLoaded: called")
            }

            override fun onError(p0: Ad?, p1: AdError?) {
                Log.d(TAG, "onError: called: ${p1?.errorMessage}")
                /*if (Tools.hasNetwork(this@HomeActivity))
                    interstitialAd.loadAd()*/
            }

            override fun onAdClicked(p0: Ad?) {

            }

            override fun onLoggingImpression(p0: Ad?) {

            }
        })

        // Banner Ad
        adView = AdView(this, MaterialColor.getAdId(AdType.BANNER), AdSize.BANNER_HEIGHT_50)
        if (Tools.hasNetwork(this))
            if (SharedPref.getInstance(this).getBoolean(StorageKey.SHOW_AD, true))
                adView?.loadAd()

        adView?.setAdListener(object : AdListener {
            override fun onAdLoaded(p0: Ad?) {
                Tools.visibleViews(binding.bannerContainer)
                binding.bannerContainer.addView(adView)
            }

            override fun onAdClicked(p0: Ad?) {

            }

            override fun onError(p0: Ad?, p1: AdError?) {
                if (Tools.hasNetwork(this@HomeActivity))
                    if (SharedPref.getInstance(this@HomeActivity).getBoolean(StorageKey.SHOW_AD, true))
                        adView?.loadAd()
            }

            override fun onLoggingImpression(p0: Ad?) {

            }
        })

    }


    /**
     * @func in-app update functionality -----------------------------------------------------------
     */
    private var updateStarted = false
    private fun initInAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        val listener = InstallStateUpdatedListener { state ->

            // Show update downloading...
            if (state.installStatus() == InstallStatus.DOWNLOADING && !updateStarted) {
                /*val bytesDownloaded = state.bytesDownloaded()
                val totalBytesToDownload = state.totalBytesToDownload()*/

                binding.rootLayout.displaySnackbar(
                    "Downloading update...",
                    Snackbar.LENGTH_LONG
                )

                updateStarted = true
            }

            // If the process of downloading is finished, start the completion flow.
            if (state.installStatus() == InstallStatus.DOWNLOADED) {

                Snackbar.make(binding.rootLayout,
                    "An update has just been downloaded from Google Play",
                    Snackbar.LENGTH_INDEFINITE
                ).apply {
                    setAction("RESTART") { appUpdateManager.completeUpdate() }
                    show()
                }

            }

            // If the downloading is failed
            if (state.installStatus() == InstallStatus.FAILED) {

                binding.rootLayout.displaySnackbar(
                    "Update downloading failed!",
                    Snackbar.LENGTH_LONG
                )

            }

        }

        // Register update listener
        appUpdateManager.registerListener(listener)

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->

            if ((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.updatePriority() >= HIGH_PRIORITY_UPDATE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE))) {

                this.displayToast("Update available")

                // Start update flow dialog
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    this,
                    REQUEST_CODE_UPDATE)

            }

            // If update is already download but, not installed
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {

                Snackbar.make(binding.rootLayout,
                    "An update has been downloaded from Google Play",
                    Snackbar.LENGTH_INDEFINITE
                ).apply {
                    setAction("RESTART") { appUpdateManager.completeUpdate() }
                    show()
                }

            }

        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
    }

}
