package com.toner.module_02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.toner.module_02.fragments.RootFragment

class AudioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_content, RootFragment())
            .commitAllowingStateLoss()
    }
}
