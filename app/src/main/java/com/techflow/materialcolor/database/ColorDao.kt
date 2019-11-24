package com.techflow.materialcolor.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.techflow.materialcolor.model.Color

@Dao
interface ColorDao {

    @Insert
    fun saveColor(color: Color)

    @Query("SELECT * FROM bookmarked_color")
    fun getAllColors(): List<Color>

    @Delete
    fun removeColor(color: Color)

}