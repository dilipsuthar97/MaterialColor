package com.techflow.materialcolor.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.techflow.materialcolor.R
import com.techflow.materialcolor.model.Gradient
import com.techflow.materialcolor.utils.AnimUtils
import com.techflow.materialcolor.utils.Tools

class AdapterGradient(private val items: ArrayList<Gradient>, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_gradient, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.setData(items[position])
            holder.btnPColor.setOnClickListener {
                AnimUtils.bounceAnim(it)
                Tools.copyToClipboard(context, items[position].primaryColor)
                Toast.makeText(context, "HEX code ${items[position].primaryColor} copied on clipboard", Toast.LENGTH_SHORT).show()
            }

            holder.btnSColor.setOnClickListener {
                AnimUtils.bounceAnim(it)
                Tools.copyToClipboard(context, items[position].secondaryColor)
                Toast.makeText(context, "HEX code ${items[position].secondaryColor} copied on clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /** View holder */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val colorLyt: View = view.findViewById(R.id.color_lyt)
        val tvPrimaryColor: TextView = view.findViewById(R.id.tv_primary_color)
        val tvSecondaryColor: TextView = view.findViewById(R.id.tv_secondary_color)
        val btnPColor: View = view.findViewById(R.id.card_primary_color)
        val btnSColor: View = view.findViewById(R.id.card_secondary_color)

        fun setData(gradient: Gradient) {
            tvPrimaryColor.text = gradient.primaryColor
            tvSecondaryColor.text = gradient.secondaryColor
            tvSecondaryColor.setTextColor(Color.parseColor(gradient.secondaryColor))

            val gd: GradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor(gradient.primaryColor), Color.parseColor(gradient.secondaryColor))
            )
            gd.cornerRadius = 0.0f
            colorLyt.background = gd
        }
    }

}