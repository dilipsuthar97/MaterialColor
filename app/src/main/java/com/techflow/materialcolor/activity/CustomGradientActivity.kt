package com.techflow.materialcolor.activity

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivityCustomGradientBinding
import com.techflow.materialcolor.utils.*

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

        pRed = sharedPref.getInt(Preferences.PRIMARY_GRADIENT_RED, 255)
        pGreen = sharedPref.getInt(Preferences.PRIMARY_GRADIENT_GREEN, 0)
        pBlue = sharedPref.getInt(Preferences.PRIMARY_GRADIENT_BLUE, 255)

        sRed = sharedPref.getInt(Preferences.SECONDARY_GRADIENT_RED, 0)
        sGreen = sharedPref.getInt(Preferences.SECONDARY_GRADIENT_GREEN, 255)
        sBlue = sharedPref.getInt(Preferences.SECONDARY_GRADIENT_BLUE, 255)

        initToolbar()
        initComponent()
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
        setSupportActionBar(binding.toolbar as Toolbar)
        supportActionBar?.title = "Gradient Maker"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(
            binding.toolbar as Toolbar,
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
            Tools.copyToClipboard(
                this,
                rgbToHex(pRed, pGreen, pBlue),
                "Primary color ${rgbToHex(pRed, pGreen, pBlue)}")
        }

        binding.btnSecondaryColor.setOnClickListener {
            AnimUtils.bounceAnim(it)
            Tools.copyToClipboard(
                this,
                rgbToHex(sRed, sGreen, sBlue),
                "Secondary color ${rgbToHex(sRed, sGreen, sBlue)}")
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
                sharedPref.saveData(Preferences.PRIMARY_GRADIENT_RED, seekBar?.progress ?: 0)
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
                sharedPref.saveData(Preferences.PRIMARY_GRADIENT_GREEN, seekBar?.progress ?: 0)
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
                sharedPref.saveData(Preferences.PRIMARY_GRADIENT_BLUE, seekBar?.progress ?: 0)
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
                sharedPref.saveData(Preferences.SECONDARY_GRADIENT_RED, seekBar?.progress ?: 0)
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
                sharedPref.saveData(Preferences.SECONDARY_GRADIENT_GREEN, seekBar?.progress ?: 0)
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
                sharedPref.saveData(Preferences.SECONDARY_GRADIENT_BLUE, seekBar?.progress ?: 0)
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

        binding.tvPrimaryColor.text = rgbToHex(gradient.primaryColor.red, gradient.primaryColor.green, gradient.primaryColor.blue)
        binding.tvSecondaryColor.text = rgbToHex(gradient.secondaryColor.red, gradient.secondaryColor.green, gradient.secondaryColor.blue)

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
     * @func convert color's rgb value into hex code
     * @param r red color value
     * @param g green color value
     * @param b blue color value
     *
     * @return string value of hex code
     */
    private fun rgbToHex(r: Int, g: Int, b: Int): String =
        "#${Integer.toHexString(r)}${Integer.toHexString(g)}${Integer.toHexString(b)}"

}

/**
 * Custom gradient data class
 */
data class GradientCustom(
    var primaryColor: RGB,
    var secondaryColor: RGB
)

data class RGB(
    var red: Int,
    var green: Int,
    var blue: Int
)
