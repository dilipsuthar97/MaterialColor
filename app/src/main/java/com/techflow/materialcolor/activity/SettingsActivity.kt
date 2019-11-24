package com.techflow.materialcolor.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
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
import com.techflow.materialcolor.databinding.ActivitySettingsBinding
import com.techflow.materialcolor.fragment.SettingFragment
import com.techflow.materialcolor.utils.*

class SettingsActivity : BaseActivity(), View.OnClickListener {
    private val TAG = SettingsActivity::class.java.simpleName

    private lateinit var bind: ActivitySettingsBinding

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    // Billing
    private var skuList: ArrayList<String> = ArrayList()
    private lateinit var billingClient: BillingClient
    private lateinit var skuDetailParam: SkuDetailsParams
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
            setupBillingClient()
            Tools.visibleViews(bind.progressBar)
            Tools.inVisibleViews(bind.btnRemoveAds, type = Tools.GONE)
        }
        if (!SharedPref.getInstance(this).getBoolean(Preferences.SHOW_AD, true)) {
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

        // Theme setting
        if (ThemeUtils.getTheme(this) == ThemeUtils.DARK)
            bind.switchChangeTheme.isChecked = true

        // Theme switch listener
        bind.switchChangeTheme.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) ThemeUtils.setTheme(this, ThemeUtils.DARK)
            else ThemeUtils.setTheme(this, ThemeUtils.LIGHT)
            recreate()
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
                    Tools.inVisibleViews(bind.btnRemoveAds, type = Tools.GONE)
                    if (::billingClient.isInitialized)
                        startPurchase()
                } else
                    Toast.makeText(this, "No active network", Toast.LENGTH_SHORT).show()
            }
            R.id.btn_rate -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appUrl))
                startActivity(intent)
            }
            R.id.btn_share -> {
                val msg = "MaterialColor is an amazing color tool app, you should try it.\nDownload it from below link."

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
                    val i = Intent(Intent.ACTION_SEND)
                    i.type = "message/html"
                    i.putExtra(Intent.EXTRA_EMAIL, arrayOf("techflow.developer97@gmail.com"))
                    i.putExtra(Intent.EXTRA_SUBJECT, "Feedback: MaterialColor - $title")
                    i.putExtra(Intent.EXTRA_TEXT, "Title: $title\nFeedback: $msg")
                    try {
                        startActivity(Intent.createChooser(i, "Send Feedback"))
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show()
                    }

                    Toast.makeText(this, "Submitting...", Toast.LENGTH_SHORT).show()
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
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    /** Methods */
    private fun initToolbar() {
        setSupportActionBar(bind.toolbar as Toolbar)
        val actionBar = supportActionBar!!
        actionBar.title = "Settings"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)
        Tools.changeNavigationIconColor(bind.toolbar as Toolbar, ThemeUtils.getThemeAttrColor(this, R.attr.colorTextPrimary))
    }

    private fun showAboutDialog() {
        val view = View.inflate(this, R.layout.dialog_about, null)
        val dialog = MaterialDialog(this).show {
            cornerRadius(16f)
            customView(view = view)
        }

        view.findViewById<MaterialButton>(R.id.btn_about_me).setOnClickListener {
            firebaseAnalytics.logEvent(MaterialColor.FIREBASE_EVENT_ABOUT_ME, null)
            openWebView("https://about.me/dilip.suthar")
            dialog.dismiss()
        }
    }

    /** It will open URL link into chrome custom tab
     *
     * @param url a url link in a string format
     */
    private fun openWebView(url: String) {
        val intent = CustomTabsIntent.Builder()
            .addDefaultShareMenuItem()
            .setToolbarColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setShowTitle(true)
            .enableUrlBarHiding()
            .addDefaultShareMenuItem()
            .setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
            .setExitAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
            .setCloseButtonIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_arrow_back))
            .build()

        intent.launchUrl(this, Uri.parse(url))
    }

    private fun showTutorial() {
        val sharedPref = SharedPref.getInstance(this)

        if (sharedPref.getBoolean(Preferences.SettingFragFR, true)) {
            TapTargetView.showFor(this,
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
    }

    /** Billing methods */
    private fun setupBillingClient() {
        Log.d(TAG, "setupBillingClient: called")

        skuList.add("")   // TODO: Add in-app purchase product ID

        // purchase listener
        billingClient = BillingClient.newBuilder(this).setListener { responseCode, purchases ->
            when (responseCode) {
                0 -> {

                    MaterialDialog(this).show {
                        cornerRadius(16f)
                        title(text = "Ad removed success")
                        message(text = "You don't see ads anymore. All ads are removed permanently." +
                                "\nDon't worry when you uninstall the app your purchase has been saved" +
                                "and after reinstall the app you have to check your purchase by tapping Pay button." +
                                "\n\nThanks for purchase :)")
                        positiveButton(text = "RESTART APP") {
                            Tools.restartApp(context!!)
                            Toast.makeText(context, "Restarting app", Toast.LENGTH_SHORT).show()
                        }
                    }

                    // Disable ad showing
                    SharedPref.getInstance(this).saveData(Preferences.SHOW_AD, false)

                }
                1 -> Toast.makeText(this, "Transaction canceled.", Toast.LENGTH_SHORT).show()
                2 -> Toast.makeText(this, "You not owned yet.", Toast.LENGTH_SHORT).show()
                7 -> {

                    // Disable ad showing
                    SharedPref.getInstance(this).saveData(Preferences.SHOW_AD, false)

                    bind.btnRemoveAds.isEnabled = false
                    Snackbar.make(bind.root, "You already purchased this. you don't see ads anymore, Restart app.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RESTART") {
                            Tools.restartApp(this)
                            Toast.makeText(this, "Restarting app", Toast.LENGTH_SHORT).show()
                        }
                        .show()

                }
            }

        }.build()

        billingClient.startConnection(object : BillingClientStateListener {

            override fun onBillingServiceDisconnected() {
                Tools.inVisibleViews(bind.btnRemoveAds, type = Tools.GONE)
                Tools.visibleViews(bind.progressBar)
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
                        if (skuDetailsList.isNotEmpty()) {
                            skuDetails = skuDetailsList[0] as SkuDetails
                            if (skuDetails.price != null) {
                                bind.btnRemoveAds.text = "Check & Pay ${skuDetails.price}"   // Set product price to button text
                            }
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
            billingClient.launchBillingFlow(this, billingFlowParams)
        }
        setupBillingClient()
    }
}
