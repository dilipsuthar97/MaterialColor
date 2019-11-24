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
class HomeFragment : Fragment(), AdapterColor.OnItemClickListener {

    companion object {
        private var homeFragment: HomeFragment? = null

        fun getInstance(): HomeFragment {
            if (homeFragment == null)
                homeFragment = HomeFragment()

            return homeFragment as HomeFragment
        }
    }

    private lateinit var bind: FragmentHomeBinding
    private lateinit var adapter: AdapterColor

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

        adapter = AdapterColor(colorList, context!!, activity!!, this)

        /**
         * Swipe item touch helper
         */
        /*ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val color = adapter.getItem(viewHolder.adapterPosition)
                Toast.makeText(context, color.colorName, Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(bind.recyclerView)*/

        bind.recyclerView.adapter = adapter
    }

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
