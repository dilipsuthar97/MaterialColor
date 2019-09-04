package com.techflow.materialcolor.fragment


import android.content.ActivityNotFoundException
import android.content.ComponentName
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
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent.Builder;
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.android.billingclient.api.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.FragmentSettingBinding
import com.techflow.materialcolor.utils.AnimUtils
import com.techflow.materialcolor.utils.Preferences
import com.techflow.materialcolor.utils.SharedPref
import com.techflow.materialcolor.utils.Tools
/**
 * Created by DILIP SUTHAR on 16/02/19
 */
class SettingFragment : Fragment(), View.OnClickListener {
    private val TAG = SettingFragment::class.java.simpleName

    companion object {
        fun getInstance(): SettingFragment {
            return SettingFragment()
        }
    }

    private lateinit var bind: FragmentSettingBinding
    private lateinit var connection: CustomTabsServiceConnection
    private var customTabsClient: CustomTabsClient? = null
    private lateinit var customTabsSession: CustomTabsSession

    // Billing
    private var skuList: ArrayList<String> = ArrayList()
    private lateinit var billingClient: BillingClient
    private lateinit var skuDetailParam: SkuDetailsParams
    private lateinit var skuDetails: SkuDetails
    private lateinit var billingFlowParams: BillingFlowParams

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)

        if (Tools.hasNetwork(context!!)) {
            setupBillingClient()
            Tools.visibleViews(bind.progressBar)
        }

        if (!SharedPref.getInstance(context!!).getBoolean(Preferences.SHOW_AD, true)) {
            bind.btnRemoveAds.isEnabled = false
        }

        bind.btnRemoveAds.setOnClickListener(this)
        bind.btnRate.setOnClickListener(this)
        bind.btnShare.setOnClickListener(this)
        bind.btnDm.setOnClickListener(this)
        bind.fabSend.setOnClickListener(this)
        bind.btnAbout.setOnClickListener(this)
        bind.btnMoreApps.setOnClickListener(this)
        bind.btnAboutMaterialColor.setOnClickListener(this)
        bind.btnMaterialTool.setOnClickListener(this)
        bind.btnPolicy.setOnClickListener(this)

        return bind.root
    }

    override fun onClick(view: View?) {
        val appUrl = "https://play.google.com/store/apps/details?id=${context?.packageName}"

        when (view?.id) {
            R.id.btn_remove_ads -> {
                AnimUtils.bounceAnim(view)
                if (Tools.hasNetwork(context!!)) {
                    Tools.visibleViews(bind.progressBar)
                    Tools.inVisibleViews(bind.btnRemoveAds, type = Tools.GONE)
                    startPurchase()
                }
            }
            R.id.btn_rate -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appUrl))
                startActivity(intent)
            }
            R.id.btn_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, appUrl)
                intent.type = "text/link"
                startActivity(intent)
            }
            R.id.btn_dm -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dilipsuthar97_dvlpr/?hl=en"))
                try {
                    intent.setPackage("com.instagram.android")
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    startActivity(intent)
                }
            }
            R.id.fab_send -> {
                val title = bind.etFeedbackTitle.text.toString()
                val msg = bind.etFeedbackMsg.text.toString()

                if (title.isEmpty() && msg.isEmpty()) {
                    bind.etFeedbackTitle.error = "Required field"
                    bind.etFeedbackMsg.error = "Required field"
                } else if (title.isEmpty()) bind.etFeedbackTitle.error = "Required field"
                else if (msg.isEmpty()) bind.etFeedbackMsg.error = "Required field"
                else {
                    val i = Intent(Intent.ACTION_SEND)
                    i.type = "message/html"
                    i.putExtra(Intent.EXTRA_EMAIL, arrayOf("techflow.developer97@gmail.com"))
                    i.putExtra(Intent.EXTRA_SUBJECT, "Feedback: MaterialColor - $title")
                    i.putExtra(Intent.EXTRA_TEXT, "Title: $title\nFeedback: $msg")
                    try {
                        startActivity(Intent.createChooser(i, "Send Feedback"))
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(activity, "There is no email client installed.", Toast.LENGTH_SHORT).show()
                    }

                    Toast.makeText(activity, "Submitting...", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btn_about -> {
                showAboutDialog()
            }
            R.id.btn_more_apps -> {}
            R.id.btn_about_material_color -> {}
            R.id.btn_material_tool -> {}
            R.id.btn_policy -> {}
        }
    }

    /** Methods */
    private fun showAboutDialog() {
        val view = View.inflate(activity, R.layout.dialog_about, null)
        MaterialDialog(context!!).show {
            cornerRadius(16f)
            customView(view = view)
        }

        view.findViewById<MaterialButton>(R.id.btn_about_me).setOnClickListener {
            openWebView("https://about.me/dilip.suthar")
        }
    }

    private fun openWebView(url: String) {
        connection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(name: ComponentName?, client: CustomTabsClient?) {
                customTabsClient = client
                customTabsClient?.warmup(0)
                customTabsSession = customTabsClient?.newSession(null)!!

            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                customTabsClient = null
            }
        }
        CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", connection)
        val intent = Builder(customTabsSession)
            .setCloseButtonIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_arrow_back))
            .setShowTitle(true)
            .setToolbarColor(ContextCompat.getColor(context!!, R.color.colorAccent))
            .build()
        intent.launchUrl(activity, Uri.parse(url))
    }

    /** Billing methods */
    private fun setupBillingClient() {
        Log.d(TAG, "setupBillingClient: called")

        skuList.add("sku_remove_ads")

        // purchase listener
        billingClient = BillingClient.newBuilder(context!!).setListener { responseCode, purchases ->
            when (responseCode) {
                0 -> {
                    Snackbar.make(bind.root, "You don't see ads anymore, Restart app.", Snackbar.LENGTH_SHORT).show()
                    SharedPref.getInstance(context!!).saveData(Preferences.SHOW_AD, false)
                }
                1 -> Toast.makeText(context, "Transaction canceled.", Toast.LENGTH_SHORT).show()
                2 -> Toast.makeText(context, "You not owned yet.", Toast.LENGTH_SHORT).show()
                7 -> {
                    SharedPref.getInstance(context!!).saveData(Preferences.SHOW_AD, false)
                    bind.btnRemoveAds.isEnabled = false
                    Snackbar.make(bind.root, "You already owned this. you don't see ads anymore, Restart app.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK") {}
                        .show()
                }
            }

            Tools.inVisibleViews(bind.progressBar, type = Tools.GONE)
            Tools.visibleViews(bind.btnRemoveAds)
        }
            .build()

        billingClient.startConnection(object : BillingClientStateListener {

            override fun onBillingServiceDisconnected() {
                Tools.inVisibleViews(bind.progressBar, type = Tools.GONE)
                Tools.visibleViews(bind.btnRemoveAds)
            }

            override fun onBillingSetupFinished(responseCode: Int) {
                if (responseCode == 0) {
                    Tools.inVisibleViews(bind.progressBar, type = Tools.GONE)
                    Tools.visibleViews(bind.btnRemoveAds)

                    skuDetailParam = SkuDetailsParams
                        .newBuilder()
                        .setSkusList(skuList)
                        .setType(BillingClient.SkuType.INAPP)
                        .build()

                    billingClient.querySkuDetailsAsync(skuDetailParam) { _, skuDetailsList ->
                        skuDetails = skuDetailsList[0] as SkuDetails
                        if (skuDetails.price != null) {
                            bind.btnRemoveAds.text = skuDetails.price   // Set product price to button text
                        }
                    }
                }
            }

        })
    }

    private fun startPurchase() {
        Log.d(TAG, "startPurchase: called")

        if (billingClient.isReady) {
            billingFlowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build()
            billingClient.launchBillingFlow(activity!!, billingFlowParams)
        }
        setupBillingClient()
    }
}
