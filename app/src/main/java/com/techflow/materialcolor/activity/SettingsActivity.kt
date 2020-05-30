package com.techflow.materialcolor.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.techflow.materialcolor.MaterialColor
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivitySettingsBinding
import com.techflow.materialcolor.helpers.displayToast
import com.techflow.materialcolor.helpers.isDebug
import com.techflow.materialcolor.helpers.openWebView
import com.techflow.materialcolor.utils.*

/**
 * @author Dilip Suthar
 */
class SettingsActivity : BaseActivity(), View.OnClickListener, PurchasesUpdatedListener, BillingClientStateListener {
    private val TAG = SettingsActivity::class.java.simpleName

    private lateinit var bind: ActivitySettingsBinding

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    // Billing
    private var skuList: ArrayList<String> = ArrayList()
    private lateinit var billingClient: BillingClient
    private lateinit var skuDetailsParam: SkuDetailsParams
    private lateinit var skuDetails: SkuDetails
    private lateinit var billingFlowParams: BillingFlowParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        initToolbar()
        showTutorial()

        // Setup billing client for in-app purchase
        if (Tools.hasNetwork(this)) {
            if (!this.isDebug()) setupBillingClient()
            Tools.visibleViews(bind.progressBar)
            Tools.inVisibleViews(bind.btnRemoveAds, type = Tools.InvisibilityType.GONE)
        }
        if (!SharedPref.getInstance(this).getBoolean(StorageKey.SHOW_AD, true)) {
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
        bind.labelResetTutorial.setOnClickListener(this)

        // Theme setting
        bind.switchChangeTheme.isChecked = ThemeUtils.getTheme(this) == ThemeUtils.DARK

        bind.switchChangeTheme.setOnCheckedChangeListener { compoundButton, isChecked ->
            firebaseAnalytics.logEvent(MaterialColor.FIREBASE_EVENT_DARK_MODE, null)

            if (isChecked) ThemeUtils.setTheme(this, ThemeUtils.DARK)
            else ThemeUtils.setTheme(this, ThemeUtils.LIGHT)
            recreate()
        }

        // Bottom sheet config setting
        bind.switchBottomSheetConfig.isChecked =
            SharedPref.getInstance(this).getBoolean(StorageKey.BOTTOM_SHEET_CONFIG, false)

        bind.switchBottomSheetConfig.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                SharedPref.getInstance(this).saveData(StorageKey.BOTTOM_SHEET_CONFIG, true)
            else
                SharedPref.getInstance(this).saveData(StorageKey.BOTTOM_SHEET_CONFIG, false)
        }
    }

    override fun onClick(view: View?) {
        val appUrl = "https://play.google.com/store/apps/details?id=${packageName}"

        when (view?.id) {
            R.id.btn_remove_ads -> {
                AnimUtils.bounceAnim(view)
                firebaseAnalytics.logEvent(MaterialColor.FIREBASE_EVENT_REMOVE_ADS, null)
                if (Tools.hasNetwork(this)) {
                    Tools.visibleViews(bind.progressBar)
                    Tools.inVisibleViews(bind.btnRemoveAds, type = Tools.InvisibilityType.GONE)
                    if (::billingClient.isInitialized)
                        startPurchase()
                } else
                    displayToast("No active network")
            }
            R.id.btn_rate -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appUrl))
                startActivity(intent)
            }
            R.id.btn_share -> {
                val msg = "MaterialColor is an amazing color tool app, you should try it.\nDownload it from Play Store using below link."

                val shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setText("$msg\n\n$appUrl")
                    .setType("text/plain")
                    .intent

                if (shareIntent.resolveActivity(this.packageManager) != null)
                    startActivity(shareIntent)
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
                    val i = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "message/rfc822"
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("techflow.developer97@gmail.com"))
                        putExtra(Intent.EXTRA_SUBJECT, "Feedback: MaterialColor - $title")
                        putExtra(Intent.EXTRA_TEXT, "Title: $title\nFeedback: $msg")
                    }

                    if (i.resolveActivity(packageManager) != null) {
                        try {
                            startActivity(Intent.createChooser(i, "Send Feedback"))
                        } catch (e: ActivityNotFoundException) {
                            displayToast("There is no email client installed.")
                        }
                    }

                    displayToast("Submitting...")
                }
            }
            R.id.btn_about -> {
                showAboutDialog()
            }
            R.id.btn_more_apps -> { openWebView("https://play.google.com/store/apps/dev?id=5025665333769403890") }
            R.id.btn_about_material_color -> { openWebView("https://material.io/design/color/the-color-system.html#color-theme-creation") }
            R.id.btn_material_tool -> { openWebView("https://material.io/tools/color/#!/?view.left=0&view.right=0") }
            R.id.btn_policy -> {
                firebaseAnalytics.logEvent(MaterialColor.FIREBASE_EVENT_READ_POLICY, null)
                openWebView("https://github.com/dilipsuthar1997/PrivacyPolicy/blob/master/MaterialColor%20Privacy%20Policy.md")
            }
            R.id.label_reset_tutorial -> {
                resetTutorial(SharedPref.getInstance(this))
                displayToast("Tutorial reset successful :)")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    /**
     * @func init toolbar config
     */
    private fun initToolbar() {
        setSupportActionBar(bind.toolbar as MaterialToolbar)
        val actionBar = supportActionBar!!
        actionBar.title = "Settings"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)
        (bind.toolbar as MaterialToolbar).setNavigationIcon(R.drawable.ic_arrow_back)
        Tools.changeNavigationIconColor(bind.toolbar as MaterialToolbar, ThemeUtils.getThemeAttrColor(this, R.attr.colorTextPrimary))
    }

    /**
     * @func open about us dialog
     */
    private fun showAboutDialog() {
        val view = View.inflate(this, R.layout.dialog_about, null)
        val dialog = MaterialDialog(this).show {
            cornerRadius(16f)
            customView(view = view)
        }

        view.findViewById<MaterialButton>(R.id.btn_about_me).setOnClickListener {
            firebaseAnalytics.logEvent(MaterialColor.FIREBASE_EVENT_ABOUT_ME, null)
            openWebView("https://dilipsuthar97.github.io/")
            dialog.dismiss()
        }
    }

    /**
     * @func show app intro for first use
     */
    private fun showTutorial() {
        val sharedPref = SharedPref.getInstance(this)

        if (sharedPref.getBoolean(StorageKey.SettingActFR, true)) {
            TapTargetView.showFor(this,
                TapTarget.forView(bind.btnRate, "Rate us", "Rate us on Google PlayStore.")
                    .outerCircleColor(R.color.colorPrimary)
                    .outerCircleAlpha(0.95F)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(20)
                    .titleTextColor(R.color.white)
                    .descriptionTextSize(18)
                    .descriptionTextColor(R.color.white)
                    .cancelable(false)
                    .targetRadius(50)
            )

            sharedPref.saveData(StorageKey.SettingActFR, false)
        }
    }

    /**
     * @func reset first use tutorial preview
     */
    private fun resetTutorial(sharedPref: SharedPref) {
        with(sharedPref) {
            saveData(StorageKey.isFirstRun, true)
            saveData(StorageKey.HomeFragFR, true)
            saveData(StorageKey.GradientFragFR, true)
            saveData(StorageKey.ColorPickerFragFR, true)
            saveData(StorageKey.SettingActFR, true)
            saveData(StorageKey.ColorActFR, true)
            saveData(StorageKey.CustomColorActFR, true)
            saveData(StorageKey.CustomGradientActFR, true)
            saveData(StorageKey.DesignToolActFR, true)
        }
    }

    /*
     * Billing Methods -----------------------------------------------------------------------------
     */
    /**
     * @func setup in-app purchase
     */
    private fun setupBillingClient() {
        Log.d(TAG, "setupBillingClient: called")

        skuList.clear()
        skuList.add("")   // TODO: Add in-app purchase product ID

        // Purchase update listener
        billingClient = BillingClient.newBuilder(this).setListener(this).enablePendingPurchases().build()

        // Billing client state listener
        billingClient.startConnection(this)
    }

    /**
     * @func open in-app purchase dialog flow for purchase
     */
    private fun startPurchase() {
        Log.d(TAG, "startPurchase: called")

        if (billingClient.isReady) {
            billingFlowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build()
            billingClient.launchBillingFlow(this, billingFlowParams)
        }
        //setupBillingClient()
    }

    /**
     * @inherited from *PurchasesUpdatedListener ->listen in-app billing purchase changes
     * @param billingResult BillingResult object
     * @param purchases Mutable list of Purchase object
     */
    override fun onPurchasesUpdated(billingResult: BillingResult?, purchases: MutableList<Purchase>?) {
        Tools.inVisibleViews(bind.progressBar, type = Tools.InvisibilityType.GONE)
        Tools.visibleViews(bind.btnRemoveAds)

        when (billingResult?.responseCode) {
            BillingResponseCode.SERVICE_TIMEOUT -> this.displayToast("Service time out :(")
            BillingResponseCode.SERVICE_DISCONNECTED -> this.displayToast("Billing service is disconnected :(")
            BillingResponseCode.OK -> {

                MaterialDialog(this).show {
                    cornerRadius(16f)
                    title(text = "Ad removed success")
                    message(text = "You don't see ads anymore. All ads are removed permanently." +
                            "\nDon't worry when you uninstall the app your purchase has been saved" +
                            "and after reinstall the app you have to check your purchase by tapping Pay button." +
                            "\n\nThanks for purchase :)")
                    positiveButton(text = "RESTART APP") {
                        Tools.restartApp(this@SettingsActivity)
                        displayToast("Restarting app")
                    }
                }

                // Disable ad showing
                SharedPref.getInstance(this).saveData(StorageKey.SHOW_AD, false)

            }
            BillingResponseCode.USER_CANCELED -> displayToast("Transaction canceled :|")
            BillingResponseCode.SERVICE_UNAVAILABLE -> displayToast("Service unavailable :(")
            BillingResponseCode.BILLING_UNAVAILABLE -> displayToast("Billing unavailable :(")
            BillingResponseCode.ITEM_UNAVAILABLE -> displayToast("Item unavailable :(")
            BillingResponseCode.DEVELOPER_ERROR -> displayToast("Developer error :(")
            BillingResponseCode.ERROR -> displayToast("Error :(")
            BillingResponseCode.ITEM_ALREADY_OWNED -> {

                // Disable ad showing
                SharedPref.getInstance(this).saveData(StorageKey.SHOW_AD, false)

                bind.btnRemoveAds.isEnabled = false

                MaterialDialog(this).show {
                    cornerRadius(16f)
                    title(text = "Item already owned")
                    message(text = "You already purchased this.\nyou don't see ads anymore, Restart app now..")
                    positiveButton(text = "RESTART") {
                        displayToast("Restarting app")
                        Tools.restartApp(this@SettingsActivity)
                    }
                }

            }
            BillingResponseCode.ITEM_NOT_OWNED -> displayToast("You not owned yet :(")
        }
    }

    /**
     * @inherited from *BillingClientStateListener -> listen Billing client state
     */
    override fun onBillingServiceDisconnected() {
        Tools.inVisibleViews(bind.btnRemoveAds, type = Tools.InvisibilityType.GONE)
        Tools.visibleViews(bind.progressBar)
    }

    /**
     * @inherited from *BillingClientStateListener -> listen Billing client state
     * @param billingResult BillingResult object
     */
    override fun onBillingSetupFinished(billingResult: BillingResult?) {
        if (billingResult?.responseCode == BillingResponseCode.OK) {

            Tools.inVisibleViews(bind.progressBar, type = Tools.InvisibilityType.GONE)
            Tools.visibleViews(bind.btnRemoveAds)

            skuDetailsParam = SkuDetailsParams
                .newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.INAPP)
                .build()

            billingClient.querySkuDetailsAsync(skuDetailsParam) { billingResult, skuDetailsList ->

                if (billingResult.responseCode == BillingResponseCode.OK && skuDetailsList.isNotEmpty()) {

                    for (skuDetails in skuDetailsList) {
                        this@SettingsActivity.skuDetails = skuDetails
                        val sku = skuDetails.sku
                        val price = skuDetails.price

                        if ("" == sku) {    // TODO: Add in-app purchase product ID
                            bind.btnRemoveAds.text = "Check & Pay $price"   // Set product price to button text
                        }
                    }
                }
            }
        }
    }
}
