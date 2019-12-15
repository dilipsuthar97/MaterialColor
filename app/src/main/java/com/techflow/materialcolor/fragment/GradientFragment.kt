package com.techflow.materialcolor.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.techflow.materialcolor.R

import com.techflow.materialcolor.adapter.AdapterGradient
import com.techflow.materialcolor.data.DataGenerator
import com.techflow.materialcolor.databinding.FragmentGradientBinding
import com.techflow.materialcolor.model.Gradient
import com.techflow.materialcolor.utils.ThemeUtils
import com.techflow.materialcolor.utils.Tools
/**
 * Created by Dilip on 16/02/19
 */
class GradientFragment : Fragment() {

    companion object {
        private var gradientFragment: GradientFragment? = null

        fun getInstance(): GradientFragment {
            if (gradientFragment == null)
                gradientFragment = GradientFragment()

            return gradientFragment as GradientFragment
        }
    }

    private lateinit var binding: FragmentGradientBinding
    private lateinit var smoothScroller: RecyclerView.SmoothScroller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gradient, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent(view)
    }

    /**
     * @func init all component's config
     */
    private fun initComponent(view: View) {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val listGradient = DataGenerator.getGradientsData(context!!)

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
        /*var adCount = 0
        if (SharedPref.getInstance(context!!).getBoolean(Preferences.SHOW_AD, true)) {
            for (i in listGradient.indices) {
                if (i % 5 == 0) {
                    listGradient.add(adCount, Gradient(Gradient.TYPE_AD, "", ""))
                    adCount += 5 + 1
                }
            }
        }*/

        val adapter = AdapterGradient(listGradient, context!!, activity!!)
        binding.recyclerView.adapter = adapter
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
                binding.recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
            }
            R.id.scroll_old -> {
                smoothScroller.targetPosition = 28
                binding.recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
            }
        }
        return true
    }
}
