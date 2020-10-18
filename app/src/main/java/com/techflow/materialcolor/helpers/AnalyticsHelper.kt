package com.techflow.materialcolor.helpers

import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class AnalyticsHelper {

    var analytics: FirebaseAnalytics = Firebase.analytics

    companion object {
        private val TAG = AnalyticsHelper::class.java.simpleName

        private var mInstance: AnalyticsHelper? = null
        fun getInstance(): FirebaseAnalytics? {
            if (mInstance == null) {
                Log.d(TAG, "getInstance: initialized")
                synchronized(this) {
                    mInstance = AnalyticsHelper()
                }
            }
            return mInstance?.analytics
        }
    }

}