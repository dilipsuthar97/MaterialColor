package com.techflow.materialcolor.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.techflow.materialcolor.R

/**
 * Created by Dilip on 16/02/19
 */

class HomeFragment : Fragment() {

    companion object {
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


}
