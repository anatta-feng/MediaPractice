package com.toner.module_03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.toner.module_03.fragments.RootFragment

class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, RootFragment())
            .commitAllowingStateLoss()
    }
}
