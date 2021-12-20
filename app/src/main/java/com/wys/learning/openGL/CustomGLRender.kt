package com.wys.learning.openGL

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.example.commonlib.log.LogUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CustomGLRender: GLSurfaceView.Renderer {

    companion object{
        const val TAG = "CustomGLRender"
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        LogUtil.i(TAG,"onSurfaceCreated++++")
        GLES20.glClearColor(1.0f,0.0f,1.0f,0.0f)

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0,0,width,height)
    }

    override fun onDrawFrame(gl: GL10?) {

    }

    private fun init(){

    }
    private fun draw(){

    }
}