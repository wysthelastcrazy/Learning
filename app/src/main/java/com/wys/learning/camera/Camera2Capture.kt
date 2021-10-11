package com.wys.learning.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.os.Handler
import android.os.Looper
import android.util.Range
import android.view.Surface
import com.example.commonlib.log.LogUtil
import java.util.*

class Camera2Capture(context: Context) : CameraCapture(context) {
    companion object {
        private const val TAG = "Camera2Capture"
    }

    private var mCameraDevice: CameraDevice? = null
    private val mCameraManager: CameraManager =
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var mSurface: Surface? = null

    private var mCaptureSession: CameraCaptureSession? = null
    private var mCameraInfo: CameraCharacteristics? = null
    private var mSeqId: Int? = null

    private val mHandler: Handler = Handler(Looper.getMainLooper())

    override fun onOpening() {
        LogUtil.i(TAG, "onOpening++++")
        try {
            openCamera()
        } catch (e: CameraAccessException) {
            LogUtil.e(TAG, "open camera failed", e)
        }
    }

    override fun onStarting() {
        LogUtil.i(TAG, "onStarting++++")
        if (mSurface == null) {
            mSurface = Surface(mSurfaceTexture)
        }
        val surfaces = Collections.singletonList(mSurface)
        try {
            mCameraDevice?.createCaptureSession(
                surfaces,
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(p0: CameraCaptureSession) {

                    }

                    override fun onConfigureFailed(p0: CameraCaptureSession) {

                    }

                    override fun onReady(session: CameraCaptureSession) {
                        super.onReady(session)
                        if (mState != State.STARTING) {
                            return
                        }
                        mCaptureSession = session
                        startCapture()
                    }

                    override fun onClosed(session: CameraCaptureSession) {
                        super.onClosed(session)
                        mCaptureSession = null
                        this@Camera2Capture.onStopped()
                    }
                },
                mHandler
            )
        } catch (e: Throwable) {
            LogUtil.e(TAG, "createCaptureSession", e)
        }
    }

    override fun onStopping() {
        LogUtil.i(TAG, "onStopping++++")
        if (mCaptureSession == null) {
            return
        }
        mCaptureSession?.close()
    }

    override fun onClosing() {
        LogUtil.i(TAG, "onClosing++++")
        if (mCameraDevice == null) {
            return
        }
        mCameraDevice?.close()
    }

    /**
     * 打开camera
     */
    @SuppressLint("MissingPermission")
    private fun openCamera() {
        if (mCameraDevice != null) {
            throw RuntimeException("camera already initialized")
        }
        val cameraId = chooseCamera() ?: throw RuntimeException("Unable to open camera")

        mCameraInfo = mCameraManager.getCameraCharacteristics(cameraId)
        val map: StreamConfigurationMap? = mCameraInfo?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

        val sizes = map?.getOutputSizes(SurfaceTexture::class.java)
        sizes?.let {
            val size = calculateSize(it)
            mActualWidth = size.width
            mActualHeight = size.height
        }

        mCameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                mCameraDevice = camera
                this@Camera2Capture.onOpened()
            }

            override fun onDisconnected(p0: CameraDevice) {
                LogUtil.i(TAG, "camera onDisconnected")
            }

            override fun onClosed(camera: CameraDevice) {
                super.onClosed(camera)
                LogUtil.i(TAG, "camera closed")
                mCameraDevice = null
                this@Camera2Capture.onClosed()
            }

            override fun onError(p0: CameraDevice, p1: Int) {
                LogUtil.e(TAG, "camera error $p1")
            }

        }, mHandler)
    }

    /**
     * 选择camera并获取cameraId
     */
    private fun chooseCamera(): String? {
        val targetFacing = if (mFacing)CameraCharacteristics.LENS_FACING_FRONT else CameraCharacteristics.LENS_FACING_BACK
        val ids = mCameraManager.cameraIdList
        if (ids.isNullOrEmpty()) {
            return null
        }
        var targetId: String? = null
        ids.forEach { id ->
            val info = mCameraManager.getCameraCharacteristics(id)
            if (info.get(CameraCharacteristics.LENS_FACING) == targetFacing) {
                targetId = id
                return@forEach
            }
        }
        if (targetId == null) {
            targetId = ids[0]
        }
        return targetId
    }

    /**
     * 开始采集(预览)
     */
    private fun startCapture() = try {
        /**
         * templateType参数有以下选项：
         * CameraDevice.TEMPLATE_PREVIEW - 预览使用
         * CameraDevice.TEMPLATE_RECORD - 视频录制
         * CameraDevice.TEMPLATE_STILL_CAPTURE - 拍照使用
         * CameraDevice.TEMPLATE_MANUAL - 手动设置
         *
         * CameraDevice.TEMPLATE_VIDEO_SNAPSHOT
         * CameraDevice.TEMPLATE_ZERO_SHUTTER_LAG
         */
        val builder = mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_RECORD)

        val fpsRanges: Array<Range<Int>>? = mCameraInfo?.get<Array<Range<Int>>>(
            CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES
        )
        if (fpsRanges != null) {
            builder!!.set(
                CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE,
                fpsRanges[fpsRanges.size - 1]
            )
        }

        builder!!.set(
            CaptureRequest.CONTROL_CAPTURE_INTENT,
            CameraMetadata.CONTROL_CAPTURE_INTENT_VIDEO_RECORD
        )
        mSurface?.let { builder.addTarget(it) }

        val captureRequest = builder.build()

        mSeqId = mCaptureSession?.setRepeatingRequest(
            captureRequest,
            object : CameraCaptureSession.CaptureCallback() {
                override fun onCaptureStarted(
                    session: CameraCaptureSession,
                    request: CaptureRequest,
                    timestamp: Long,
                    frameNumber: Long
                ) {
                    if (mState != State.STARTING) {
                        return
                    }
                    onStarted()
                }

                override fun onCaptureFailed(
                    session: CameraCaptureSession,
                    request: CaptureRequest,
                    failure: CaptureFailure
                ) {
                    super.onCaptureFailed(session, request, failure)
                    if (failure.frameNumber % 20L == 0L) {
                        LogUtil.e(TAG, "onCaptureFailed reason =" + failure.reason)
                    }
                }
            },
            mHandler
        )
    } catch (e: Throwable) {
        LogUtil.e(TAG, "setRepeatingRequest", e)
    }
}