package com.techflow.materialcolor.activity

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivityLicensesBinding
import com.techflow.materialcolor.helpers.displaySnackbar
import com.techflow.materialcolor.utils.ThemeUtils
import com.techflow.materialcolor.utils.Tools
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by Dilip Suthar on 17/06/2020
 */
class LicensesActivity : BaseActivity() {
    private val TAG = LicensesActivity::class.java.simpleName

    private lateinit var bind: ActivityLicensesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_licenses)

        initToolbar()
        initComponents()
    }

    /**
     * @func init toolbar config
     */
    private fun initToolbar() {
        setSupportActionBar(bind.toolbar as MaterialToolbar)
        val actionBar = supportActionBar!!
        actionBar.title = "Settings"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)
        (bind.toolbar as MaterialToolbar).elevation = 3.0F
        (bind.toolbar as MaterialToolbar).setNavigationIcon(R.drawable.ic_arrow_back)
        Tools.changeNavigationIconColor(bind.toolbar as MaterialToolbar, ThemeUtils.getThemeAttrColor(this, R.attr.colorTextPrimary))
    }

    /**
     * @func init all component's config
     */
    private fun initComponents() {
        val licensesData = StringBuilder()
        var reader: BufferedReader? = null

        try {
            reader = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.licenses)))
            var mLine: String?
            while (reader.readLine().also { mLine = it } != null) {
                licensesData.append(mLine)
                licensesData.append('\n')
            }
        } catch (e: IOException) {
            bind.root.displaySnackbar("Error reading file!", Snackbar.LENGTH_LONG)
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    //log the exception
                }
            }
        }

        bind.txtLicenses.text = licensesData
        bind.txtLicenses.movementMethod = ScrollingMovementMethod()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}