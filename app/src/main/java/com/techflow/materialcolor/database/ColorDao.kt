package com.techflow.materialcolor.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.techflow.materialcolor.model.Color

@Dao
interface ColorDao {

    @Insert
    fun saveColor(color: Color)

    @Query("SELECT * FROM bookmarked_color")
    fun getAllColors(): LiveData<List<Color>>

    @Query("SELECT * FROM bookmarked_color WHERE color_code = :colorCode LIMIT 1")
    fun checkIfColorBookmarked(colorCode: String): Color

    @Query("DELETE FROM bookmarked_color WHERE color_code = :colorCode")
    fun removeColor(colorCode: String)

    @Delete
    fun removeColor(color: Color)

}