package com.example.avdemo.part01

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.avdemo.R
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    companion object{
        const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showBitmap()
    }
    private fun showBitmap(){
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher)
        val surfaceView = findViewById<SurfaceView>(R.id.surfaceView)
        surfaceView.holder.addCallback(object: SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder) {
                Log.d(TAG,"currThread : ${Thread.currentThread().name}")
                if (holder == null){
                    return
                }
                val paint = Paint()
                var canvas: Canvas? = null

                try {
                    canvas = holder.lockCanvas()
                    canvas.drawRGB(100,100,255)
                    if (bitmap != null && !bitmap.isRecycled)
                    canvas.drawBitmap(bitmap,0f,0f,paint)
                }catch (e: Exception){
                    e.printStackTrace()
                }finally {
                    if (canvas != null){
                        holder.unlockCanvasAndPost(canvas)
                    }
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {

            }

        })
    }
}