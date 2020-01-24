package com.toner.module_01

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.toner.module_01.fragments.RootFragment

class ShowImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_show_image)
        supportFragmentManager.beginTransaction()
            .add(R.id.root, RootFragment())
            .addToBackStack("")
            .commitAllowingStateLoss()
    }
}