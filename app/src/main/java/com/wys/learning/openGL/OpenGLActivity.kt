package com.wys.learning.openGL

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wys.learning.R

class OpenGLActivity : AppCompatActivity() {
    private lateinit var mGLSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opengl_activity)
        mGLSurfaceView = findViewById(R.id.gl_surfaceView)
        checkOpenGLVersion()
        mGLSurfaceView.setEGLContextClientVersion(2)
        mGLSurfaceView.setRenderer(CustomGLRender())
        mGLSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }
    private fun checkOpenGLVersion(){
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val config = activityManager.deviceConfigurationInfo
    }
}