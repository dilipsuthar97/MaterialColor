package com.techflow.materialcolor.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.crashlytics.android.Crashlytics
import com.techflow.materialcolor.R

import com.techflow.materialcolor.adapter.AdapterGradient
import com.techflow.materialcolor.data.DataGenerator
import com.techflow.materialcolor.databinding.FragmentGradientBinding
import com.techflow.materialcolor.model.Gradient
import com.techflow.materialcolor.utils.Preferences
import com.techflow.materialcolor.utils.SharedPref
import com.techflow.materialcolor.utils.ThemeUtils
import com.techflow.materialcolor.utils.Tools
/**
 * Created by Dilip on 16/02/19
 */
class GradientFragment : Fragment() {

    companion object {
        fun getInstance(): GradientFragment {
            return GradientFragment()
        }
    }

    private lateinit var bind: FragmentGradientBinding
    private lateinit var listGradient: ArrayList<Gradient>
    private lateinit var smoothScroller: RecyclerView.SmoothScroller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_gradient, container, false)

        initComponent(bind.root)
        return bind.root
    }

    private fun initComponent(view: View) {
        bind.recyclerView.setHasFixedSize(true)
        bind.recyclerView.layoutManager = LinearLayoutManager(context)
        listGradient = DataGenerator.getGradientsData(context!!)

        // For smooth scrolling to a specific position & visible item to top of recycler view
        smoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START   // Visible item to top of recycler view
            }
        }

        // Add section for NEW & OLD gradients >>>>>>>>>>
        listGradient.add(0, Gradient(Gradient.TYPE_SECTION, "", ""))
        listGradient.add(28, Gradient(Gradient.TYPE_SECTION, "", ""))

        // Add AdView for each 5 steps >>>>>>>>>>
        var adCount = 0
        /*if (SharedPref.getInstance(context!!).getBoolean(Preferences.SHOW_AD, true)) {
            for (i in listGradient.indices) {
                if (i % 5 == 0) {
                    listGradient.add(adCount, Gradient(Gradient.TYPE_AD, "", ""))
                    adCount += 5 + 1
                }
            }
        }*/

        val adapter = AdapterGradient(listGradient, context!!, activity!!)
        bind.recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_fragment_gradient, menu)
        Tools.changeMenuIconColor(menu, ThemeUtils.getThemeAttrColor(context!!, R.attr.colorTextPrimary))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.scroll_new -> {
                smoothScroller.targetPosition = 0
                bind.recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
            }
            R.id.scroll_old -> {
                smoothScroller.targetPosition = 28
                bind.recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
            }
        }
        return true
    }
}
