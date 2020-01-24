package com.toner.mediapractice

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.toner.module_01.ShowImageActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.show_image -> {
                openActivity(ShowImageActivity::class.java)
            }
        }
    }

    private fun openActivity(clazz: Class<ShowImageActivity>) {
        startActivity(Intent(this, clazz))
    }

}
