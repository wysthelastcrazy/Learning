//package com.example.camera;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.graphics.ImageFormat;
//import android.graphics.Matrix;
//import android.graphics.RectF;
//import android.graphics.SurfaceTexture;
//import android.hardware.camera2.CameraAccessException;
//import android.hardware.camera2.CameraCaptureSession;
//import android.hardware.camera2.CameraCharacteristics;
//import android.hardware.camera2.CameraDevice;
//import android.hardware.camera2.CameraManager;
//import android.hardware.camera2.CaptureRequest;
//import android.hardware.camera2.params.StreamConfigurationMap;
//import android.media.Image;
//import android.media.ImageReader;
//import android.os.Build;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.util.Size;
//import android.view.Surface;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.core.app.ActivityCompat;
//
//
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * @author Leeray
// * @date: 2023/2/7
// */
//public class CameraHelper {
//    /**
//     * imageReader预览帧的key
//     */
//    public static final String PREVIEW_IMAGE_READER = "imageReader";
//    private HandlerThread mBackgroundThread;
//    private Handler mBackgroundHandler;
//    private HandlerThread stTread;
//    private Handler stHandler;
//    private String cameraId = "0";
//    private static CameraHelper cameraHelper;
//    private Context context;
//    private CameraManager cameraManager;
//    private CameraCharacteristics characteristics;
//    private ImageReader imageReader;
//    private Size[] supportPreview;
//    private CameraDevice mCameraDevice;
//    private CameraCaptureSession mPreviewSession;
//    private CaptureRequest.Builder mCaptureRequestBuilder;
//    private CaptureRequest mCaptureRequest;
//
//    // 预览surface列表
//    private List<Surface> surfaceList = new ArrayList<>();
//    // surface与页面对应关系
//    private HashMap<String, Surface> previewMap = new HashMap<>();
//    private boolean mHasPreview = false;
//
//    public static CameraHelper getInstance(Context context) {
//        if (cameraHelper == null) {
//            cameraHelper = new CameraHelper(context);
//        }
//        return cameraHelper;
//    }
//
//    private CameraHelper(Context context) {
//        this.context = context;
//    }
//
//    public void initCamera() {
//        if (cameraManager == null) {
//            cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
//        }
//
//        surfaceList.clear();
//        previewMap.clear();
//        try {
//            String[] cameraIdList = cameraManager.getCameraIdList();
//            if (cameraIdList.length > 0) {
//                this.cameraId = cameraIdList[0];
//            } else {
//                LogUtils.e("initCamera fail, not found camera!!!!");
//                return;
//            }
//            // 获取指定摄像头的特性
//            characteristics = cameraManager.getCameraCharacteristics(this.cameraId);
//            // 获取摄像头支持的配置属性
//            StreamConfigurationMap map = characteristics.get(
//                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//            supportPreview = map.getOutputSizes(SurfaceTexture.class);
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        } catch (NullPointerException e) {
//            LogUtils.e(e.getMessage());
//        }
//        startBackgroundThread();
//        startStThread();
//    }
//
//    private void startBackgroundThread() {
//        mBackgroundThread = new HandlerThread("CameraBackground");
//        mBackgroundThread.start();
//        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
//    }
//
//    private void startStThread() {
//        stTread = new HandlerThread("sendFrame");
//        stTread.start();
//        stHandler = new Handler(stTread.getLooper());
//    }
//
//    public boolean hasCamera() {
//        String[] cameraIdList = new String[0];
//        if (cameraManager == null) {
//            cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
//        }
//        try {
//            cameraIdList = cameraManager.getCameraIdList();
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//        return cameraIdList.length > 0;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void openCamera() {
//        if (cameraManager == null) {
//            LogUtils.e("camera is not init");
//            return;
//        }
//        try {
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
//                    @Override
//                    public void onOpened(@NonNull CameraDevice cameraDevice) {
//                        mCameraDevice = cameraDevice;
//                    }
//
//                    @Override
//                    public void onDisconnected(@NonNull CameraDevice cameraDevice) {
//                        LogUtils.e("onDisconnected");
//                        mCameraDevice = null;
//                    }
//
//                    @Override
//                    public void onError(@NonNull CameraDevice cameraDevice, int i) {
//                        LogUtils.e("onError");
//                        mCameraDevice.close();
//                        mCameraDevice = null;
//                    }
//                }, mBackgroundHandler);
//            }
//
//        } catch (CameraAccessException exception) {
//            LogUtils.e(exception.getMessage());
//        }
//    }
//
//    /**
//     * 移除预览
//     *
//     * @param fragmentTag fragment
//     */
//    public void removePreview(String fragmentTag) {
//        previewMap.remove(fragmentTag);
//        LogUtils.i("stop preview by removePreview from %s", fragmentTag);
//        stopPreview();
//        mHasPreview = false;
//    }
//
//    public void removeAllPreview() {
//        LogUtils.i("removeAllPreview");
//        previewMap.clear();
//        stopPreview();
//        mHasPreview = false;
//    }
//
//    /**
//     * 重新开启预览
//     *
//     * @param fragmentTag
//     */
//    public void reStartPreview(String fragmentTag) {
//        LogUtils.i("open preview from %s", fragmentTag);
//        if (mCaptureRequestBuilder != null && previewMap.containsKey(fragmentTag)) {
//            mCaptureRequestBuilder.addTarget(previewMap.get(fragmentTag));
//            openPreview();
//            mHasPreview = true;
//        }
//    }
//
//    public void previewSizeChange(SurfaceTexture surfaceTexture, String fragmentTag, String cameraType) {
//        stopPreview(fragmentTag);
//        Size mPreviewSize = PreviewSizeUtil.getOptimalSize(supportPreview, cameraType);
//        //设置TextureView的缓冲区大小
//        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
//        //获取Surface显示预览数据
//        Surface mSurface = new Surface(surfaceTexture);
//        previewMap.put(fragmentTag, mSurface);
//        openPreview();
//        mHasPreview = true;
//    }
//
//    /**
//     * 停止预览
//     *
//     * @param fragmentTag
//     */
//    public void stopPreview(String fragmentTag) {
//        LogUtils.i("stop preview from %s", fragmentTag);
//        stopPreview();
//        mHasPreview = false;
//    }
//
//    public void addPreview(@NonNull SurfaceTexture surfaceTexture, String fragmentTag, String cameraType, int angel, int mirror) {
//        Size mPreviewSize = PreviewSizeUtil.getOptimalSize(supportPreview, cameraType);
//        initImageReader(mPreviewSize, angel, mirror);
//
//        //设置TextureView的缓冲区大小
//        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
//        //获取Surface显示预览数据
//        Surface mSurface = new Surface(surfaceTexture);
//        previewMap.put(fragmentTag, mSurface);
//        takePreview();
//        LogUtils.i("open preview by addPreview from %s", fragmentTag);
//    }
//
//    private ReentrantLock lock = new ReentrantLock();
//    private final Integer mImageReaderLock = 1;
//
//    private void initImageReader(Size previewSize, int angel, int mirror) {
//        // 创建一个ImageReader对象，用于获取摄像头的图像数据
//        if (imageReader != null) {
//            if (!previewMap.containsKey(PREVIEW_IMAGE_READER)) {
//                previewMap.put(PREVIEW_IMAGE_READER, imageReader.getSurface());
//            }
//            return;
//        }
//        imageReader = ImageReader.newInstance(previewSize.getWidth(), previewSize.getHeight(),
//                ImageFormat.YUV_420_888, 2);
//        //设置获取图片的监听
//        imageReader.setOnImageAvailableListener(imageReader -> {
//            Image image = imageReader.acquireLatestImage();
//            if (image == null) {
//                return;
//            }
//            synchronized (mImageReaderLock) {
//                if (!mImageReaderLock.equals(1)) {
//                    image.close();
//                    return;
//                }
//                if (ImageFormat.YUV_420_888 == image.getFormat()) {
//                    lock.lock();
//                    try {
//                        byte[] bytes = ImageUtil.yuv420ToNv21(image);
//                        if (mHasPreview) {
//                            byte[] newBytes = new byte[bytes.length];
//                            if (angel == 90) {
//                                BitMapUtils.rotateNV21_90(bytes, newBytes, previewSize.getHeight(),
//                                        previewSize.getWidth());
//                            } else if (angel == 180) {
//                                BitMapUtils.rotateNV21_180(bytes, newBytes, previewSize.getHeight(),
//                                        previewSize.getWidth());
//                            } else if (angel == 270) {
//                                BitMapUtils.rotateNV21_270(bytes, newBytes, previewSize.getHeight(),
//                                        previewSize.getWidth());
//                            } else {
//                                newBytes = bytes;
//                            }
//
//                            // 是否需要镜面旋转
//                            if (mirror == 180) {
//                                newBytes = BitMapUtils.frameMirror(newBytes, previewSize.getWidth(), previewSize.getHeight());
//                            }
//                            if (angel == 0 && mirror == 0) {
//                                sendFrameToSt(newBytes, image.getWidth(), image.getHeight());
//                            } else {
//                                sendFrameToSt(newBytes, previewSize.getWidth(), previewSize.getHeight());
//                            }
//                        }
//                    } catch (Exception exception) {
//                        LogUtils.e("onImageAvailable error: %s", exception.getMessage());
//                    } finally {
//                        lock.unlock();
//                    }
//                }
//            }
//            image.close();
//        }, mBackgroundHandler);
//        previewMap.put(PREVIEW_IMAGE_READER, imageReader.getSurface());
//    }
//
//    private void takePreview() {
//        if (mCameraDevice == null) {
//            openCamera();
//            return;
//        }
//        try {
//            //创建预览请求
//            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice
//                    .TEMPLATE_PREVIEW);
//
//            // 设置自动对焦模式
//            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
//                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
//            surfaceList.clear();
//            for (Map.Entry<String, Surface> surfaceEntry : previewMap.entrySet()) {
//                if (surfaceEntry.getValue() == null) {
//                    continue;
//                }
//                if (PREVIEW_IMAGE_READER.equals(surfaceEntry.getKey())) {
//                    surfaceList.add(surfaceEntry.getValue());
//                } else {
//                    surfaceList.add(0, surfaceEntry.getValue());
//                }
//                mCaptureRequestBuilder.addTarget(surfaceEntry.getValue());
//            }
//
//            //创建相机捕获会话，第一个参数是捕获数据的输出Surface列表，第二个参数是CameraCaptureSession的状态回调接口，当它创建好后会回调onConfigured方法，第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
//
//            if (!hasPreview()) {
//                LogUtils.e("not preview surface");
//                return;
//            }
//            mCameraDevice.createCaptureSession(surfaceList, new CameraCaptureSession.StateCallback() {
//                @Override
//                public void onConfigured(CameraCaptureSession session) {
//                    mPreviewSession = session;
//                    openPreview();
//                }
//
//                @Override
//                public void onConfigureFailed(CameraCaptureSession session) {
//
//                }
//            }, mBackgroundHandler);
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 是否存在预览页面
//     *
//     * @return
//     */
//    private boolean hasPreview() {
//        boolean hasPreview = false;
//        for (Map.Entry<String, Surface> surfaceEntry : previewMap.entrySet()) {
//            if (!PREVIEW_IMAGE_READER.equals(surfaceEntry.getKey())) {
//                hasPreview = true;
//                break;
//            }
//        }
//        mHasPreview = hasPreview;
//        return hasPreview;
//    }
//
//    public Matrix configureTextureViewTransform(int viewWidth, int viewHeight, int width,
//                                                int height, int cameraRotation) {
//        Matrix matrix = new Matrix();
//        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
//        RectF bufferRect = new RectF(0, 0, width,
//                height);
//        float centerX = viewRect.centerX();
//        float centerY = viewRect.centerY();
//        if (cameraRotation == 90 || cameraRotation == 270) {
//            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
//            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
//            float scale = Math.max(
//                    (float) viewHeight / viewWidth,
//                    (float) viewWidth / viewHeight);
//            matrix.postScale(scale, scale, centerX, centerY);
//            matrix.postRotate(cameraRotation, centerX, centerY);
//        } else if (180 == cameraRotation) {
//            matrix.postRotate(180, centerX, centerY);
//        }
//        return matrix;
//    }
//
//    /**
//     * 数据帧发送商汤
//     *
//     * @param frameInfo
//     * @param width
//     * @param height
//     */
//    private void sendFrameToSt(byte[] frameInfo, int width, int height) {
//        stHandler.post(() -> {
//            StImage stImage = new StImage(width, height, width, StImage.StPixelFormat.PixFmtNV21,
//                    frameInfo);
//            ShangTangThreadManager.QueueInfo queueInfo = new ShangTangThreadManager
//                    .QueueInfo(System.currentTimeMillis(), stImage);
//            ShangTangThreadManager.mAllImagesQueue[0].offer(queueInfo);
//        });
//    }
//
//    /**
//     * 开启预览
//     */
//    private void openPreview() {
//        if (mCameraDevice == null) {
//            openCamera();
//        }
//        try {
//            //开始预览
//            mCaptureRequest = mCaptureRequestBuilder.build();
//
//            //设置反复捕获数据的请求，这样预览界面就会一直有数据显示
//            mPreviewSession.setRepeatingRequest(mCaptureRequest, null, mBackgroundHandler);
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 停止预览
//     */
//    private void stopPreview() {
//        try {
//            if (mPreviewSession != null && mCameraDevice != null) {
//                mPreviewSession.stopRepeating();
//            }
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void stopBackgroundThread() {
//        if (mBackgroundThread == null || stTread == null) {
//            return;
//        }
//        mBackgroundThread.quitSafely();
//        stTread.quitSafely();
//        try {
//            mBackgroundThread.join();
//            mBackgroundThread = null;
//            mBackgroundHandler = null;
//            stTread.join();
//            stTread = null;
//            stHandler = null;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 停止拍照释放资源
//     */
//    private void stopCamera() {
//        if (imageReader != null) {
//            imageReader.setOnImageAvailableListener(null, null);
//            imageReader.close();
//            imageReader = null;
//        }
//        if (mPreviewSession != null) {
//            stopPreview();
//            previewMap.clear();
//            mPreviewSession.close();
//            mPreviewSession = null;
//        }
//        if (mCameraDevice != null) {
//            mCameraDevice.close();
//            mCameraDevice = null;
//        }
//        if (cameraManager != null) {
//            cameraManager = null;
//        }
//    }
//
//    public void cameraRelease() {
//        stopCamera();
//        stopBackgroundThread();
//    }
//}