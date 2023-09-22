package com.wys.learning

import android.Manifest
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.SurfaceTexture
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.renderscript.*
import android.util.Log
import android.view.OrientationEventListener
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.commonlib.log.LogUtil
import com.example.commonlib.utils.Rx
import com.wys.learning.camera.Camera1Capture
import com.wys.learning.camera.Camera2Capture
import com.wys.learning.camera.CameraCapture
import com.wys.learning.camera.ICaptureListener
import com.wys.learning.utils.ImageUtils
import com.wys.liblame.LameUtils
import com.wys.libyuv.utils.YuvUtils
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

            override fun onPreviewFrame(frameDate: ByteArray, type: Int, width: Int, height: Int) {
                LogUtil.i(TAG,"onPreviewFrame+++++++++++++")
//                Rx.runUI{
//                    val bitamp = yuvToBitmap2(frameDate,type, width,height)
//                    mImageViewShow.setImageBitmap(bitamp)
//                }
            }

        }
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//        mOrientationListener.enable()

        val lameVersion = LameUtils.getLameVersion()
        Log.d(TAG,"onCreate lameVersion = $lameVersion")

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

    private fun yuvToBitmap2(data: ByteArray, formatType: Int, width: Int, height: Int): Bitmap?{
        LogUtil.d(TAG, "yuvToBitmap2 width = $width ,height = $height, data.size = ${data.size}")
        var i420Data = ByteArray(data.size)
        if (formatType == ImageUtils.COLOR_FormatNV21){
            YuvUtils.NV21ToI420(data,width,height,i420Data)
        }else{
            i420Data = data
        }
        var rWidth = width
        var rHeight = height

        val degree = 270
        YuvUtils.rotateI420(i420Data, rWidth, rHeight, data, degree)
        if (degree == 90 || degree == 270) {
            var t = rWidth
            rWidth = rHeight
            rHeight = t
        }

        val rgba = ByteArray(rWidth * rHeight * 4)
        YuvUtils.I420ToRGBA(data,rWidth,rHeight,rgba)

        var bitmap = Bitmap.createBitmap(rWidth,rHeight,Bitmap.Config.ARGB_8888)

        val buffer = ByteBuffer.wrap(rgba)
        buffer.rewind()
        buffer.position(0)
        bitmap.copyPixelsFromBuffer(buffer)
        return bitmap
    }

    /**
     * yuv转换为bitmap，用于TextureView size为0获取不到bitmap时，直接从流获取截图
     */
    private fun yuvToBitmap(data: ByteArray, formatType: Int, width: Int, height: Int): Bitmap? {
        LogUtil.d(TAG, "yuvToBitmap width = $width ,height = $height, data.size = ${data.size}")
        //YUV_420P I420数据格式：YUV分量分别存放，先是w*h长度的Y，后面跟w*h*0.25长度的U，
        // 最后是w*h*0.25长度的V，总长度为 w*h*1.5
        var rWidth = width
        var rHeight = height

        var output = ByteArray(data.size)
        if (formatType == ImageUtils.COLOR_FormatNV21){
            output = data
        }else if (formatType == ImageUtils.COLOR_FormatI420) {
            //转换为i420并旋转
            var o = ByteArray(data.size)
            val degree = 270
            YuvUtils.rotateI420(data, rWidth, rHeight, o, degree)
            if (degree == 90 || degree == 270) {
                var t = rWidth
                rWidth = rHeight
                rHeight = t
            }
            YuvUtils.mirrorI420(o,rWidth,rHeight,data)
            val dst_data = ByteArray(data.size/4)
            val dst_widht = rWidth/2
            val dst_height = rHeight/2
            YuvUtils.compressI420(data,rWidth,rHeight,dst_data,dst_widht,dst_height,0)
            rWidth = dst_widht
            rHeight = dst_height
            output = ByteArray(dst_data.size)
            YuvUtils.I420ToNV21(dst_data, rWidth, rHeight, output)
        }else{
            output = data
        }
        //使用RenderScript将数据转换为bitmap

        val rs = RenderScript.create(this)
        val yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs))
        var yuvType = Type.Builder(rs, Element.U8(rs)).setX(output.size)
        val dataIn = Allocation.createTyped(rs, yuvType.create(), 1)
        val rgbaType = Type.Builder(rs, Element.RGBA_8888(rs))
            .setX(rWidth).setY(rHeight)
        val dateOut = Allocation.createTyped(rs, rgbaType.create(), 1)
        dataIn.copyFrom(output)
        yuvToRgbIntrinsic.setInput(dataIn)
        yuvToRgbIntrinsic.forEach(dateOut)
        val bitmap = Bitmap.createBitmap(rWidth, rHeight, Bitmap.Config.ARGB_8888)
        dateOut.copyTo(bitmap)
        return bitmap
    }

    override fun onDestroy() {
        super.onDestroy()
        mOrientationListener.disable()
    }

    private var mIsLock = false
    private var mOrientation = -1
    private val mOrientationListener: OrientationEventListener =
        object : OrientationEventListener(MApp.getContext()) {
            override fun onOrientationChanged(orientation: Int) {
                LogUtil.i(TAG,"onOrientationChanged orientation = $orientation")
                //-1：手机平放，检测不到有效的角度
                //0：用户竖直拿着手机
                //90：用户右侧横屏拿着手机
                //180：用户反向竖直拿着手机
                //270：用户左侧横屏拿着手机

                //记录上一次的放置位置
                val mLastOrientation = mOrientation
                if (orientation == ORIENTATION_UNKNOWN){
                    mOrientation = -1
                    return
                }

                if (orientation > 330 || orientation < 30){
                    //0度，用户竖直拿着手机
                    mOrientation = 0
                }else if (orientation in 61..119){
                    //90度，用户右侧横屏拿着手机
                    mOrientation = 90
                }else if (orientation in 151..209){
                    //180度，用户反向竖直拿着手机
                    mOrientation = 180
                }else if (orientation in 241..299){
                    //270度，用户左侧横屏拿着手机
                    mOrientation = 270
                }
                if (mIsLock){
                    return
                }

                //如果用户关闭了手机的屏幕旋转功能，不在开启代码自动旋转，直接return
                //1 手机已开启屏幕旋转功能
                //0 手机未开启屏幕旋转功能
                if (Settings.System.getInt(contentResolver,Settings.System.ACCELEROMETER_ROTATION) == 0){
                    return
                }

                //当检测到用户手机位置距离一次的位置发生了变化，开启屏幕自动旋转
                if (mLastOrientation != mOrientation){
                    when(mOrientation){
                        0 ->{
                            //正向竖屏
                            this@CameraActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        }
                        90 -> {
                            //反向横屏
                            this@CameraActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                        }
                        180 -> {
                            //反向竖屏
//                            this@CameraActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                        }
                        270 -> {
                            //正向横屏（横屏分正向横屏：靠左手反向横屏  反向横屏：靠右手反向横屏）
                            this@CameraActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        }
                    }
                }
            }
        }
}