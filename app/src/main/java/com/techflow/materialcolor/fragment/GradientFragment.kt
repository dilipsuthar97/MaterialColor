package com.techflow.materialcolor.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.techflow.materialcolor.R
import com.techflow.materialcolor.adapter.AdapterGradient
import com.techflow.materialcolor.data.DataGenerator
import com.techflow.materialcolor.databinding.FragmentGradientBinding
import com.techflow.materialcolor.model.Gradient

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
    private lateinit var listGradient: List<Gradient>

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
        val adapter = AdapterGradient(ArrayList(listGradient), context!!)
        bind.recyclerView.adapter = adapter
    }


}