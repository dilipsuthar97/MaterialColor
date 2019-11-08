package com.techflow.materialcolor.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.techflow.materialcolor.R
import com.techflow.materialcolor.activity.ColorActivity
import com.techflow.materialcolor.activity.HomeActivity
import com.techflow.materialcolor.adapter.AdapterColor
import com.techflow.materialcolor.data.DataGenerator
import com.techflow.materialcolor.databinding.FragmentHomeBinding
import com.techflow.materialcolor.model.Color
import com.techflow.materialcolor.utils.Preferences
import com.techflow.materialcolor.utils.SharedPref
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

        // On color card item click listener
        mOnItemClickListener = object : AdapterColor.OnItemClickListener {

            override fun onItemClick(view: View, color: Color, position: Int) {

                // Load interstitial ad
                if (SharedPref.getInstance(context!!).getBoolean(Preferences.SHOW_AD, true))
                    HomeActivity.showAd(context!!)

                val i = Intent(activity, ColorActivity::class.java)
                i.putExtra("COLOR_NAME", color.colorName)
                i.putExtra("COLOR", color.color)
                startActivity(i)
            }

            override fun onItemLongClick(view: View, color: Color, position: Int) {
                Tools.copyToClipboard(context!!, color.colorCode, "HEX code ${color.colorCode}")
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
        bind.recyclerView.layoutManager = LinearLayoutManager(context)
        bind.recyclerView.isNestedScrollingEnabled = false
        val colorList = DataGenerator.getColorData(context!!)

        // Add AdView for each 5 steps >>>>>>>>>>
        /*var adCount = 0
        for (i in colorList.indices) {
            if (i % 5 == 0) {
                colorList.add(adCount, Color(Color.TYPE_AD, -1, "", ""))
                adCount += 5 + 1
            }
        }*/

        adapter = AdapterColor(colorList, context!!, activity!!, mOnItemClickListener)
        bind.recyclerView.adapter = adapter
    }
}
