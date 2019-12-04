package com.techflow.materialcolor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.techflow.materialcolor.database.AppDatabase
import com.techflow.materialcolor.model.Color
/**
 * Created by Dilip on 03/12/19
 */
class BookmarkedColorViewModel constructor(appDatabase: AppDatabase): ViewModel() {

    private var bookmarkedColors: LiveData<List<Color>>? = null

    init {
        bookmarkedColors = appDatabase.colorDao()?.getAllColors()
    }

    fun getBookmarkedColors(): LiveData<List<Color>>? = bookmarkedColors

}