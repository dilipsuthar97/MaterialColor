package com.techflow.materialcolor.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_color")
data class Color constructor(

    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var type: Int,
    var color: Int,
    @ColumnInfo(name = "color_name")
    var colorName: String,
    @ColumnInfo(name = "color_code")
    var colorCode: String,
    var bookmarked: Boolean
    )
{

    companion object {
        const val TYPE_COLOR = 0
        const val TYPE_COLOR_SHADE = 1
        const val TYPE_AD = 2
    }

    @Ignore
    constructor(type: Int, color: Int, colorName: String, colorCode: String):
            this(null, type, color, colorName, colorCode, false)
}