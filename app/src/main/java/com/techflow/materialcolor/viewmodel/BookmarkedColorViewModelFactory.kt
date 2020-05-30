package com.techflow.materialcolor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.techflow.materialcolor.db.AppDatabase
/**
 * Created by Dilip Suthar on 03/12/19
 */
class BookmarkedColorViewModelFactory constructor(
    private val appDatabase: AppDatabase
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BookmarkedColorViewModel(appDatabase) as T
    }

}