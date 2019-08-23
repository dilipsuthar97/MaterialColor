package com.techflow.materialcolor.data

import android.content.Context
import com.techflow.materialcolor.R
import com.techflow.materialcolor.model.Color
import com.techflow.materialcolor.model.Gradient

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

    fun getGradientsData(ctx: Context): List<Gradient> {
        val items = ArrayList<Gradient>()
        val pColors = ctx.resources.getStringArray(R.array.gradients_primary_color)
        val sColors = ctx.resources.getStringArray(R.array.gradients_secondary_color)
        for (i in pColors.indices)
           items.add(Gradient(pColors[i], sColors[i]))
        return items
    }

    fun getRedColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_red)
        val list_color = ctx.resources.getIntArray(R.array.color_red)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getPinkColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_pink)
        val list_color = ctx.resources.getIntArray(R.array.color_pink)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getPurpleColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_purple)
        val list_color = ctx.resources.getIntArray(R.array.color_purple)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getDeepPurpleColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_deep_purple)
        val list_color = ctx.resources.getIntArray(R.array.color_deep_purple)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getIndigoColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_indigo)
        val list_color = ctx.resources.getIntArray(R.array.color_indigo)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getBlueColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_blue)
        val list_color = ctx.resources.getIntArray(R.array.color_blue)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getLightBlueColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_light_blue)
        val list_color = ctx.resources.getIntArray(R.array.color_light_blue)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getCyanColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_cyan)
        val list_color = ctx.resources.getIntArray(R.array.color_cyan)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getTealColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_teal)
        val list_color = ctx.resources.getIntArray(R.array.color_teal)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getGreenColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_green)
        val list_color = ctx.resources.getIntArray(R.array.color_green)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getLightGreenColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_light_green)
        val list_color = ctx.resources.getIntArray(R.array.color_light_green)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getLimeColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_lime)
        val list_color = ctx.resources.getIntArray(R.array.color_lime)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getYellowColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_yellow)
        val list_color = ctx.resources.getIntArray(R.array.color_yellow)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getAmberColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_amber)
        val list_color = ctx.resources.getIntArray(R.array.color_amber)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getOrangeColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_orange)
        val list_color = ctx.resources.getIntArray(R.array.color_orange)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getDeepOrangeColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades_with_A)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_deep_orange)
        val list_color = ctx.resources.getIntArray(R.array.color_deep_orange)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getBrownColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_brown)
        val list_color = ctx.resources.getIntArray(R.array.color_brown)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getGreyColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_grey)
        val list_color = ctx.resources.getIntArray(R.array.color_grey)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

    fun getBlueGreyColorData(ctx: Context): List<Color> {
        val items = ArrayList<Color>()
        val list_color_name = ctx.resources.getStringArray(R.array.color_names_shades)
        val list_color_code = ctx.resources.getStringArray(R.array.color_code_blue_grey)
        val list_color = ctx.resources.getIntArray(R.array.color_blue_grey)
        for (i in list_color_name.indices)
            items.add(Color(Color.TYPE_COLOR_SHADE, list_color[i], list_color_name[i], list_color_code[i]))
        return items
    }

}