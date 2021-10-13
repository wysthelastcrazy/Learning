package com.wys.learning

import android.Manifest
import android.graphics.SurfaceTexture
import android.opengl.GLES10
import android.opengl.GLES11
import android.opengl.GLES20
import android.os.Build
import android.os.Bundle
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.commonlib.log.LogUtil
import com.wys.learning.camera.Camera1Capture
import com.wys.learning.camera.CameraCapture
import com.wys.learning.camera.ICaptureListener
import java.nio.ByteBuffer

class CameraActivity : AppCompatActivity() {
    companion object{
        const val TAG = "CameraActivity"
    }
    private lateinit var mTextureView: TextureView

    private lateinit var mCameraCapture: CameraCapture

    private lateinit var mImageViewShow: ImageView

    private lateinit var mSurfaceTexture: SurfaceTexture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        requestPermissions()
        mTextureView = findViewById(R.id.camera_preview)
        mImageViewShow = findViewById(R.id.img_show)
        mTextureView.surfaceTextureListener = PreviewSurfaceTextureListener()
        mCameraCapture = Camera1Capture(this)
        mCameraCapture.setWantedSize(1280,720)
        mCameraCapture.setFacing(true)
        mCameraCapture.mCaptureListener = object: ICaptureListener{
            override fun onOpened() {

                LogUtil.i(TAG,"onOpened+++++++++++++")
                mCameraCapture?.start()
            }

            override fun onStarted() {
                LogUtil.i(TAG,"onStarted+++++++++++++")
            }

            override fun onStopped() {
                LogUtil.i(TAG,"onStopped+++++++++++++")
                mCameraCapture?.close()
            }

            override fun onClosed() {
                LogUtil.i(TAG,"onClosed+++++++++++++")
            }
        }
    }
    private fun requestPermissions(){
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 0x100)
        }
    }

    private inner class PreviewSurfaceTextureListener : TextureView.SurfaceTextureListener{
        override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, heigth: Int) {
            mCameraCapture?.setSurfaceTexture(surfaceTexture)
            LogUtil.i(TAG,"onSurfaceTextureAvailable+++++++++++++")
        }

        override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, width: Int, heigth: Int) {

        }

        override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
            mCameraCapture?.stop()
            return false
        }

        override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
//            val bitmap = mTextureView.bitmap
//            if (bitmap != null && !bitmap.isRecycled){
//                val width = bitmap.width
//                val height = bitmap.height
//                val size = bitmap.rowBytes * bitmap.height
//
//                val byteBuffer = ByteBuffer.allocate(size)
//                bitmap.copyPixelsToBuffer(byteBuffer)
//                val data = byteBuffer.array()
//                LogUtil.d(TAG,"onSurfaceTextureUpdated   data size = ${data.size}")
//                mImageViewShow.setImageBitmap(bitmap)
//                bitmap.recycle()
//            }
        }

    }

    fun openCamera(view: View) {
        mCameraCapture?.open()
    }
    fun closeCamera(view: View) {
        mCameraCapture?.stop()
    }

    fun generateTexture(target: Int): Int{
        val textures = IntArray(1)
        GLES20.glGenTextures(1,textures,0)
        val textureId = textures[0]
        GLES20.glBindTexture(target,textureId)
        GLES20.glTexParameterf(target, 10241, 9729.0f)
        GLES20.glTexParameterf(target, 10240, 9729.0f)
        GLES20.glTexParameterf(target, 10242, 33071.0f)
        GLES20.glTexParameterf(target, 10243, 33071.0f)
        return textureId
    }
}