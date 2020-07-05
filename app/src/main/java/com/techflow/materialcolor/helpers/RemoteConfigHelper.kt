package com.techflow.materialcolor.helpers

import android.content.Context
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.techflow.materialcolor.MaterialColor
import com.techflow.materialcolor.R

class RemoteConfigHelper() {
    private val TAG = RemoteConfigHelper::class.java.simpleName

    var remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    companion object {
        const val KEY_SUPPORT_EMAIL = "support_email"
        const val KEY_PORTFOLIO_URL = "portfolio_url"
        const val KEY_DEFAULT_THEME = "default_theme"

        private var mInstance: RemoteConfigHelper? = null
        fun getInstance(): RemoteConfigHelper {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = RemoteConfigHelper()
                }
            }
            return mInstance as RemoteConfigHelper
        }
    }

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    fun activate() {
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Log.d(TAG, "Fetch and activate succeeded: $updated")

            } else {
                Log.d(TAG, "Something went wrong with fetching the data.")
            }
        }
    }

}