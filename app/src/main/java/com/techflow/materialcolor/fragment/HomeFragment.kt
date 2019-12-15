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

    // STATIC
    companion object {
        private var homeFragment: HomeFragment? = null

        fun getInstance(): HomeFragment {
            if (homeFragment == null)
                homeFragment = HomeFragment()

            return homeFragment as HomeFragment
        }
    }

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent(view)
    }

    /**
     * @func init all component's config
     * @param view view
     */
    private fun initComponent(view: View) {

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.isNestedScrollingEnabled = false
        val colorList = DataGenerator.getColorData(context!!)

        // Add AdView for each 5 steps >>>>>>>>>>
        /*var adCount = 0
        for (i in colorList.indices) {
            if (i % 5 == 0) {
                colorList.add(adCount, Color(Color.TYPE_AD, -1, "", ""))
                adCount += 5 + 1
            }
        }*/

        val adapter = AdapterColor(context!!, activity!!, this, null)
        binding.recyclerView.adapter = adapter
        adapter.setColors(colorList)
    }

    /**
     * @inherited on item click callback
     * @param view view
     * @param color color object
     * @param position recycler view item position
     */
    override fun onItemClick(view: View, color: Color, position: Int) {
        // Load interstitial ad
        if (SharedPref.getInstance(context!!).getBoolean(Preferences.SHOW_AD, true))
            HomeActivity.showAd(context!!)

        val i = Intent(activity, ColorActivity::class.java)
        i.putExtra("COLOR_NAME", color.colorName)
        i.putExtra("COLOR", color.color)
        startActivity(i)
    }

    /**
     * @inherited on item long click callback
     * @param view view
     * @param color color object
     * @param position recycler view item position
     */
    override fun onItemLongClick(view: View, color: Color, position: Int) {
        Tools.copyToClipboard(context!!, color.colorCode, "HEX code ${color.colorCode}")
    }

    /**
     * @inherited on bookmark button click ballback
     * @param view view
     * @param color color object
     * @param position recycler view item position
     */
    override fun onBookmarkButtonClick(view: View, color: Color, position: Int) {
    }
}
