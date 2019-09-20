package com.techflow.materialcolor.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.facebook.ads.*
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.techflow.materialcolor.MaterialColor.AdType
import com.techflow.materialcolor.MaterialColor
import com.techflow.materialcolor.R
import com.techflow.materialcolor.model.Gradient
import com.techflow.materialcolor.utils.AnimUtils
import com.techflow.materialcolor.utils.Preferences
import com.techflow.materialcolor.utils.SharedPref
import com.techflow.materialcolor.utils.Tools

class AdapterGradient(
    private val items: ArrayList<Gradient>,
    private val context: Context,
    private val activity: Activity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == Gradient.TYPE_AD) {
            return AdViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ad, parent, false))
        } else if (viewType == Gradient.TYPE_SECTION) {
            return SectionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_section_title, parent, false))
        }

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_gradient, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when {
            items[position].type == Gradient.TYPE_GRADIENT -> Gradient.TYPE_GRADIENT
            items[position].type == Gradient.TYPE_SECTION -> Gradient.TYPE_SECTION
            else -> Gradient.TYPE_AD
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.setData(items[position])
            holder.btnPColor.setOnClickListener {
                AnimUtils.bounceAnim(it)
                Tools.copyToClipboard(context, items[position].primaryColor, "HEX code ${items[position].primaryColor}")
            }

            holder.btnSColor.setOnClickListener {
                AnimUtils.bounceAnim(it)
                Tools.copyToClipboard(context, items[position].secondaryColor, "HEX code ${items[position].secondaryColor}")
            }

            holder.showTutorial(position, context, activity)
        } else if (holder is AdViewHolder) {

            // Check if purchased and load >> advertisement
            if (SharedPref.getInstance(context).getBoolean(Preferences.SHOW_AD, true)) {
                holder.loadAds(context)
            }

        } else if (holder is SectionViewHolder) {

            if (position == 1) {
                val title = SpannableString("NEW")
                title.setSpan(UnderlineSpan(), 0, "NEW".length, 0)
                holder.title.text = title
            }
            else {
                val title = SpannableString("OLD")
                title.setSpan(UnderlineSpan(), 0, "OLD".length, 0)
                holder.title.text = title
            }

        }
    }

    /** View holder */
    // Gradient view holder
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val colorLyt: View = view.findViewById(R.id.color_lyt)
        val tvPrimaryColor: TextView = view.findViewById(R.id.tv_primary_color)
        val tvSecondaryColor: TextView = view.findViewById(R.id.tv_secondary_color)
        val btnPColor: View = view.findViewById(R.id.card_primary_color)
        val btnSColor: View = view.findViewById(R.id.card_secondary_color)

        fun setData(gradient: Gradient) {
            tvPrimaryColor.text = gradient.primaryColor
            tvSecondaryColor.text = gradient.secondaryColor
            //tvSecondaryColor.setTextColor(Color.parseColor(gradient.secondaryColor))

            val gd: GradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor(gradient.primaryColor), Color.parseColor(gradient.secondaryColor))
            )
            gd.cornerRadius = 0.0f
            colorLyt.background = gd
        }

        fun showTutorial(pos: Int, ctx: Context, act: Activity) {
            val sharedPref = SharedPref.getInstance(ctx)
            val msg = "From tapping here you can copy this code."

            if (sharedPref.getBoolean(Preferences.GradientFragFR, true) && pos == 3) {
                TapTargetSequence(act)
                    .targets(
                        TapTarget.forView(tvPrimaryColor, "Primary Color HexCode", msg)
                            .outerCircleColor(R.color.colorAccent)
                            .outerCircleAlpha(0.96f)
                            .targetCircleColor(R.color.white)
                            .titleTextSize(20)
                            .titleTextColor(R.color.white)
                            .descriptionTextSize(18)
                            .descriptionTextColor(R.color.white)
                            .cancelable(false)
                            .targetRadius(50),

                        TapTarget.forView(tvSecondaryColor, "Secondary Color HexCode", msg)
                            .outerCircleColor(R.color.colorAccent)
                            .outerCircleAlpha(0.96f)
                            .targetCircleColor(R.color.white)
                            .titleTextSize(20)
                            .titleTextColor(R.color.white)
                            .descriptionTextSize(18)
                            .descriptionTextColor(R.color.white)
                            .cancelable(false)
                            .targetRadius(50)
                    )
                    .start()

                sharedPref.saveData(Preferences.GradientFragFR, false)
            }
        }
    }

    // Ad View Holder
    class AdViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val bannerContainer: LinearLayout = view.findViewById(R.id.banner_container)

        fun loadAds(context: Context) {
            val adView = AdView(context, MaterialColor.getAdId(AdType.BANNER), AdSize.BANNER_HEIGHT_50)
            if (Tools.hasNetwork(context))
                adView.loadAd()

            adView.setAdListener(object : AdListener {
                override fun onAdLoaded(p0: Ad?) {
                    Tools.visibleViews(bannerContainer)
                    if (bannerContainer.size == 0)
                        bannerContainer.addView(adView)
                }

                override fun onAdClicked(p0: Ad?) {

                }

                override fun onError(p0: Ad?, p1: AdError?) {
                    if (Tools.hasNetwork(context))
                        adView.loadAd()
                }

                override fun onLoggingImpression(p0: Ad?) {

                }
            })
        }
    }

    // Section view holder
    class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tv_section_title)
    }

}