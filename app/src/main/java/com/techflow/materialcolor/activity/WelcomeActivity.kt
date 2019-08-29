package com.techflow.materialcolor.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import com.techflow.materialcolor.R
import com.techflow.materialcolor.utils.Tools

/**
 * Created by DILIP SUTHAR on 16/02/19
 */

class WelcomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        initComponent()

        findViewById<TextView>(R.id.tv_bottom_msg).text = (StringBuilder()
            .append("Made with ")
            .append(String(Character.toChars(0x2764)))
            .append(" by ${resources.getString(R.string.brand_name)}"))

    }

    private fun initComponent() {
        Handler().postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 800)
    }
}
