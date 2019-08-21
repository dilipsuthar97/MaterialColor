package com.techflow.materialcolor.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.techflow.materialcolor.utils.Tools
import android.app.ProgressDialog
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.irozon.justbar.JustBar
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivityHomeBinding
import com.techflow.materialcolor.fragment.ColorPickerFragment
import com.techflow.materialcolor.fragment.GradientFragment
import com.techflow.materialcolor.fragment.HomeFragment
import com.techflow.materialcolor.fragment.SettingFragment
import com.techflow.materialcolor.utils.Preferences
import com.techflow.materialcolor.utils.SharedPref

/**
 * @author
 * Modified by DILIP SUTHAR on 30/06/2019
 */
class HomeActivity : AppCompatActivity() {

    val TAG = HomeActivity::class.java.simpleName

    private lateinit var binding: ActivityHomeBinding

    private lateinit var sharedPref: SharedPref
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_home
        )
        sharedPref = SharedPref.getInstance(applicationContext)

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")

        Tools.setSystemBarColor(this, R.color.colorPrimary)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        initToolbar()
        initComponent()
        initIntro()
        initBottomNavigation()
        //initAd()
    }

    override fun onBackPressed() {
        //appCloser()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding.bannerContainer != null)
            binding.bannerContainer.destroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
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
        displayFragment(HomeFragment.getInstance())
    }

    private fun initIntro() {
        if (!sharedPref.getBooleanData(Preferences.isFirstRun)) {
            Snackbar.make(binding.rootLayout, "Long press on card to copy code", Snackbar.LENGTH_INDEFINITE)
                .setAction("NEXT") {
                    Snackbar.make(binding.rootLayout, "Tap on card to see different shades", Snackbar.LENGTH_INDEFINITE)
                        .setAction("CLOSE") {

                        }.show()
                }.show()

            sharedPref.saveBooleanData(Preferences.isFirstRun, value = true)
        }
    }

    private fun initBottomNavigation() {
        binding.justBar.setOnBarItemClickListener { barItem, position ->
            when(barItem.id) {
                R.id.nav_home -> {
                    displayFragment(HomeFragment.getInstance())
                    supportActionBar?.title = "MaterialColor"
                }
                R.id.nav_gradient -> {
                    displayFragment(GradientFragment.getInstance())
                    supportActionBar?.title = "Gradients"
                }
                R.id.nav_color_picker -> {
                    displayFragment(ColorPickerFragment.getInstance())
                    supportActionBar?.title = "Color Picker"
                }
                R.id.nav_settings -> {
                    displayFragment(SettingFragment.getInstance())
                    supportActionBar?.title = "Settings"
                }
            }
        }
    }

    private fun initAd() {
        val adRequest: AdRequest = AdRequest.Builder().build()

        Handler().postDelayed({
            binding.bannerContainer.loadAd(adRequest)
        }, 1000)

        binding.bannerContainer.adListener = object: AdListener() {
            override fun onAdLoaded() {
                Log.d(TAG, "onAdLoaded: called")
                binding.bannerContainer.visibility = View.VISIBLE
            }

            override fun onAdClosed() {
                Log.d(TAG, "onAdClosed: called")
                binding.bannerContainer.visibility = View.GONE
            }

            override fun onAdFailedToLoad(p0: Int) {
                Log.d(TAG, "onAdFailedToLoad: called")
                binding.bannerContainer.loadAd(adRequest)
            }
        }

    }

    private fun displayFragment(fragment: Fragment) {
        val backStackName = fragment.javaClass.name
        val fragmentManager = supportFragmentManager
        if (fragment != null) {
            val transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            transaction.add(R.id.fragment, fragment, fragment.tag)
            // TODO: Add fragment back stack code here
            //transaction.replace(R.id.fragment, fragment)
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
}
