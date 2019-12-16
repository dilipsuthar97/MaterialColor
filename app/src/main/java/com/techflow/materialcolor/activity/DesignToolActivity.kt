package com.techflow.materialcolor.activity

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivityDesignToolBinding
import com.techflow.materialcolor.helpers.displayToast
import com.techflow.materialcolor.helpers.isDark
import com.techflow.materialcolor.utils.Tools

/**
 * @author Dilip Suthar
 */
class DesignToolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDesignToolBinding

    private lateinit var headerView: View
    private lateinit var menu: Menu
    private var primaryColor: MutableLiveData<Int> = MutableLiveData()
    private var secondaryColor: MutableLiveData<Int> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_design_tool)

        initToolbar()
        initNavigationDrawer()
        initComponent()
        subscribeObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home_dummy, menu)
        this.menu = menu!!
        Tools.changeMenuIconColor(menu, ContextCompat.getColor(this, R.color.black))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> binding.drawerLayout.openDrawer(GravityCompat.START)
            R.id.action_setting -> this.displayToast(item.title.toString())
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * @func init toolbar config
     */
    private fun initToolbar() {
        setSupportActionBar(binding.toolbar as Toolbar)
        supportActionBar?.title = "Design Tool"
        supportActionBar?.setHomeButtonEnabled(true)
        (binding.toolbar as Toolbar).setNavigationIcon(R.drawable.ic_menu)
    }

    /**
     * @func init all component's config
     */
    private fun initComponent() {
        val subColors = listOf(
            resources.getIntArray(R.array.color_red),
            resources.getIntArray(R.array.color_pink),
            resources.getIntArray(R.array.color_purple),
            resources.getIntArray(R.array.color_deep_purple),
            resources.getIntArray(R.array.color_indigo),
            resources.getIntArray(R.array.color_blue),
            resources.getIntArray(R.array.color_light_blue),
            resources.getIntArray(R.array.color_cyan),
            resources.getIntArray(R.array.color_teal),
            resources.getIntArray(R.array.color_green),
            resources.getIntArray(R.array.color_light_green),
            resources.getIntArray(R.array.color_lime),
            resources.getIntArray(R.array.color_yellow),
            resources.getIntArray(R.array.color_amber),
            resources.getIntArray(R.array.color_orange),
            resources.getIntArray(R.array.color_deep_orange),
            resources.getIntArray(R.array.color_brown),
            resources.getIntArray(R.array.color_grey),
            resources.getIntArray(R.array.color_blue_grey)
        ).toTypedArray()

        binding.btnPrimaryColor.setOnClickListener {

            val colors = resources.getIntArray(R.array.color)

            MaterialDialog(this).show {
                cornerRadius(16f)
                title(text = "Primary Color")
                colorChooser(
                    colors,
                    subColors = subColors,
                    allowCustomArgb = true,
                    showAlphaSelector = true
                ) { dialog, color ->
                    primaryColor.value = color
                }
            }
        }

        binding.btnSecondaryColor.setOnClickListener {

            val colors = resources.getIntArray(R.array.color)

            MaterialDialog(this).show {
                cornerRadius(16f)
                title(text = "Secondary Color")
                colorChooser(
                    colors,
                    subColors = subColors,
                    allowCustomArgb = true,
                    showAlphaSelector = true
                ) { dialog, color ->
                    secondaryColor.value = color
                }
            }
        }

        binding.fab.setOnClickListener {
            this.displayToast("Fab clicked")
        }
    }

    /**
     * @func init navigation drawer config
     */
    private fun initNavigationDrawer() {

        val drawerHeader = binding.navView.getHeaderView(0)
        headerView = drawerHeader.findViewById(R.id.nav_header)

        Tools.changeNavigationIconColor(
            binding.toolbar as Toolbar,
            ContextCompat.getColor(this, R.color.black))
    }

    /**
     * @func subscribe all live data observer
     */
    private fun subscribeObserver() {
        primaryColor.observe(this, Observer {

            binding.toolbar.setBackgroundColor(it)
            Tools.setSystemBarColorById(this, it)

            if (it.isDark()) {

                (binding.toolbar as Toolbar).setTitleTextColor(ContextCompat.getColor(this, R.color.colorTextPrimary_dark))
                Tools.changeNavigationIconColor(
                    binding.toolbar as Toolbar,
                    ContextCompat.getColor(this, R.color.colorTextPrimary_dark))
                Tools.clearSystemBarLight(this)
                Tools.changeMenuIconColor(menu, ContextCompat.getColor(this, R.color.colorTextPrimary_dark))

            } else {

                (binding.toolbar as Toolbar).setTitleTextColor(ContextCompat.getColor(this, R.color.colorTextPrimary))
                Tools.changeNavigationIconColor(
                    binding.toolbar as Toolbar,
                    ContextCompat.getColor(this, R.color.colorTextPrimary))
                Tools.setSystemBarLight(this)
                Tools.changeMenuIconColor(menu, ContextCompat.getColor(this, R.color.colorTextPrimary))

            }

        })

        secondaryColor.observe(this, Observer {

            headerView.setBackgroundColor(it)
            binding.fab.supportBackgroundTintList = ColorStateList.valueOf(it)
            binding.btnPrimaryColor.setTextColor(it)
            binding.btnSecondaryColor.setTextColor(it)

            if (it.isDark())
                binding.fab.setColorFilter(ContextCompat.getColor(this, R.color.colorTextPrimary_dark))
            else
                binding.fab.setColorFilter(ContextCompat.getColor(this, R.color.colorTextPrimary))

        })
    }
}
