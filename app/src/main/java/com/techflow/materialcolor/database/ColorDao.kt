package com.techflow.materialcolor.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.techflow.materialcolor.model.Color

@Dao
interface ColorDao {

    /**
     * @func save color object in Room
     * @param color color object
     */
    @Insert
    fun saveColor(color: Color)

    /**
     * @func return live data color object list
     *
     * @return return list of color object live data
     */
    @Query("SELECT * FROM bookmarked_color")
    fun getAllColors(): LiveData<List<Color>>

    /**
     * @func check if color code value is available in Room database
     *
     * @return return color object
     */
    @Query("SELECT * FROM bookmarked_color WHERE color_code = :colorCode LIMIT 1")
    fun checkIfColorBookmarked(colorCode: String): Color

    /**
     * @func remove color from Room using color code
     */
    @Query("DELETE FROM bookmarked_color WHERE color_code = :colorCode")
    fun removeColor(colorCode: String)

    /**
     * @func remove color object from Room
     */
    @Delete
    fun removeColor(color: Color)

}