package com.wys.learning.camera

import android.content.Context
import android.hardware.Camera
import android.util.Size
import com.example.commonlib.log.LogUtil
import com.example.commonlib.utils.Rx

class Camera1Capture(context: Context) : CameraCapture(context) {
    companion object {
        private const val TAG = "Camera1Capture"
    }

    private var mCamera: Camera? = null
    private var mCameraInfo: Camera.CameraInfo? = null

    override fun onOpening() {
        LogUtil.i(TAG, "onOpening++++")
        Rx.runIO2UI({
            openCamera()
            true
        }, {
            onOpened()
        })
    }

    override fun onStarting() {
        LogUtil.i(TAG, "onStarting++++")
        Rx.runIO2UI({
            startCameraPreview()
            true
        }, {
            onStarted()
        })
    }

    override fun onStopping() {
        LogUtil.i(TAG, "onStopping++++")
        Rx.runIO2UI({
            stopCameraPreview()
            true
        }, {
            onStopped()
        })
    }

    override fun onClosing() {
        LogUtil.i(TAG, "onClosing++++")
        Rx.runIO2UI({
            releaseCamera()
            true
        }, {
            onClosed()
        })
    }

    override fun getCameraOrientation(): Int {
        return 0
    }

    /**open camera**/
    private fun openCamera() {
        LogUtil.i(TAG, "openCamera++++")
        if (mCamera != null) {
            throw RuntimeException("camera already initialized")
        }
        chooseCamera()
        if (mCamera == null) {
            throw java.lang.RuntimeException("Unable to open camera")
        }

        // params 设置
        val params = mCamera?.parameters

        //fps
        val frameRates = params?.supportedPreviewFpsRange
        frameRates?.let {
            val minFps = it[it.size - 1][Camera.Parameters.PREVIEW_FPS_MIN_INDEX]
            val maxFps = it[it.size - 1][Camera.Parameters.PREVIEW_FPS_MAX_INDEX]
            LogUtil.i(TAG,"openCamera minFps = $minFps, maxFps = $maxFps")
            params.setPreviewFpsRange(minFps, maxFps)
        }

        //size
        val cameraSizes = params?.supportedPreviewSizes
        cameraSizes?.let {
            val sizes = arrayOfNulls<Size>(it.size)
            for (i in 0 until it.size){
                sizes[i] = Size(it[i].width,it[i].height)
            }
            val size = calculateSize(sizes)
            mActualWidth = size.width
            mActualHeight = size.height

            params.setPreviewSize(size.width,size.height)
        }

        params?.setRecordingHint(true)
        mCamera?.parameters = params
    }

    /**
     * 选择摄像头
     */
    private fun chooseCamera() {
        LogUtil.i(TAG, "chooseCamera++++")
        mCameraInfo = Camera.CameraInfo()
        val numCameras = Camera.getNumberOfCameras()
        val face = if (mFacing) Camera.CameraInfo.CAMERA_FACING_FRONT else Camera.CameraInfo.CAMERA_FACING_BACK
        for (i in 0 until numCameras) {
            Camera.getCameraInfo(i, mCameraInfo)
            if (mCameraInfo?.facing == face) {
                mCamera = Camera.open(i)
                LogUtil.i(TAG, "choose camera $i")
                break
            }
        }
        if (mCamera == null) {
            LogUtil.i(TAG, "No camera matching facing was found; opening default")
            mCamera = Camera.open()
        }
    }

    /**
     * 开启预览
     */
    private fun startCameraPreview() {
        if (mCamera == null) {
            LogUtil.w(TAG, "startCameraPreview but camera is null")
            return
        }
        try {
            LogUtil.i(TAG, "startCameraPreview++++")
            mCamera?.setPreviewTexture(mSurfaceTexture)
            mCamera?.startPreview()
        } catch (e: Throwable) {
            LogUtil.e(TAG, "camera start preview failed", e)
        }
    }

    /**
     * 停止预览
     */
    private fun stopCameraPreview() {
        LogUtil.i(TAG, "stopCameraPreview++++")
        Rx.catchAll {
            mCamera?.stopPreview()
        }
        try {
            mCamera?.setPreviewTexture(null)
        } catch (e: Throwable) {
            LogUtil.e(TAG, "camera stop preview failed", e)
        }
    }

    /**
     * release camera
     */
    private fun releaseCamera() {
        LogUtil.i(TAG, "releaseCamera++++")
        if (mCamera == null) {
            return
        }
        Rx.catchAll {
            mCamera?.release()
        }
        mCamera = null
        LogUtil.i(TAG, "release camera -- done")
    }

}