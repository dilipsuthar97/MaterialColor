package com.techflow.materialcolor.activity

import android.os.Bundle
import com.techflow.materialcolor.utils.Tools
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.facebook.ads.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.techflow.materialcolor.MaterialColor.AdType
import com.techflow.materialcolor.MaterialColor
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivityHomeBinding
import com.techflow.materialcolor.fragment.*
import com.techflow.materialcolor.helpers.displaySnackbar
import com.techflow.materialcolor.helpers.displayToast
import com.techflow.materialcolor.utils.Preferences
import com.techflow.materialcolor.utils.SharedPref
import com.techflow.materialcolor.utils.ThemeUtils
/**
 * @author Dilip Suthar
 * Modified by Dilip Suthar on 15/12/2019
 */

/**
 *  Copyright 2019 Dilip Suthar

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
class HomeActivity : BaseActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    val TAG = HomeActivity::class.java.simpleName
    val BACK_STACK_ROOT_NAME = "root_fragment"
    val REQUEST_CODE_UPDATE = 201

    // STATIC
    companion object {
        // Audience network
        private lateinit var interstitialAd: InterstitialAd

        fun showAd(context: Context) {
            if (interstitialAd.isAdLoaded) {
                if (interstitialAd.isAdInvalidated)
                    return
                else if (SharedPref.getInstance(context).actionShowInterstitialAd())
                    interstitialAd.show()
            } else if (Tools.hasNetwork(context))
                interstitialAd.loadAd()
        }
    }

    private lateinit var binding: ActivityHomeBinding

    private lateinit var sharedPref: SharedPref

    private lateinit var homeFragment: HomeFragment
    private lateinit var gradientFragment: GradientFragment
    private lateinit var colorPickerFragment: ColorPickerFragment
    private lateinit var bookmarkedColorFragment: BookmarkedColorFragment
    private lateinit var activeFragment: Fragment

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var bottomSheet: MaterialDialog

    private var adView: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        sharedPref = SharedPref.getInstance(this)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        initToolbar()
        initComponent()
        initIntro()
        initBottomNavigation()
        initMenuSheet()
        initAd()
        initInAppUpdate()   // Init in-app update
    }

    override fun onStart() {
        super.onStart()
        this.getSharedPreferences(packageName, Context.MODE_PRIVATE)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        if (!sharedPref.getBoolean(Preferences.BOTTOM_SHEET_CONFIG, false))
            bottomSheet.cancel()
    }

    override fun onDestroy() {
        if (adView != null)
            adView?.destroy()
        super.onDestroy()
        this.getSharedPreferences(packageName, Context.MODE_PRIVATE)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onBackPressed() {
        // Back stack code
        /*val manager = supportFragmentManager
        val fragmentList = manager.fragments
        if (fragmentList.size - 1 >= 0) {
            Log.d(TAG, manager.backStackEntryCount.toString())
            val fragment = manager.fragments.last()
            manager.beginTransaction().replace(R.id.fragment, fragment)
            manager.popBackStack()
        }
        else
            appCloser()*/

        if (activeFragment != homeFragment) {

            binding.justBar.setSelected(0)
            displayFragment(homeFragment)
            activeFragment = homeFragment

        } else {
            appCloser()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        Tools.changeMenuIconColor(menu!!, ThemeUtils.getThemeAttrColor(this, R.attr.colorTextPrimary))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_menu) {
            bottomSheet.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key!! == Preferences.THEME)
            recreate()
    }

    /**
     * @func init toolbar config
     */
    private fun initToolbar() {
        setSupportActionBar(binding.toolbar as Toolbar)
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
        if (sharedPref.getBoolean(Preferences.isFirstRun, true)) {
            Snackbar.make(binding.rootLayout, "Long press on card to copy code", Snackbar.LENGTH_INDEFINITE).apply {
                setAction("NEXT") {
                    Snackbar.make(binding.rootLayout, "Tap on card to see different shades", Snackbar.LENGTH_INDEFINITE).apply {
                        setAction("CLOSE") {}
                        show()
                    }
                }
                show()
            }

            sharedPref.saveData(Preferences.isFirstRun, false)
        }
    }

    /**
     * @func init bottom navigation config
     */
    private fun initBottomNavigation() {
        binding.justBar.setOnBarItemClickListener { barItem, position ->

            // Load ad
            if (SharedPref.getInstance(this).getBoolean(Preferences.SHOW_AD, true))
                showAd(this)

            when(barItem.id) {
                R.id.nav_home -> {
                    firebaseAnalytics.logEvent(MaterialColor.FIREBASE_EVENT_TAB_COLOR, null)
                    displayFragment(homeFragment)
                    supportActionBar?.title = "MaterialColor"
                }
                R.id.nav_gradients -> {
                    firebaseAnalytics.logEvent(MaterialColor.FIREBASE_EVENT_TAB_GRADIENT, null)
                    displayFragment(gradientFragment)
                    supportActionBar?.title = "Gradients"
                }
                R.id.nav_color_picker -> {
                    firebaseAnalytics.logEvent(MaterialColor.FIREBASE_EVENT_TAB_COLOR_PICKER, null)
                    displayFragment(colorPickerFragment)
                    supportActionBar?.title = "Color Picker"
                }
                R.id.nav_bookmarked_color -> {
                    firebaseAnalytics.logEvent(MaterialColor.FIREBASE_EVENT_TAB_SETTING, null)
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
        //val backStackName = fragment?.javaClass?.name
        val fragmentManager = supportFragmentManager
        if (fragment != null) {
            activeFragment = fragment

            val transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.anim_fragment_enter, R.anim.anim_fragment_exit)
            //transaction.add(R.id.fragment, fragment, fragment.tag)
            //transaction.replace(R.id.fragment, fragment, BACK_STACK_ROOT_NAME)
            //transaction.addToBackStack(BACK_STACK_ROOT_NAME)
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

        val view = bottomSheet.getCustomView()

        view.findViewById<View>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        view.findViewById<View>(R.id.btn_custom_color_maker).setOnClickListener {
            firebaseAnalytics.logEvent(MaterialColor.FIREBASE_EVENT_CUSTOM_COLOR_MAKER, null)
            startActivity(Intent(this, CustomColorActivity::class.java))
        }

        view.findViewById<View>(R.id.btn_gradient_maker).setOnClickListener {
            firebaseAnalytics.logEvent(MaterialColor.FIREBASE_EVENT_CUSTOM_GRADIENT_MAKER, null)
            startActivity(Intent(this, CustomGradientActivity::class.java))
        }

        view.findViewById<View>(R.id.btn_material_design_tool).setOnClickListener {
            startActivity(Intent(this, DesignToolActivity::class.java))
        }
    }

    private var exitTime: Long = 0

    /**
     * @func app closer functionality
     */
    private fun appCloser() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            this.displayToast("Press again to close app")
            exitTime = System.currentTimeMillis()
            return
        }
        finish()
    }

    /**
     * @func init audience network config for ad
     */
    private fun initAd() {
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

            }

            override fun onError(p0: Ad?, p1: AdError?) {
                if (Tools.hasNetwork(this@HomeActivity))
                    interstitialAd.loadAd()
            }

            override fun onAdClicked(p0: Ad?) {

            }

            override fun onLoggingImpression(p0: Ad?) {

            }
        })

        // Banner Ad
        adView = AdView(this, MaterialColor.getAdId(AdType.BANNER), AdSize.BANNER_HEIGHT_50)
        if (Tools.hasNetwork(this))
            if (SharedPref.getInstance(this).getBoolean(Preferences.SHOW_AD, true))
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
                    if (SharedPref.getInstance(this@HomeActivity).getBoolean(Preferences.SHOW_AD, true))
                        adView?.loadAd()
            }

            override fun onLoggingImpression(p0: Ad?) {

            }
        })

    }


    /**
     * @func in-app update functionality ---------------------------------------------------------
     */
    var updateStarted = false
    private fun initInAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        val listener = InstallStateUpdatedListener { state ->

            // Show update downloading...
            if (state.installStatus() == InstallStatus.DOWNLOADING && !updateStarted) {

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
                    setAction("INSTALL") { appUpdateManager.completeUpdate() }
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
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE))) {

                this.displayToast("Update available")

                // Start update flow dialog
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    this,
                    REQUEST_CODE_UPDATE)

                // If update is already download but, not installed
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {

                    Snackbar.make(binding.rootLayout,
                        "An update has been downloaded from Google Play",
                        Snackbar.LENGTH_INDEFINITE
                    ).apply {
                        setAction("INSTALL") { appUpdateManager.completeUpdate() }
                        show()
                    }

                }

            }

        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
    }

}
