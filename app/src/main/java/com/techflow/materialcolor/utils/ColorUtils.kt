package com.techflow.materialcolor.utils

import android.graphics.Color

object ColorUtils {

    fun getParsedColors(colorsInHex: Array<String>): IntArray =
        colorsInHex.map { color -> Color.parseColor(color) }.toIntArray()

}