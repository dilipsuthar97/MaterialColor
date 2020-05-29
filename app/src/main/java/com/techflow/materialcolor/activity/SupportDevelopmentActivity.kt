package com.techflow.materialcolor.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.android.billingclient.api.*
import com.techflow.materialcolor.MaterialColor
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivitySupportDevelopmentBinding
import com.techflow.materialcolor.helpers.displayToast
import com.techflow.materialcolor.helpers.isDebug
import com.techflow.materialcolor.utils.*

/**
 * @author Dilip Suthar
 */
class SupportDevelopmentActivity : BaseActivity(), PurchasesUpdatedListener, BillingClientStateListener {
    private val TAG = SupportDevelopmentActivity::class.java.simpleName

    private lateinit var binding: ActivitySupportDevelopmentBinding

    private lateinit var coke: SkuDetails
    private lateinit var coffee: SkuDetails
    private lateinit var meal: SkuDetails
    private lateinit var pizza: SkuDetails
    private lateinit var tshirt: SkuDetails

    // Billing
    private lateinit var billingClient: BillingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_support_development)

        startLoader()
        if (Tools.hasNetwork(this)) {
            if (!this.isDebug()) setupBillingClient()

        } else displayToast("Make sure you have active internet")

        initToolbar()
        initComponent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return true
    }

    /**
     * @func init toolbar config
     */
    private fun initToolbar() {
        setSupportActionBar(binding.toolbar as Toolbar)
        supportActionBar?.let {
            it.title = "Support Development"
            it.setHomeButtonEnabled(true)
        }
        (binding.toolbar as Toolbar).setNavigationIcon(R.drawable.ic_arrow_back)
        Tools.changeNavigationIconColor(binding.toolbar as Toolbar, ThemeUtils.getThemeAttrColor(this, R.attr.colorTextPrimary))
    }

    /**
     * @func init all component's config
     */
    private fun initComponent() {
        binding.cardView1.setOnClickListener {
            startPurchase(coke)
        }

        binding.cardView2.setOnClickListener {
            startPurchase(coffee)
        }

        binding.cardView3.setOnClickListener {
            startPurchase(meal)
        }

        binding.cardView4.setOnClickListener {
            startPurchase(pizza)
        }

        binding.cardView5.setOnClickListener {
            startPurchase(tshirt)
        }
    }

    /**
     * @func start loader and disable view
     */
    private fun startLoader() {
        Tools.visibleViews(binding.loader)
        Tools.disableViews(
            binding.cardView1,
            binding.cardView2,
            binding.cardView3,
            binding.cardView4,
            binding.cardView5
        )
    }

    /**
     * @func end loader and enable view
     */
    private fun endLoader() {
        Tools.inVisibleViews(binding.loader, type = Tools.InvisibilityType.GONE)
        Tools.enableViews(
            binding.cardView1,
            binding.cardView2,
            binding.cardView3,
            binding.cardView4,
            binding.cardView5
        )
    }


    /*
     * Billing Methods -----------------------------------------------------------------------------
     */
    /**
     * @func setup in-app purchase
     */
    // TODO: Add in-app purchase product ID
    private val skuList = arrayListOf("", "", "", "", "")

    private fun setupBillingClient() {
        Log.d(TAG, "setupBillingClient: called")

        // Purchase update listener
        billingClient = BillingClient.newBuilder(this).setListener(this).enablePendingPurchases().build()

        // Billing client state listener
        billingClient.startConnection(this)
    }

    /**
     * @func open in-app purchase dialog flow for purchase
     */
    private fun startPurchase(skuDetails: SkuDetails) {
        Log.d(TAG, "startPurchase: called")

        startLoader()

        if (Tools.hasNetwork(this)) {
            if (billingClient.isReady) {
                val billingFlowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build()
                billingClient.launchBillingFlow(this, billingFlowParams)
            }
        } else
            displayToast("Make sure you have active internet")
    }

    /**
     * @func one-time product specific feature
     */
    private fun allowMultiplePurchases(purchases: MutableList<Purchase>?) {
        val purchase = purchases?.first()
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase?.purchaseToken)
            .setDeveloperPayload(purchase?.developerPayload)
            .build()

        billingClient.consumeAsync(consumeParams) { billingResult, outToken ->
            if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK)
                Log.d(TAG, "allowMultiplePurchases: Token: $outToken")
        }
    }

    /**
     * @inherited from *PurchasesUpdatedListener ->listen in-app billing purchase changes
     * @param billingResult BillingResult object
     * @param purchases Mutable list of Purchase object
     */
    override fun onPurchasesUpdated(billingResult: BillingResult?, purchases: MutableList<Purchase>?) {
        endLoader()
        allowMultiplePurchases(purchases)

        when (billingResult?.responseCode) {
            BillingClient.BillingResponseCode.SERVICE_TIMEOUT -> displayToast("Service time out :(")
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> displayToast("Billing service is disconnected :(")
            BillingClient.BillingResponseCode.OK -> {
                MaterialDialog(this).show {
                    cornerRadius(16f)
                    title(text = "Success")
                    message(text = "Thank you very much for supporting us :)\nWe'll use it for more better application.")
                    positiveButton(text = "OK") {  }
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> displayToast("Transaction canceled :|")
            BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE -> displayToast("Service unavailable :(")
            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> displayToast("Billing unavailable :(")
            BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> displayToast("Item unavailable :(")
            BillingClient.BillingResponseCode.DEVELOPER_ERROR -> displayToast("Developer error :(")
            BillingClient.BillingResponseCode.ERROR -> displayToast("Error :(")
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                displayToast("You already owned this")
            }
            BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> displayToast("You not owned yet :(")
        }
    }

    /**
     * @inherited from *BillingClientStateListener -> listen Billing client state
     */
    override fun onBillingServiceDisconnected() {
        startLoader()
    }

    /**
     * @inherited from *BillingClientStateListener -> listen Billing client state
     * @param billingResult BillingResult object
     */
    override fun onBillingSetupFinished(billingResult: BillingResult?) {
        if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK) {

            val skuDetailsParam = SkuDetailsParams
                .newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.INAPP)
                .build()

            billingClient.querySkuDetailsAsync(skuDetailsParam) { billingResult, skuDetailsList ->

                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList.isNotEmpty()) {

                    for (skuDetails in skuDetailsList) {

                        when (skuDetails.sku) {
                            skuList[0] -> coke = skuDetails
                            skuList[1] -> coffee = skuDetails
                            skuList[2] -> meal = skuDetails
                            skuList[3] -> pizza = skuDetails
                            skuList[4] -> tshirt = skuDetails
                        }
                    }

                    endLoader()
                }
            }
        }
    }

}
