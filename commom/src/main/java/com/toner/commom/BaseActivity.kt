package com.toner.commom

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class BaseActivity : AppCompatActivity() {

    protected fun requestPermission(vararg permission: String) {
        val notGrantedPermissions = permission.filter {
            PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, it)
        }
        if (notGrantedPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, notGrantedPermissions.toTypedArray(), 200)
        }
    }
}