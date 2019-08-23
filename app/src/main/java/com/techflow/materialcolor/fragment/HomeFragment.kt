package com.techflow.materialcolor.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager

import com.techflow.materialcolor.R
import com.techflow.materialcolor.activity.ColorActivity
import com.techflow.materialcolor.adapter.AdapterColor
import com.techflow.materialcolor.data.DataGenerator
import com.techflow.materialcolor.databinding.FragmentHomeBinding
import com.techflow.materialcolor.model.Color
import com.techflow.materialcolor.utils.Tools

/**
 * Created by Dilip on 16/02/19
 */

class HomeFragment : Fragment() {

    companion object {
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private lateinit var bind: FragmentHomeBinding
    private lateinit var adapter: AdapterColor
    private lateinit var mOnItemClickListener: AdapterColor.OnItemClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mOnItemClickListener = object : AdapterColor.OnItemClickListener {
            override fun onItemClick(view: View, color: Color, position: Int) {
                val i = Intent(activity, ColorActivity::class.java)
                i.putExtra("COLOR_NAME", color.colorName)
                i.putExtra("COLOR", color.color)
                i.putExtra("COLOR_POS", position)
                startActivity(i)
            }

            override fun onItemLongClick(view: View, color: Color, position: Int) {
                Tools.copyToClipboard(context!!, color.colorCode)
                Toast.makeText(context!!, "HEX code ${color.colorCode} copied on clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        initComponent(bind.root)
        return bind.root
    }

    private fun initComponent(view: View) {
        bind.recyclerView.setHasFixedSize(true)
        bind.recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = AdapterColor(DataGenerator.getColorData(context!!), context!!, mOnItemClickListener)
        bind.recyclerView.adapter = adapter
    }
}
