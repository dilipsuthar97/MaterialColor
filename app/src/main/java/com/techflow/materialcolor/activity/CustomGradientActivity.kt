package com.techflow.materialcolor.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivityCustomGradientBinding
import com.techflow.materialcolor.utils.ThemeUtils
import com.techflow.materialcolor.utils.Tools

class CustomGradientActivity : BaseActivity() {

    private lateinit var binding: ActivityCustomGradientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_gradient)

        initToolbar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /** Methods */
    private fun initToolbar() {
        setSupportActionBar(binding.toolbar as Toolbar)
        supportActionBar?.title = "Gradient Maker"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(
            binding.toolbar as Toolbar,
            ThemeUtils.getThemeAttrColor(this, R.attr.colorTextPrimary))
    }
}
