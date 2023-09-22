package com.example.camera

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 *@author wangyasheng
 *@date 2023/9/13
 * 预览view
 */
class CameraPreview : SurfaceView, SurfaceHolder.Callback {


    private var scaleW = 1f
    private var scaleH = 1f
    private var width = 0

    constructor(context: Context):this(context,null)
    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context,attrs: AttributeSet?,defStyleAttr:Int):super(context,attrs,defStyleAttr){
        init()
    }
    private fun init(){
        holder.addCallback(this)
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }


    fun setScale(scaleW:Float, scaleH:Float){
        this.scaleW = scaleW
        this.scaleH = scaleH
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

    }


    override fun surfaceCreated(holder: SurfaceHolder) {

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

}