package com.toner.module_01.fragments

import android.graphics.BitmapFactory.decodeStream
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.toner.module_01.R

class ImageViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val image = inflater.inflate(R.layout.fragment_imageview, container, false)
        showImage(image as ImageView)
        return image
    }

    private fun showImage(image: ImageView) {
        context?.let {
            image.setImageBitmap(decodeStream(it.assets.open("images/test_image.jpeg")))
        }
    }
}