package com.techflow.materialcolor.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.techflow.materialcolor.utils.Tools
import android.app.ProgressDialog
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivityHomeBinding
import com.techflow.materialcolor.fragment.ColorPickerFragment
import com.techflow.materialcolor.fragment.GradientFragment
import com.techflow.materialcolor.fragment.HomeFragment
import com.techflow.materialcolor.fragment.SettingFragment
import com.techflow.materialcolor.utils.Preferences
import com.techflow.materialcolor.utils.SharedPref

/**
 * Modified by DILIP SUTHAR on 30/06/2019
 */
class HomeActivity : BaseActivity() {

    val TAG = HomeActivity::class.java.simpleName
    val BACK_STACK_ROOT_NAME = "root_fragment"

    private lateinit var binding: ActivityHomeBinding

    private lateinit var sharedPref: SharedPref
    private lateinit var progressDialog: ProgressDialog

    private lateinit var homeFragment: HomeFragment
    private lateinit var gradientFragment: GradientFragment
    private lateinit var colorPickerFragment: ColorPickerFragment
    private lateinit var settingFragment: SettingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        sharedPref = SharedPref.getInstance(this)

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

            sharedPref.saveData(Preferences.isFirstRun, value = false)
        }
    }

    private fun initBottomNavigation() {
        binding.justBar.setOnBarItemClickListener { barItem, position ->
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

    private fun initAd() {
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

}
