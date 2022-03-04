package com.example.avdemo.part01

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.avdemo.R

class CustomImagerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) :
    View(context, attrs, defStyleAttr) {
    private val mPaint: Paint = Paint()
    private val mBitmap: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher)


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mBitmap != null && !mBitmap.isRecycled) {
            canvas?.drawBitmap(mBitmap, 0f, 0f, mPaint)
        }
    }
}