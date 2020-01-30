package com.toner.module_03.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toner.commom.BaseFragment
import com.toner.module_03.Camera2Helper
import com.toner.module_03.R
import kotlinx.android.synthetic.main.fragment_surface_view.view.*

class SurfaceViewFragment : BaseFragment() {
    private val TAG = "SurfaceViewFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_surface_view, container, false)
        context?.let {
            Camera2Helper(it, view.surface_view).start()
        }
        return view
    }

}