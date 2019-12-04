package com.techflow.materialcolor.adapter

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.techflow.materialcolor.R
import com.techflow.materialcolor.database.AppDatabase
import com.techflow.materialcolor.helpers.AppExecutorHelper
import com.techflow.materialcolor.helpers.isDark
import com.techflow.materialcolor.model.Color
import com.techflow.materialcolor.utils.AnimUtils
import com.techflow.materialcolor.utils.Preferences
import com.techflow.materialcolor.utils.SharedPref

class AdapterColor constructor(
    private val context: Context,
    private val activity: Activity,
    private val onClickListener: OnItemClickListener,
    private val mDb: AppDatabase? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /*// Secondary constructor
    constructor(context: Context, activity: Activity, onClickListener: OnItemClickListener): this(
        context, activity, onClickListener, null
    )*/

    private lateinit var colorList: ArrayList<Color>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == Color.TYPE_COLOR) {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false))
        } else if (viewType == Color.TYPE_AD) {
            return AdViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ad, parent, false))
        }

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_color_shade, parent, false))
    }

    override fun getItemCount(): Int {
        return if (!::colorList.isInitialized) 0 else colorList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {

            holder.setData(colorList[position], context, holder, onClickListener, mDb, position)
            holder.setItemClickListener(onClickListener, colorList[position], position)

            // Show first start-up tutorial
            holder.showTutorial(position, context, activity)

        }/* else if (holder is AdViewHolder) {

            // Check if purchased and load Ad
            if (SharedPref.getInstance(context).getBoolean(Preferences.SHOW_AD, true)) {
                holder.loadAds(context)
            }

        }*/
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            colorList[position].type == Color.TYPE_COLOR -> Color.TYPE_COLOR
            colorList[position].type == Color.TYPE_COLOR_SHADE -> Color.TYPE_COLOR_SHADE
            else -> Color.TYPE_AD
        }
    }

    /** View holder */
    // Color view holder
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val viewColor: View = view.findViewById(R.id.view_color)
        private val tvColorName: TextView = view.findViewById(R.id.tv_color_name)
        private val tvColorCode: TextView = view.findViewById(R.id.tv_color_code)
        private val btnTap: View = view.findViewById(R.id.btn_tap)
        private lateinit var btnBookmark: ImageButton

        fun setData(colorCard: Color,
                    context: Context,
                    holder: RecyclerView.ViewHolder,
                    onClickListener: OnItemClickListener,
                    mDb: AppDatabase?,
                    pos: Int
        ) {
            tvColorName.text = colorCard.colorName
            tvColorCode.text = colorCard.colorCode
            viewColor.setBackgroundColor(colorCard.color)

            if (itemViewType == Color.TYPE_COLOR_SHADE) {

                btnBookmark = holder.itemView.findViewById(R.id.btn_bookmark)
                var color: Color? = null

                AppExecutorHelper.getInstance()?.multiThreadIO()?.execute {
                    color = mDb?.colorDao()?.checkIfColorBookmarked(colorCard.colorCode)

                    AppExecutorHelper.getInstance()?.mainThread()?.execute {
                        if (color != null) {
                            btnBookmark.setImageResource(R.drawable.ic_bookmark)
                            colorCard.bookmarked = true
                        }
                        else if (color == null) {
                            btnBookmark.setImageResource(R.drawable.ic_bookmark_border)
                            colorCard.bookmarked = false
                        }
                    }
                }

                btnBookmark.setOnClickListener {
                    onClickListener.onBookmarkButtonClick(it, colorCard, pos)
                }

                // Set textView color based on color luminance
                if (colorCard.color.isDark()) {
                    tvColorCode.setTextColor(ContextCompat.getColor(context, R.color.colorTextPrimary_dark))
                    tvColorName.setTextColor(ContextCompat.getColor(context, R.color.colorTextPrimary_dark))
                    btnBookmark.setColorFilter(ContextCompat.getColor(context, R.color.colorTextPrimary_dark), PorterDuff.Mode.SRC_ATOP)
                } else {
                    tvColorCode.setTextColor(ContextCompat.getColor(context, R.color.colorTextPrimary))
                    tvColorName.setTextColor(ContextCompat.getColor(context, R.color.colorTextPrimary))
                    btnBookmark.setColorFilter(ContextCompat.getColor(context, R.color.colorTextPrimary), PorterDuff.Mode.SRC_ATOP)
                }

            }
        }

        fun setItemClickListener(onClickListener: OnItemClickListener, color: Color, pos: Int) {
            btnTap.setOnClickListener {
                onClickListener.onItemClick(it, color, pos)
            }

            btnTap.setOnLongClickListener {
                onClickListener.onItemLongClick(it, color, pos)
                true
            }
        }

        fun showTutorial(pos: Int, context: Context, activity: Activity) {
            val sharedPref = SharedPref.getInstance(context)

            if (itemViewType == Color.TYPE_COLOR) {
                if (sharedPref.getBoolean(Preferences.HomeFragFR, true) && pos == 1) {
                    TapTargetSequence(activity)
                        .targets(
                            TapTarget.forView(tvColorName, "Material Color", "From here you can see the different shades for this color.")
                                .outerCircleColor(R.color.colorAccent)
                                .outerCircleAlpha(0.90f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(18)
                                .descriptionTextColor(R.color.white)
                                .cancelable(false)
                                .targetRadius(50))
                        .start()

                    sharedPref.saveData(Preferences.HomeFragFR, false)
                }

            } else {

                if (sharedPref.getBoolean(Preferences.ColorActFR, true) && pos == 1) {
                    TapTargetSequence(activity)
                        .targets(
                            TapTarget.forView(viewColor, "Color shades", "Long press here to copy the color code.")
                                .outerCircleColor(R.color.colorAccent)
                                .outerCircleAlpha(0.90f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(18)
                                .descriptionTextColor(R.color.white)
                                .cancelable(false)
                                .targetRadius(50))
                        .start()

                    sharedPref.saveData(Preferences.ColorActFR, false)
                }
            }
        }
    }

    // Ad View Holder
    class AdViewHolder(view: View): RecyclerView.ViewHolder(view) {
        /*private val bannerContainer: LinearLayout = view.findViewById(R.id.banner_container)

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
        }*/
    }

    /** Methods */
    fun getColor(position: Int): Color = colorList[position]

    fun getColors(): List<Color> = colorList

    fun setColor(color: Color, position: Int) {
        colorList.add(position, color)
        notifyDataSetChanged()
    }

    fun setColors(colors: List<Color>) {
        colorList = ArrayList(colors)
        notifyDataSetChanged()
    }

    fun getBookmarkedValue(position: Int): Boolean = colorList[position].bookmarked

    fun setBookmarkedValue(position: Int, value: Boolean) {
        colorList[position].bookmarked = value
        //notifyItemChanged(position)
    }

    /** Interface */
    interface OnItemClickListener {
        fun onItemClick(view: View, color: Color, position: Int)
        fun onItemLongClick(view: View, color: Color, position: Int)
        fun onBookmarkButtonClick(view: View, color: Color, position: Int)
    }

}