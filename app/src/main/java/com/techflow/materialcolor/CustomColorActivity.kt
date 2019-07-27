package com.techflow.materialcolor

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.techflow.materialcolor.databinding.ActivityCustomColorBinding
import com.techflow.materialcolor.utils.Tools

/**
 * Created by Dilip on 16/02/19
 */

class CustomColorActivity : AppCompatActivity() {

    private lateinit var bind: ActivityCustomColorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_custom_color)

        Tools.setSystemBarColor(this, R.color.colorPrimary)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setSupportActionBar(bind.toolbar as Toolbar)
        supportActionBar?.title = "Palette creator"

    }
}
