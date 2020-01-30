package com.toner.module_01.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toner.commom.BaseFragment
import com.toner.module_01.R
import kotlinx.android.synthetic.main.fragment_root.view.*

class RootFragment : BaseFragment(), View.OnClickListener {
    private lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_root, container, false)
        root.imageview.setOnClickListener(this)
        root.surfaceview.setOnClickListener(this)
        root.custom_view.setOnClickListener(this)
        return root
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imageview -> {
                showFragment(ImageViewFragment())
            }
            R.id.surfaceview -> {
                showFragment(SurfaceViewFragment())
            }
            R.id.custom_view -> {
                showFragment(CustomViewFragment())
            }
        }
    }

    override fun getFragmentContainerId(): Int = R.id.root

}