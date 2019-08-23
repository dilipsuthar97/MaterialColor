package com.techflow.materialcolor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techflow.materialcolor.R
import com.techflow.materialcolor.model.Color

class AdapterColor(
    private val colorList: ArrayList<Color>,
    private val context: Context,
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