package com.wys.learning.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
class PDuffXFermodeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private val paint: Paint = Paint()
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            //设置背景色
            it.drawARGB(255,139,197,186)

            val layerId = it.saveLayer(0f,0f,it.width.toFloat(),it.height.toFloat(),null,Canvas.ALL_SAVE_FLAG)
            val r = it.width / 3f
            //绘制黄色的圆形
            paint.color = Color.YELLOW
            it.drawCircle(r,r,r,paint)

            //绘制蓝色的矩形
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
            paint.color = Color.RED
            it.drawRect(r,r,r * 2.7f,r * 2.7f,paint)

            //最后去除xfermode
            paint.xfermode = null
            canvas.restoreToCount(layerId)
        }
    }
}