package com.toner.module_01.fragments

import android.graphics.BitmapFactory.decodeStream
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.toner.module_01.R

class SurfaceViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val surfaceView = inflater.inflate(R.layout.fragment_surfaceview, container, false)
        showImage(surfaceView as SurfaceView)
        return surfaceView
    }

    private fun showImage(surfaceView: SurfaceView) {
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                holder?.let {
                    Thread {
                        val canvas = it.lockCanvas()
                        val bitmap =
                            decodeStream(context?.assets?.open("images/test_image.jpeg"))
                        val bitmapW = bitmap.width
                        val bitmapH = bitmap.height
                        val src = Rect(0, 0, bitmapW, bitmapH)
                        val dst = if (bitmapW > bitmapH) {
                            Rect(
                                0,
                                0,
                                surfaceView.width,
                                (surfaceView.width * (bitmapH.toFloat() / bitmapW)).toInt()
                            )

                        } else {
                            Rect(
                                0,
                                0,
                                surfaceView.height * (bitmapW / bitmapH),
                                surfaceView.height
                            )
                        }
                        canvas.drawColor(Color.parseColor("#ffffff"))
                        canvas.drawBitmap(
                            bitmap,
                            src,
                            dst,
                            Paint()
                        )
                        it.unlockCanvasAndPost(canvas)
                    }.start()

                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

        })
    }
}