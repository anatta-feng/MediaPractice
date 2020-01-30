package com.toner.commom

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

open class BaseFragment : Fragment() {

    protected fun requestPermission(vararg permission: String) {
        activity?.let {
            val notGrantedPermissions = permission.filter { item ->
                PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(it, item)
            }
            if (notGrantedPermissions.isNotEmpty()) {
                ActivityCompat.requestPermissions(it, notGrantedPermissions.toTypedArray(), 200)
            }
        }

    }

    protected fun showFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()
            ?.add(getFragmentContainerId(), fragment)
            ?.addToBackStack("")
            ?.commitAllowingStateLoss()
    }

    protected open fun getFragmentContainerId(): Int = 0

}