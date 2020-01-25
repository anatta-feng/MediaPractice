package com.toner.module_02.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.toner.module_02.R
import kotlinx.android.synthetic.main.fragment_root.view.*

class RootFragment : Fragment(), View.OnClickListener {
    private lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_root, container, false)
        root.audio_record.setOnClickListener(this)
        root.audio_trace.setOnClickListener(this)
        return root
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.audio_record -> {
                showFragment(AudioPcmFragment())
            }
            R.id.audio_trace -> {
                showFragment(AudioWavFragment())
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()
            ?.add(R.id.fragment_content, fragment)
            ?.addToBackStack("")
            ?.commitAllowingStateLoss()
    }
}