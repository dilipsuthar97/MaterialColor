package com.techflow.materialcolor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.techflow.materialcolor.db.AppDatabase
import com.techflow.materialcolor.model.Color
/**
 * Created by Dilip on 03/12/19
 */
class BookmarkedColorViewModel constructor(appDatabase: AppDatabase): ViewModel() {

    private var bookmarkedColors: LiveData<List<Color>>? = null

    /**
     * initialize of constructor call
     */
    init {
        bookmarkedColors = appDatabase.colorDao()?.getAllColors()
    }

    /**
     * @func get color object list
     *
     * @return return live data of color object list
     */
    fun getBookmarkedColors(): LiveData<List<Color>>? = bookmarkedColors

}