package com.techflow.materialcolor.activity

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivityCustomColorBinding
import com.techflow.materialcolor.utils.Preferences
import com.techflow.materialcolor.utils.SharedPref
import com.techflow.materialcolor.utils.Tools

/**
 * @author
 * Created by DILIP SUTHAR on 16/02/19
 */

class CustomColorActivity : AppCompatActivity() {

    private lateinit var bind: ActivityCustomColorBinding

    private lateinit var sharedPref: SharedPref
    var red = 0
    var green = 0
    var blue = 0
    var hexCode = "#000000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_custom_color)
        sharedPref = SharedPref.getInstance(this)

        red = sharedPref.getInt(Preferences.RED, 125)
        green = sharedPref.getInt(Preferences.GREEN, 125)
        blue = sharedPref.getInt(Preferences.BLUE, 125)

        Tools.setSystemBarColor(this, R.color.colorPrimary)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setSupportActionBar(bind.toolbar as Toolbar)
        supportActionBar?.title = "Palette creator"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        initComponent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return true
    }

    /** methods */
    private fun initComponent() {
        setResult(red, green, blue)

        bind.seekbarRed.progress = red
        bind.seekbarGreen.progress = green
        bind.seekbarBlue.progress = blue

        bind.seekbarRed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                red = p1
                setResult(red, green, blue)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                sharedPref.saveData(Preferences.RED, p0!!.progress)
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
                sharedPref.saveData(Preferences.GREEN, p0!!.progress)
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
                sharedPref.saveData(Preferences.BLUE, p0!!.progress)
            }
        })

        bind.lytHex.setOnClickListener {
            Tools.copyToClipboard(this, hexCode)
            Toast.makeText(this, "HEX code $hexCode copied on clipboard", Toast.LENGTH_SHORT).show()
        }

        bind.lytRgb.setOnClickListener {
            val rgbCode = "( $red, $green, $blue )"
            Tools.copyToClipboard(this, rgbCode)
            Toast.makeText(this, "RGB code $rgbCode copied on clipboard", Toast.LENGTH_SHORT).show()
        }

    }

    private fun rgbToHex(r: Int, g: Int, b: Int): String {
        hexCode = ""
        hexCode = "#"
        var builder = StringBuilder()
        builder.append(hexCode)
        builder.append(Integer.toHexString(r))
        hexCode = builder.toString()
        builder = StringBuilder()
        builder.append(hexCode)
        builder.append(Integer.toHexString(g))
        hexCode = builder.toString()
        builder = StringBuilder()
        builder.append(hexCode)
        builder.append(Integer.toHexString(b))
        hexCode = builder.toString()

        return hexCode
    }

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
