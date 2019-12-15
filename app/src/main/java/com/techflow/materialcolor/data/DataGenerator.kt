package com.techflow.materialcolor.data

import android.content.Context
import com.techflow.materialcolor.R
import com.techflow.materialcolor.model.Color
import com.techflow.materialcolor.model.Gradient

/**
 * Data generator static class
 * @param ctx context from activity
 *
 * @return return array list of <?>
 */
object DataGenerator {
    fun getColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_txt)
        val colors = ctx.resources.getIntArray(R.array.color)
        for (i in colorNames.indices) {
            items.add(Color(Color.TYPE_COLOR, colors[i], colorNames[i], colorCodes[i]))
        }
        return items
    }

    fun getGradientsData(ctx: Context): ArrayList<Gradient> {
        val items = ArrayList<Gradient>()
        val pColors = ctx.resources.getStringArray(R.array.gradients_primary_color)
        val sColors = ctx.resources.getStringArray(R.array.gradients_secondary_color)
        for (i in pColors.indices)
           items.add(Gradient(Gradient.TYPE_GRADIENT, pColors[i], sColors[i]))
        return items
    }

    fun getRedColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_red)
        val colors = ctx.resources.getIntArray(R.array.color_red)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getPinkColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_pink)
        val colors = ctx.resources.getIntArray(R.array.color_pink)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getPurpleColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_purple)
        val colors = ctx.resources.getIntArray(R.array.color_purple)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getDeepPurpleColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_deep_purple)
        val colors = ctx.resources.getIntArray(R.array.color_deep_purple)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getIndigoColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_indigo)
        val colors = ctx.resources.getIntArray(R.array.color_indigo)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getBlueColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_blue)
        val colors = ctx.resources.getIntArray(R.array.color_blue)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getLightBlueColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_light_blue)
        val colors = ctx.resources.getIntArray(R.array.color_light_blue)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getCyanColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_cyan)
        val colors = ctx.resources.getIntArray(R.array.color_cyan)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getTealColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_teal)
        val colors = ctx.resources.getIntArray(R.array.color_teal)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getGreenColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_green)
        val colors = ctx.resources.getIntArray(R.array.color_green)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getLightGreenColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_light_green)
        val colors = ctx.resources.getIntArray(R.array.color_light_green)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getLimeColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_lime)
        val colors = ctx.resources.getIntArray(R.array.color_lime)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getYellowColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_yellow)
        val colors = ctx.resources.getIntArray(R.array.color_yellow)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getAmberColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_amber)
        val colors = ctx.resources.getIntArray(R.array.color_amber)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getOrangeColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_orange)
        val colors = ctx.resources.getIntArray(R.array.color_orange)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getDeepOrangeColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_deep_orange)
        val colors = ctx.resources.getIntArray(R.array.color_deep_orange)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getBrownColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_brown)
        val colors = ctx.resources.getIntArray(R.array.color_brown)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getGreyColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_grey)
        val colors = ctx.resources.getIntArray(R.array.color_grey)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

    fun getBlueGreyColorData(ctx: Context): ArrayList<Color> {
        val items = ArrayList<Color>()
        val colorNames = ctx.resources.getStringArray(R.array.color_names_shades)
        val colorCodes = ctx.resources.getStringArray(R.array.color_code_blue_grey)
        val colors = ctx.resources.getIntArray(R.array.color_blue_grey)
        for (i in colorNames.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, colors[i], colorNames[i], colorCodes[i]))
        return items
    }

}