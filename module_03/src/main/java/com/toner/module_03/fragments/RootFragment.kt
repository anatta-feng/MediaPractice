package com.toner.module_03.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toner.commom.BaseFragment
import com.toner.module_03.R
import kotlinx.android.synthetic.main.fragment_root.view.*

class RootFragment : BaseFragment(), View.OnClickListener {
    private val TAG = "RootFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_root, container, false)
        view.surface_view.setOnClickListener(this)
        view.texture.setOnClickListener(this)
        requestPermission(Manifest.permission.CAMERA)
        return view
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.surface_view -> showFragment(SurfaceViewFragment())
        }
    }

    override fun getFragmentContainerId(): Int = R.id.fragment_container
}