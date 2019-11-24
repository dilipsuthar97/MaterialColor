package com.techflow.materialcolor.fragment


import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent.Builder
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.android.billingclient.api.*
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.techflow.materialcolor.MaterialColor

import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.FragmentSettingBinding
import com.techflow.materialcolor.utils.*
/**
 * Created by DILIP SUTHAR on 16/02/19
 */
class SettingFragment : Fragment() {
    private val TAG = SettingFragment::class.java.simpleName

    companion object {
        fun getInstance(): SettingFragment {
            return SettingFragment()
        }
    }

    private lateinit var bind: FragmentSettingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)

        return bind.root
    }

    /** Methods */
    /*private fun showTutorial() {
        val sharedPref = SharedPref.getInstance(context!!)

        if (sharedPref.getBoolean(Preferences.SettingFragFR, true)) {
            TapTargetView.showFor(activity!!,
                TapTarget.forView(bind.btnRate, "Rate us", "Rate us on Google PlayStore.")
                    .outerCircleColor(R.color.colorAccent)
                    .outerCircleAlpha(0.90f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(20)
                    .titleTextColor(R.color.white)
                    .descriptionTextSize(18)
                    .descriptionTextColor(R.color.white)
                    .cancelable(false)
                    .targetRadius(50)
            )

            sharedPref.saveData(Preferences.SettingFragFR, false)
        }
    }*/
}
