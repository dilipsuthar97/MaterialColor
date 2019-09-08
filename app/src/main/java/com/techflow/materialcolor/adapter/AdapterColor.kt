package com.techflow.materialcolor.adapter

import android.app.Activity
import android.content.Context
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.getkeepsafe.taptargetview.TapTargetView
import com.techflow.materialcolor.R
import com.techflow.materialcolor.model.Color
import com.techflow.materialcolor.utils.Preferences
import com.techflow.materialcolor.utils.SharedPref

class AdapterColor(
    private val colorList: ArrayList<Color>,
    private val context: Context,
    private val activity: Activity,
    private val onClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == Color.TYPE_COLOR) {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false))
        }

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_color_shade, parent, false))
    }

    override fun getItemCount(): Int = colorList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.setData(colorList[position])
            holder.setItemClickListener(onClickListener, colorList[position], position)

            // Show first start-up tutorial
            holder.showTutorial(position, context, activity)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (colorList[position].type == Color.TYPE_COLOR) Color.TYPE_COLOR else Color.TYPE_COLOR_SHADE
    }

    /** View holder */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val viewColor: View = view.findViewById(R.id.view_color)
        private val tvColorName: TextView = view.findViewById(R.id.tv_color_name)
        private val tvColorCode: TextView = view.findViewById(R.id.tv_color_code)
        private val btnTap: LinearLayout = view.findViewById(R.id.linear_layout)

        fun setData(colorCard: Color) {
            tvColorName.text = colorCard.colorName
            tvColorCode.text = colorCard.colorCode
            viewColor.setBackgroundColor(colorCard.color)
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
                if (sharedPref.getBoolean(Preferences.HomeFragFR, true) && pos == 0) {
                    TapTargetSequence(activity)
                        .targets(
                            TapTarget.forView(tvColorName, "Material Color", "From here you can see the different shades for this color.")
                                .outerCircleColor(R.color.colorAccent)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(18)
                                .descriptionTextColor(R.color.white)
                                .cancelable(false)
                                .targetRadius(50))
                        .start()
                }

                sharedPref.saveData(Preferences.HomeFragFR, false)
            } else {
                if (sharedPref.getBoolean(Preferences.ColorActFR, true) && pos == 1) {
                    TapTargetSequence(activity)
                        .targets(
                            TapTarget.forView(btnTap, "Color shades", "Long press here to copy the color code.")
                                .outerCircleColor(R.color.colorAccent)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(18)
                                .descriptionTextColor(R.color.white)
                                .cancelable(false)
                                .targetRadius(50))
                        .start()
                }

                sharedPref.saveData(Preferences.ColorActFR, false)
            }
        }
    }

    /** Methods */
    fun getItem(position: Int): Color {
        return colorList[position]
    }

    /** Interface */
    interface OnItemClickListener {
        fun onItemClick(view: View, color: Color, position: Int)
        fun onItemLongClick(view: View, color: Color, position: Int)
    }

}