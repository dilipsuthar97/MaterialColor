package com.techflow.materialcolor.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.techflow.materialcolor.R
import com.techflow.materialcolor.helpers.RemoteConfigHelper
import com.techflow.materialcolor.helpers.isDebug
import com.techflow.materialcolor.utils.SharedPref
import com.techflow.materialcolor.utils.StorageKey

/**
 * Created by Dilip Suthar on 16/02/19
 */

/**
 *  Copyright 2019 Dilip Suthar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
class WelcomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        initComponent()

        // Disable crashlytics in DEBUG mode
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!isDebug())

        // Activate remote config
        RemoteConfigHelper.getInstance().activate()

        findViewById<TextView>(R.id.tv_bottom_msg).text = (StringBuilder()
            .append("Made with ")
            .append(String(Character.toChars(0x2764)))
            .append(" by ${resources.getString(R.string.author_name)}"))

    }

    /**
     * @func init all component's config
     */
    private fun initComponent() {
        Handler().postDelayed({
            if (SharedPref.getInstance(this).getBoolean(StorageKey.isFirstRun, true))
                startActivity(Intent(this, AppIntroActivity::class.java))
            else
                startActivity(Intent(this, HomeActivity::class.java))

            finish()
        }, 500)
    }
}
