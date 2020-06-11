package com.techflow.materialcolor.activity

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.MaterialToolbar
import com.techflow.materialcolor.MaterialColor
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivityCustomGradientBinding
import com.techflow.materialcolor.utils.*
import com.techflow.materialcolor.utils.ColorUtils.rgbToHex
import it.sephiroth.android.library.xtooltip.ClosePolicy
import it.sephiroth.android.library.xtooltip.Tooltip

/**
 * @author Dilip Suthar
 */
class CustomGradientActivity : BaseActivity() {
    private val TAG = CustomGradientActivity::class.java.simpleName

    private lateinit var binding: ActivityCustomGradientBinding
    private lateinit var sharedPref: SharedPref

    private var pRed = 0
    private var pGreen = 0
    private var pBlue = 0

    private var sRed = 0
    private var sGreen = 0
    private var sBlue = 0

    private lateinit var gradientCustom: GradientCustom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_gradient)
        sharedPref = SharedPref.getInstance(this)

        pRed = sharedPref.getInt(StorageKey.PRIMARY_GRADIENT_RED, 255)
        pGreen = sharedPref.getInt(StorageKey.PRIMARY_GRADIENT_GREEN, 0)
        pBlue = sharedPref.getInt(StorageKey.PRIMARY_GRADIENT_BLUE, 255)

        sRed = sharedPref.getInt(StorageKey.SECONDARY_GRADIENT_RED, 0)
        sGreen = sharedPref.getInt(StorageKey.SECONDARY_GRADIENT_GREEN, 255)
        sBlue = sharedPref.getInt(StorageKey.SECONDARY_GRADIENT_BLUE, 255)

        initToolbar()
        initComponent()
        if (SharedPref.getInstance(this).getBoolean(StorageKey.CustomGradientActFR, true))
            showTooltip()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * @func init toolbar config
     */
    private fun initToolbar() {
        setSupportActionBar(binding.toolbar as MaterialToolbar)
        supportActionBar?.title = "Gradient Maker"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (binding.toolbar as MaterialToolbar).setNavigationIcon(R.drawable.ic_arrow_back)
        Tools.changeNavigationIconColor(
            binding.toolbar as MaterialToolbar,
            ThemeUtils.getThemeAttrColor(this, R.attr.colorTextPrimary))
    }

    /**
     * @func init all component's config
     */
    private fun initComponent() {
        gradientCustom = GradientCustom(
            RGB(pRed, pGreen, pBlue),
            RGB(sRed, sGreen, sBlue)
        )

        setResult(gradientCustom)

        binding.seekbarRedPrimary.progress = pRed
        binding.seekbarGreenPrimary.progress = pGreen
        binding.seekbarBluePrimary.progress = pBlue
        binding.seekbarRedSecondary.progress = sRed
        binding.seekbarGreenSecondary.progress = sGreen
        binding.seekbarBlueSecondary.progress = sBlue

        /**
         * listeners
         */
        binding.btnPrimaryColor.setOnClickListener {
            AnimUtils.bounceAnim(it)
            HomeActivity.firebaseAnalytics.logEvent(MaterialColor.FIREBASE_EVENT_COPY_HEX_CODE, null)
            Tools.copyToClipboard(
                this,
                rgbToHex(pRed, pGreen, pBlue),
                "Primary color ${rgbToHex(pRed, pGreen, pBlue)}")
        }
        binding.btnPrimaryColor.setOnLongClickListener {
            AnimUtils.bounceAnim(it)
            ColorUtils.executeColorCodePopupMenu(
                this,
                rgbToHex(gradientCustom.primaryColor.red, gradientCustom.primaryColor.green, gradientCustom.primaryColor.blue),
                it
            )
            true
        }

        binding.btnSecondaryColor.setOnClickListener {
            AnimUtils.bounceAnim(it)
            HomeActivity.firebaseAnalytics.logEvent(MaterialColor.FIREBASE_EVENT_COPY_HEX_CODE, null)
            Tools.copyToClipboard(
                this,
                rgbToHex(sRed, sGreen, sBlue),
                "Secondary color ${rgbToHex(sRed, sGreen, sBlue)}")
        }
        binding.btnSecondaryColor.setOnLongClickListener {
            AnimUtils.bounceAnim(it)
            ColorUtils.executeColorCodePopupMenu(
                this,
                rgbToHex(gradientCustom.secondaryColor.red, gradientCustom.secondaryColor.green, gradientCustom.secondaryColor.blue),
                it
            )
            true
        }

        // Primary color seekbar listeners
        binding.seekbarRedPrimary.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                pRed = progress
                gradientCustom.primaryColor.red = pRed
                setResult(gradientCustom)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                sharedPref.saveData(StorageKey.PRIMARY_GRADIENT_RED, seekBar?.progress ?: 0)
            }
        })

        binding.seekbarGreenPrimary.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                pGreen = progress
                gradientCustom.primaryColor.green = pGreen
                setResult(gradientCustom)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                sharedPref.saveData(StorageKey.PRIMARY_GRADIENT_GREEN, seekBar?.progress ?: 0)
            }
        })

        binding.seekbarBluePrimary.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                pBlue = progress
                gradientCustom.primaryColor.blue = pBlue
                setResult(gradientCustom)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                sharedPref.saveData(StorageKey.PRIMARY_GRADIENT_BLUE, seekBar?.progress ?: 0)
            }
        })

        // Secondary color seekbar listeners
        binding.seekbarRedSecondary.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sRed = progress
                gradientCustom.secondaryColor.red = sRed
                setResult(gradientCustom)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                sharedPref.saveData(StorageKey.SECONDARY_GRADIENT_RED, seekBar?.progress ?: 0)
            }
        })

        binding.seekbarGreenSecondary.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sGreen = progress
                gradientCustom.secondaryColor.green = sGreen
                setResult(gradientCustom)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                sharedPref.saveData(StorageKey.SECONDARY_GRADIENT_GREEN, seekBar?.progress ?: 0)
            }
        })

        binding.seekbarBlueSecondary.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sBlue = progress
                gradientCustom.secondaryColor.blue = sBlue
                setResult(gradientCustom)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                sharedPref.saveData(StorageKey.SECONDARY_GRADIENT_BLUE, seekBar?.progress ?: 0)
            }
        })

    }

    /**
     * @func set color results into UI
     * @param gradient gradientCustom object
     */
    private fun setResult(gradient: GradientCustom) {
        Log.d(TAG, "HexCode_Primary: ${rgbToHex(gradient.primaryColor.red, gradient.primaryColor.green, gradient.primaryColor.blue)}")
        Log.d(TAG, "HexCode_Secondary: ${rgbToHex(gradient.secondaryColor.red, gradient.secondaryColor.green, gradient.secondaryColor.blue)}")

        binding.tvPrimaryColor.text =
            rgbToHex(gradient.primaryColor.red, gradient.primaryColor.green, gradient.primaryColor.blue)
        binding.tvSecondaryColor.text =
            rgbToHex(gradient.secondaryColor.red, gradient.secondaryColor.green, gradient.secondaryColor.blue)

        binding.tvRedPrimary.text = gradient.primaryColor.red.toString()
        binding.tvGreenPrimary.text = gradient.primaryColor.green.toString()
        binding.tvBluePrimary.text = gradient.primaryColor.blue.toString()
        binding.tvRedSecondary.text = gradient.secondaryColor.red.toString()
        binding.tvGreenSecondary.text = gradient.secondaryColor.green.toString()
        binding.tvBlueSecondary.text = gradient.secondaryColor.blue.toString()

        val gradient = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                Color.rgb(
                    gradient.primaryColor.red,
                    gradient.primaryColor.green,
                    gradient.primaryColor.blue
                ),
                Color.rgb(
                    gradient.secondaryColor.red,
                    gradient.secondaryColor.green,
                    gradient.secondaryColor.blue
                ))
        )

        gradient.cornerRadius = 0.0F
        binding.viewGradientDrawable.background = gradient

    }

    /**
     * @func show tooltip instruction
     */
    private fun showTooltip() {
        var tooltip: Tooltip? = null

        tooltip?.dismiss()

        tooltip = Tooltip.Builder(applicationContext)
            .anchor(binding.btnPrimaryColor, 0, 0, false)
            .text("tap here to copy Primary color")
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
                    .text("tap here to copy Secondary code")
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
                        .saveData(StorageKey.CustomGradientActFR, false)
                }
                    ?.show(binding.btnSecondaryColor, Tooltip.Gravity.TOP, true)

            }
                ?.show(binding.btnPrimaryColor, Tooltip.Gravity.TOP, true)
        }
    }
}

/**
 * Custom gradient data class
 */
data class GradientCustom(
    var primaryColor: RGB,
    var secondaryColor: RGB
)
