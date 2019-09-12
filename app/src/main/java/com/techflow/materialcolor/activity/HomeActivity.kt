package com.techflow.materialcolor.activity

import android.os.Bundle
import com.techflow.materialcolor.utils.Tools
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.techflow.materialcolor.BuildConfig
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivityHomeBinding
import com.techflow.materialcolor.fragment.ColorPickerFragment
import com.techflow.materialcolor.fragment.GradientFragment
import com.techflow.materialcolor.fragment.HomeFragment
import com.techflow.materialcolor.fragment.SettingFragment
import com.techflow.materialcolor.utils.Preferences
import com.techflow.materialcolor.utils.SharedPref
import com.techflow.materialcolor.utils.ThemeUtils
/**
 * Modified by DILIP SUTHAR on 30/06/2019
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
class HomeActivity : BaseActivity() {

    val TAG = HomeActivity::class.java.simpleName
    val BACK_STACK_ROOT_NAME = "root_fragment"
    val UPDATE_RC = 201

    // STATIC
    companion object {
        // Audience
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
    private lateinit var settingFragment: SettingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        sharedPref = SharedPref.getInstance(this)

        /*binding.rootLayout.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)*/

        initToolbar()
        initComponent()
        initIntro()
        initBottomNavigation()
        initAd()
        initInAppUpdate()   // Init in-app update
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
        appCloser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        Tools.changeMenuIconColor(menu!!, ThemeUtils.getThemeAttrColor(this, R.attr.colorTextPrimary))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_custom_code)
            startActivity(Intent(this, CustomColorActivity::class.java))
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.include as Toolbar)
        supportActionBar?.title = resources.getString(R.string.app_name)
    }

    private fun initComponent() {
        homeFragment = HomeFragment.getInstance()
        gradientFragment = GradientFragment.getInstance()
        colorPickerFragment = ColorPickerFragment.getInstance()
        settingFragment = SettingFragment.getInstance()

        displayFragment(homeFragment)
    }

    private fun initIntro() {
        if (sharedPref.getBoolean(Preferences.isFirstRun, true)) {
            Snackbar.make(binding.rootLayout, "Long press on card to copy code", Snackbar.LENGTH_INDEFINITE)
                .setAction("NEXT") {
                    Snackbar.make(binding.rootLayout, "Tap on card to see different shades", Snackbar.LENGTH_INDEFINITE)
                        .setAction("CLOSE") {

                        }.show()
                }.show()

            sharedPref.saveData(Preferences.isFirstRun, false)
        }
    }

    private fun initBottomNavigation() {
        binding.justBar.setOnBarItemClickListener { barItem, position ->

            // Load ad
            if (SharedPref.getInstance(this).getBoolean(Preferences.SHOW_AD, true))
                showAd(this)

            when(barItem.id) {
                R.id.nav_home -> {
                    displayFragment(homeFragment)
                    supportActionBar?.title = "MaterialColor"
                }
                R.id.nav_gradient -> {
                    displayFragment(gradientFragment)
                    supportActionBar?.title = "Gradients"
                }
                R.id.nav_color_picker -> {
                    displayFragment(colorPickerFragment)
                    supportActionBar?.title = "Color Picker"
                }
                R.id.nav_settings -> {
                    displayFragment(settingFragment)
                    supportActionBar?.title = "Settings"
                }
            }
        }
    }

    private fun displayFragment(fragment: Fragment) {
        val backStackName = fragment.javaClass.name
        val fragmentManager = supportFragmentManager
        if (fragment != null) {
            val transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            //transaction.add(R.id.fragment, fragment, fragment.tag)
            transaction.replace(R.id.fragment, fragment, BACK_STACK_ROOT_NAME)
            //transaction.addToBackStack(BACK_STACK_ROOT_NAME)
            transaction.commit()
        }
    }

    private var exitTime: Long = 0
    private fun appCloser() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "Press again to close app", Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
            return
        }
        finish()
    }

    private fun initAd() {
        interstitialAd = InterstitialAd(this, BuildConfig.AUDIENCE_INTERSTITIAL_ID)
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
    }


    /** @method in-app update functionality */
    private fun initInAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        val listener = InstallStateUpdatedListener { state ->

            // Show update downloading...
            if (state.installStatus() == InstallStatus.DOWNLOADING) {

                Snackbar.make(binding.rootLayout,
                    "Downloading update...",
                    Snackbar.LENGTH_LONG
                ).apply {
                    show()
                }

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

                Snackbar.make(binding.rootLayout,
                    "Update downloading failed!",
                    Snackbar.LENGTH_LONG
                ).apply {
                    show()
                }

            }

        }

        // Register update listener
        appUpdateManager.registerListener(listener)

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->

            if ((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE))) {

                Toast.makeText(this, "Update available", Toast.LENGTH_SHORT).show()

                // Start update flow dialog
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    this,
                    UPDATE_RC)

            }

        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
    }

}
