package com.techflow.materialcolor.helpers

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
/**
 * Modified by Dilip Suthar on 15/12/19
 */
class AppExecutorHelper constructor(private val diskIO: Executor, private val multiThreadIO: Executor, private val mainThread: Executor) {

    // Static
    companion object {
        private var mInstance: AppExecutorHelper? = null

        fun getInstance(): AppExecutorHelper? {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = AppExecutorHelper(
                        Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(30),
                        MainThreadExecutor()
                    )
                }
            }

            return mInstance
        }

        /**
         * Main thread handling executor class
         */
        class MainThreadExecutor: Executor {
            private val mainThreadHandler = Handler(Looper.getMainLooper())

            override fun execute(command: Runnable) {
                mainThreadHandler.post(command)
            }

        }
    }

    fun diskIO(): Executor = diskIO
    fun multiThreadIO(): Executor = multiThreadIO
    fun mainThread(): Executor = mainThread

}