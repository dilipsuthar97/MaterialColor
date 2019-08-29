package com.techflow.materialcolor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.techflow.materialcolor.R
import com.techflow.materialcolor.model.ColorFromImage
import com.techflow.materialcolor.utils.AnimUtils
import com.techflow.materialcolor.utils.Tools

class AdapterColorFromImage(
    private val listColor: List<ColorFromImage>,
    private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_color_from_image, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val colorFromImage = listColor[position]
        if (holder is ViewHolder) {
            with(holder) {
                lytColor.setBackgroundColor(colorFromImage.color)
                tvColorCode.text = colorFromImage.hexCode
                fabCopyCode.setOnClickListener {
                    AnimUtils.bounceAnim(it)
                    Tools.copyToClipboard(context, colorFromImage.hexCode, "Code ${colorFromImage.hexCode} ")

                }
            }
        }
    }

    override fun getItemCount(): Int = if (listColor.isEmpty()) 0 else listColor.size

    /** View holder */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lytColor: View = view.findViewById(R.id.lyt_color)
        val tvColorCode: TextView = view.findViewById(R.id.tv_color_code)
        val fabCopyCode: FloatingActionButton = view.findViewById(R.id.fab_copy_code)
    }

}