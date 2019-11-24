package com.techflow.materialcolor.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.techflow.materialcolor.model.Color

@Database(entities = [Color::class], version = 1, exportSchema = false)
public abstract class AppDatabase : RoomDatabase() {

    companion object {
        private val TAG = AppDatabase.javaClass.simpleName
        private var mInstance: AppDatabase? = null
        private const val DATABASE_NAME = "color_database"

        fun getInstance(context: Context): AppDatabase? {
            if (mInstance == null) {
                synchronized(this) {
                    Log.d(TAG, "Creating new database instance")
                    mInstance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME).build()
                }
            }

            Log.d(TAG, "Getting the database instance")
            return mInstance
        }

    }

    abstract fun colorDao(): ColorDao?

}