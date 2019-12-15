package com.techflow.materialcolor.activity

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivityCustomColorBinding
import com.techflow.materialcolor.utils.Preferences
import com.techflow.materialcolor.utils.SharedPref
import com.techflow.materialcolor.utils.ThemeUtils
import com.techflow.materialcolor.utils.Tools
/**
 * Created by Dilip Suthar on 16/02/19
 */
class CustomColorActivity : BaseActivity() {

    private lateinit var bind: ActivityCustomColorBinding

    private lateinit var sharedPref: SharedPref
    private var red = 0
    private var green = 0
    private var blue = 0
    private var hexCode = "#000000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_custom_color)
        sharedPref = SharedPref.getInstance(this)

        red = sharedPref.getInt(Preferences.RED, 125)
        green = sharedPref.getInt(Preferences.GREEN, 125)
        blue = sharedPref.getInt(Preferences.BLUE, 125)

        initToolbar()
        initComponent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return true
    }

    /**
     * @func init toolbar config
     */
    private fun initToolbar() {
        setSupportActionBar(bind.toolbar as Toolbar)
        supportActionBar?.title = "Palette creator"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        Tools.changeNavigationIconColor(bind.toolbar as Toolbar, ThemeUtils.getThemeAttrColor(this, R.attr.colorTextPrimary))
    }

    /**
     * @func init all component's config
     */
    private fun initComponent() {
        setResult(red, green, blue)

        bind.seekbarRed.progress = red
        bind.seekbarGreen.progress = green
        bind.seekbarBlue.progress = blue

        // Listeners
        bind.seekbarRed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                red = progress
                setResult(red, green, blue)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                sharedPref.saveData(Preferences.RED, p0?.progress ?: 0)
            }
        })

        bind.seekbarGreen.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                green = p1
                setResult(red, green, blue)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                sharedPref.saveData(Preferences.GREEN, p0?.progress ?: 0)
            }
        })

        bind.seekbarBlue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                blue = p1
                setResult(red, green, blue)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                sharedPref.saveData(Preferences.BLUE, p0?.progress ?: 0)
            }
        })

        bind.lytHex.setOnClickListener {
            Tools.copyToClipboard(this, hexCode, "HEX code $hexCode")
        }

        bind.lytRgb.setOnClickListener {
            val rgbCode = "( $red, $green, $blue )"
            Tools.copyToClipboard(this, rgbCode, "RGB code $rgbCode")
        }

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

    /**
     * @func set color results into UI
     * @param r red color value
     * @param g green color value
     * @param b blue color value
     */
    private fun setResult(r: Int, g: Int, b: Int) {
        bind.tvRgbCode.text = "( $r, $g, $b )"
        hexCode = rgbToHex(r, g, b)
        bind.tvHexCode.text = hexCode

        bind.tvRed.text = r.toString()
        bind.tvGreen.text = g.toString()
        bind.tvBlue.text = b.toString()

        bind.viewColor.setBackgroundColor(Color.rgb(r, g, b))
    }
}
