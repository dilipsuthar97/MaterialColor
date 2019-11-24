package com.techflow.materialcolor.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_color")
data class Color(

    @PrimaryKey
    var type: Int,
    var color: Int,
    @ColumnInfo(name = "color_name")
    var colorName: String,
    @ColumnInfo(name = "color_code")
    var colorCode: String) {

    companion object {
        const val TYPE_COLOR = 0
        const val TYPE_COLOR_SHADE = 1
        const val TYPE_AD = 2
    }

    /*constructor(type: Int, color: Int, colorName: String, colorCode: String):
            this(null, type, color, colorName, colorCode)*/

}