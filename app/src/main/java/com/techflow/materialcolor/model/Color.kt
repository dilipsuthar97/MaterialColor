package com.techflow.materialcolor.model

data class Color(var type: Int, var color: Int, var colorName: String, var colorCode: String) {
    companion object {
        const val TYPE_COLOR = 0
        const val TYPE_COLOR_SHADE = 1
        const val TYPE_AD = 2
    }
}