package com.toner.module_01.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CustomImageView : View {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val bitmap: Bitmap =
        BitmapFactory.decodeStream(context?.assets?.open("images/test_image.jpeg"))

    private val src = Rect()
    private val dst = Rect()
    private val paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val bitmapW = bitmap.width
        val bitmapH = bitmap.height
        src.set(0, 0, bitmapW, bitmapH)
        if (bitmapW > bitmapH) {
            dst.set(
                0,
                0,
                this.width,
                (this.width * (bitmapH.toFloat() / bitmapW)).toInt()
            )

        } else {
            dst.set(
                0,
                0,
                this.height * (bitmapW / bitmapH),
                this.height
            )
        }
        canvas?.drawBitmap(bitmap, src, dst, paint)
    }
}