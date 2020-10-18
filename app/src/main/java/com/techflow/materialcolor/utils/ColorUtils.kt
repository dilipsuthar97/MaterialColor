package com.techflow.materialcolor.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.techflow.materialcolor.MaterialColor
import com.techflow.materialcolor.R
import com.techflow.materialcolor.helpers.AnalyticsHelper
import com.techflow.materialcolor.helpers.showPopup
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

object ColorUtils {

    /**
     * @func convert color's int value into hex code
     * @param color int value of color
     *
     * @return return string value of hex code
     */
    fun getHexCode(color: Int): String {
        return "#${Integer.toHexString(color).toUpperCase(Locale.getDefault()).substring(2)}"  // Without alpha
        //return "#${Integer.toHexString(color).toUpperCase()}"  // With alpha
    }

    /**
     * Convert Array of hex code string into IntArray
     * @param colorsInHex Array of string (HEX code)
     *
     * @return IntArray
     */
    fun getParsedColors(colorsInHex: Array<String>): IntArray =
        colorsInHex.map { color -> Color.parseColor(color) }.toIntArray()

    /**
     * @func convert color's rgb value into hex code
     * @param r red color value
     * @param g green color value
     * @param b blue color value
     *
     * @return string value of hex code
     */
    fun rgbToHex(r: Int, g: Int, b: Int): String {
        val rHex = if (r == 0) r.toString(16) + "0" else r.toString(16)
        val gHex = if (g == 0) g.toString(16) + "0" else g.toString(16)
        val bHex = if (b == 0) b.toString(16) + "0" else b.toString(16)

        return "#$rHex$gHex$bHex"
    }

    /**
     * Convert color's hex code into RGB object
     * @param hexCode e.g. "#FFFFFF"
     *
     * @return RGB object
     */
    fun hexToRgb(hexCode: String): RGB {
        val r = hexCode.substring(1, 3).toInt(16) // R - Red
        val g = hexCode.substring(3, 5).toInt(16) // G - Green
        val b = hexCode.substring(5, 7).toInt(16) // B - Blue

        return RGB(r, g, b)
    }

    /**
     * Convert color's hec code into HSV code
     * @param hexCode e.g. "#FFFFFF"
     *
     * @return HSV object
     */
    fun hexToHsv(hexCode: String): HSV {

        val rgb = hexToRgb(hexCode)

        val hsvArray = FloatArray(3)
        Color.RGBToHSV(rgb.red, rgb.green, rgb.blue, hsvArray)

        val df = DecimalFormat("##.#")
        df.roundingMode = RoundingMode.CEILING

        return HSV(
            "%.0f".format(hsvArray[0]),
            df.format(hsvArray[1] * 100.0),
            df.format(hsvArray[2] * 100.0)
        )
    }

    /**
     * Open popup menu for color code selector
     * @param context Context
     * @param hexCode String hex code for color
     * @param view View view to display popup on behalf
     */
    fun executeColorCodePopupMenu(context: Context, hexCode: String, view: View) {
        val popupListener = PopupMenu.OnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_to_rgb -> {
                    val rgb = ColorUtils.hexToRgb(hexCode)
                    Tools.copyToClipboard(
                        context,
                        rgb.toString(),
                        "RGB code ${rgb.toString()}"
                    )
                    AnalyticsHelper.getInstance()?.logEvent(MaterialColor.FIREBASE_EVENT_COPY_RGB_CODE, null)
                }
                R.id.action_to_hsv -> {
                    val hsv = ColorUtils.hexToHsv(hexCode)
                    Tools.copyToClipboard(
                        context,
                        hsv.toString(),
                        "HSV code ${hsv.toString()}"
                    )
                    AnalyticsHelper.getInstance()?.logEvent(MaterialColor.FIREBASE_EVENT_COPY_HSV_CODE, null)
                }
                else -> return@OnMenuItemClickListener false
            }

            true
        }

        context.showPopup(view, R.menu.menu_color_code, popupListener)
    }
}

/* ---------- Data classes ---------- */
data class RGB(var red: Int, var green: Int, var blue: Int) {
    override fun toString(): String {
        return "( $red, $green, $blue )"
    }
}

data class HSV(val hue: String, val saturation: String, val value: String) {
    override fun toString(): String {
        return "( $hue, $saturation%, $value% )"
    }
}