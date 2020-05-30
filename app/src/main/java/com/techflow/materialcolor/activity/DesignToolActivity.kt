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
import com.google.android.material.appbar.MaterialToolbar
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivityDesignToolBinding
import com.techflow.materialcolor.helpers.displayToast
import com.techflow.materialcolor.helpers.isDark
import com.techflow.materialcolor.utils.StorageKey
import com.techflow.materialcolor.utils.SharedPref
import com.techflow.materialcolor.utils.Tools
import it.sephiroth.android.library.xtooltip.ClosePolicy
import it.sephiroth.android.library.xtooltip.Tooltip

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
        if (SharedPref.getInstance(this).getBoolean(StorageKey.DesignToolActFR, true))
            showTooltip()

        // Show warning dialog before use
        val sharedPref = SharedPref.getInstance(this)
        if (sharedPref.getBoolean("design_tool_warning", true)) {
            MaterialDialog(this).show {
                cornerRadius(16f)
                title(text = "Attention")
                message(text = "This is just a demo preview. " +
                        "\nFeature is still in development for improving and adding more design tool functionality.")
                positiveButton(text = "OK") {

                }

                negativeButton(text = "Don't show again") {
                    SharedPref.getInstance(this@DesignToolActivity)
                        .saveData("design_tool_warning", false)
                }
            }
        }
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
        setSupportActionBar(binding.toolbar as MaterialToolbar)
        supportActionBar?.title = "Design Tool"
        supportActionBar?.setHomeButtonEnabled(true)
        (binding.toolbar as MaterialToolbar).setNavigationIcon(R.drawable.ic_menu)
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
            binding.btnPrimaryColor.setTextColor(it)
            binding.btnPrimaryColor.strokeColor = ColorStateList.valueOf(it)

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
            binding.btnSecondaryColor.setTextColor(it)
            binding.btnSecondaryColor.strokeColor = ColorStateList.valueOf(it)

            if (it.isDark())
                binding.fab.setColorFilter(ContextCompat.getColor(this, R.color.colorTextPrimary_dark))
            else
                binding.fab.setColorFilter(ContextCompat.getColor(this, R.color.colorTextPrimary))

        })
    }

    /**
     * @func show tooltip instruction
     */
    private fun showTooltip() {
        var tooltip: Tooltip? = null

        tooltip?.dismiss()

        tooltip = Tooltip.Builder(applicationContext)
            .anchor(binding.btnPrimaryColor, 0, 0, false)
            .text("Select Primary material color")
            .styleId(R.style.ToolTipAltStyle)
            .arrow(true)
            .maxWidth(resources.displayMetrics.widthPixels / 2)
            .typeface(null)
            .floatingAnimation(Tooltip.Animation.DEFAULT)
            .closePolicy(ClosePolicy.TOUCH_ANYWHERE_NO_CONSUME)
            .overlay(false)
            .create()

        binding.btnPrimaryColor.post {

            tooltip?.doOnHidden {

                tooltip = null
                tooltip = Tooltip.Builder(applicationContext)
                    .anchor(binding.btnSecondaryColor, 0, 0, false)
                    .text("Select Secondary material color")
                    .styleId(R.style.ToolTipAltStyle)
                    .arrow(true)
                    .maxWidth(resources.displayMetrics.widthPixels / 2)
                    .typeface(null)
                    .floatingAnimation(Tooltip.Animation.DEFAULT)
                    .closePolicy(ClosePolicy.TOUCH_ANYWHERE_NO_CONSUME)
                    .overlay(false)
                    .create()

                tooltip?.doOnHidden {
                    SharedPref.getInstance(this)
                        .saveData(StorageKey.DesignToolActFR, false)
                }
                    ?.show(binding.btnSecondaryColor, Tooltip.Gravity.TOP, true)

            }
                ?.show(binding.btnPrimaryColor, Tooltip.Gravity.TOP, true)
        }
    }
}
