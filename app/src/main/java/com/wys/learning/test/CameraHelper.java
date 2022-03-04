//package com.wys.learning.test;
//
//import android.content.Context;
//import android.graphics.SurfaceTexture;
//import android.hardware.Camera;
//import android.hardware.camera2.CameraAccessException;
//import android.hardware.camera2.CameraCharacteristics;
//import android.hardware.camera2.CameraManager;
//import android.hardware.camera2.params.StreamConfigurationMap;
//import android.hardware.usb.UsbConstants;
//import android.hardware.usb.UsbDevice;
//import android.hardware.usb.UsbInterface;
//import android.hardware.usb.UsbManager;
//import android.media.ImageReader;
//import android.util.Size;
//
//import com.xiaomi.mitv.tvvideocall.App;
//import com.xiaomi.mitv.tvvideocall.log.LogUtil;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * Created by eng on 19-6-1.
// * This class provides state check about camera.
// */
//public class CameraHelper {
//    private static final String TAG = "CameraAdapter";
//
//    private static volatile CameraHelper sInstance;
//    private Context mContext;
//
//    private CameraHelper(Context context) {
//        this.mContext = context.getApplicationContext();
//        init();
//    }
//
//    public static CameraHelper getInstance(Context context) {
//        if (sInstance == null) {
//            synchronized (CameraHelper.class) {
//                if (sInstance == null) {
//                    sInstance = new CameraHelper(context);
//                }
//            }
//        }
//
//        return sInstance;
//    }
//
//    private Context getContext() {
//        return mContext;
//    }
//
//    private void init() {
//        LogUtil.i(TAG, "init");
//    }
//
//    /**
//     * Get the num of cameras that connects to the device.
//     */
//    public static int getNumOfCameras() {
//        int number = Camera.getNumberOfCameras();
//        LogUtil.i(TAG, "camera num: " + number);
//        return number;
//    }
//
//    /**
//     * Get the num of cameras from {@link CameraManager}.
//     * <p>
//     * Since android recommend using the new {@link android.hardware.camera2} API,
//     * maybe {@link #getNumOfCameras()} will be deprecated.
//     */
//    public int getNumOfCameras2() {
//        int newNumber = 0;
//        try {
//            CameraManager mgr = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
//            newNumber = mgr.getCameraIdList().length;
//        } catch (Exception e) {
//            LogUtil.e(TAG, "error: " + e);
//        }
//
//        LogUtil.i(TAG, "camera2 num: " + newNumber);
//        return newNumber;
//    }
//
//    public boolean hasUsbCamera() {
//        UsbManager mUsbManager = (UsbManager) getContext().getSystemService(Context.USB_SERVICE);
//        HashMap<String, UsbDevice> mDeviceMap = mUsbManager.getDeviceList();
//        if (mDeviceMap != null) {
//            for (UsbDevice usbDevice : mDeviceMap.values()) {
//                if (isUsbCamera(usbDevice)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public UsbDevice getUsbCameraDevice() {
//        UsbManager mUsbManager = (UsbManager) getContext().getSystemService(Context.USB_SERVICE);
//        HashMap<String, UsbDevice> mDeviceMap = mUsbManager.getDeviceList();
//        if (mDeviceMap != null) {
//            for (UsbDevice usbDevice : mDeviceMap.values()) {
//                if (isUsbCamera(usbDevice)) {
//                    return usbDevice;
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Determine a USB device attached to the android device is USB camera or not.
//     * From internet(maybe not right).
//     */
//    public static boolean isUsbCamera0(UsbDevice usbDevice) {
//        return usbDevice != null &&
//                UsbConstants.USB_CLASS_MISC == usbDevice.getDeviceClass() &&
//                UsbConstants.USB_CLASS_COMM == usbDevice.getDeviceSubclass();
//    }
//
//    /**
//     * Determine a USB device attached to the android device is USB camera or not.
//     * From BSP engineer.
//     */
//    public static boolean isUsbCamera(UsbDevice usbDevice) {
//        if (usbDevice == null) {
//            return false;
//        }
//
//        int count = usbDevice.getInterfaceCount();
//        if (App.getDebugStatus()) {
//            for (int i = 0; i < count; i++) {
//                UsbInterface intf = usbDevice.getInterface(i);
//                LogUtil.d(TAG, "isCamera UsbInterface: " + intf);
//            }
//        }
//
//        for (int i = 0; i < count; i++) {
//            UsbInterface intf = usbDevice.getInterface(i);
//            if (intf != null && intf.getInterfaceClass() == UsbConstants.USB_CLASS_VIDEO) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * This method provides the camera info via {@link Camera}
//     */
//    @Deprecated
//    private static void getCameraInfo(CameraInfo cameraInfo) {
//        int number = getNumOfCameras();
//        if (number > 0) {
//            Camera.CameraInfo info = new Camera.CameraInfo();
//            Camera.getCameraInfo(0, info);
//            if (App.getDebugStatus()) {
//                LogUtil.i(TAG, "facing(0-back): " + info.facing);
//                LogUtil.i(TAG, "orientation: " + info.orientation);
//                LogUtil.d(TAG, "canDisableShutterSound: " + info.canDisableShutterSound);
//            }
//            try {
//                Camera camera = Camera.open(0);
//                Camera.Parameters params = camera.getParameters();
//                Camera.Size supportedPicSize = params.getPreviewSize();
//                Camera.Size supportedVideoSize = params.getPreferredPreviewSizeForVideo();
//                if (supportedPicSize == null) {
//                    LogUtil.i(TAG, "supportedPicSize: null");
//                } else {
//                    String res = supportedPicSize.width + "x" + supportedPicSize.height;
//                    cameraInfo.setPicturePreviewSize(res);
//                    if (App.getDebugStatus()) {
//                        LogUtil.i(TAG, "supportedPicSize: " + res);
//                    }
//                }
//
//                if (supportedVideoSize == null) {
//                    LogUtil.i(TAG, "supportedVideoSize: null");
//                } else {
//                    String res = supportedVideoSize.width + "x" + supportedVideoSize.height;
//                    cameraInfo.setVideoPreviewSize(res);
//                    if (App.getDebugStatus()) {
//                        LogUtil.i(TAG, "supportedVideoSize: " + res);
//                    }
//                }
//
//                if (App.getDebugStatus()) {
//                    List<Camera.Size> supportedVideoSizes = params.getSupportedVideoSizes();
//                    if (supportedVideoSizes == null) {
//                        LogUtil.i(TAG, "supportedVideo Sizes: null");
//                    } else {
//                        LogUtil.i(TAG, "supportedVideo Sizes: " + supportedVideoSizes.size());
//                        for (Camera.Size item : supportedVideoSizes) {
//                            if (item != null) {
//                                LogUtil.i(TAG, "supported: " + item.width + "x" + item.height);
//                            }
//                        }
//                    }
//                }
//                camera.release();
//                LogUtil.i(TAG, "camera info release");
//            } catch (Exception e) {
//                LogUtil.e(TAG, "Info: " + e.toString());
//            }
//        } else {
//            LogUtil.i(TAG, "no camera");
//        }
//    }
//
//    private static Size getMaxSize(Size... sizes) {
//        if (sizes == null || sizes.length == 0) {
//            throw new IllegalArgumentException("sizes was empty");
//        }
//
//        Size sz = sizes[0];
//        for (Size size : sizes) {
//            if (size.getWidth() * size.getHeight() > sz.getWidth() * sz.getHeight()) {
//                sz = size;
//            }
//        }
//
//        return sz;
//    }
//
//    /**
//     * This method provides the camera info via {@link android.hardware.camera2}
//     */
//    private void getCameraInfo2(CameraInfo cameraInfo) {
//        int number = getNumOfCameras();
//        if (number > 0) {
//            String cameraId = String.valueOf(0);
//            CameraManager cameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
//            try {
//                CameraCharacteristics characteristics =
//                        cameraManager.getCameraCharacteristics(cameraId);
//                StreamConfigurationMap streamConfigurationMap =
//                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//                if (streamConfigurationMap != null) {
//                    Size[] previewSizes = streamConfigurationMap.getOutputSizes(SurfaceTexture.class);
//                    if (previewSizes != null && previewSizes.length > 0) {
//                        Size maxPreviewSize = getMaxSize(previewSizes);
//                        String resPreview = maxPreviewSize.getWidth() + "x" + maxPreviewSize.getHeight();
//                        cameraInfo.setVideoPreviewSize(resPreview);
//
//                        if (App.getDebugStatus()) {
//                            LogUtil.i(TAG, "preview size: " + previewSizes.length);
//                            for (Size item : previewSizes) {
//                                LogUtil.i(TAG, "preview: " + item.getWidth() + "x" + item.getHeight());
//                            }
//                            LogUtil.i(TAG, "max preview size: " + resPreview);
//                        }
//                    } else {
//                        CameraInfo tmp = new CameraInfo();
//                        getCameraInfo(tmp);
//                        cameraInfo.setVideoPreviewSize(tmp.getVideoPreviewSize());
//                    }
//
//                    Size[] pictureSizes = streamConfigurationMap.getOutputSizes(ImageReader.class);
//                    if (pictureSizes != null && pictureSizes.length > 0) {
//                        Size maxPictureSize = getMaxSize(pictureSizes);
//                        String resPicture = maxPictureSize.getWidth() + "x" + maxPictureSize.getHeight();
//                        cameraInfo.setPicturePreviewSize(resPicture);
//
//                        if (App.getDebugStatus()) {
//                            LogUtil.i(TAG, "picture size: " + pictureSizes.length);
//                            for (Size item : pictureSizes) {
//                                LogUtil.d(TAG, "picture: " + item.getWidth() + "x" + item.getHeight());
//                            }
//                            LogUtil.i(TAG, "max picture size: " + resPicture);
//                        }
//                    } else {
//                        CameraInfo tmp = new CameraInfo();
//                        getCameraInfo(tmp);
//                        cameraInfo.setPicturePreviewSize(tmp.getPicturePreviewSize());
//                    }
//                }
//            } catch (CameraAccessException e) {
//                LogUtil.e(TAG, "CameraAccessException: " + e.toString());
//            } catch (Exception e) {
//                LogUtil.e(TAG, "Exception: " + e.toString());
//                getCameraInfo(cameraInfo);
//            }
//        } else {
//            LogUtil.w(TAG, "no camera");
//        }
//    }
//
//
//    /**
//     * Get the product info of attached usb camera
//     */
//    public CameraInfo getProductInfo() {
//        LogUtil.d(TAG, "getProductInfo");
//        int number = getNumOfCameras();
//        if (number <= 0) {
//            return null;
//        }
//
//        CameraInfo cameraInfo = new CameraInfo();
//
//        UsbManager manager = (UsbManager) getContext().getSystemService(Context.USB_SERVICE);
//        try {
//            HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
//            for (UsbDevice device : deviceList.values()) {
//                boolean isUsbCam = isUsbCamera(device);
//                boolean isUsbCam0 = isUsbCamera0(device);
//                if (isUsbCam || isUsbCam0) {
//                    String productName = device.getProductName();
//                    String mfgName = device.getManufacturerName();
//                    if (App.getDebugStatus()) {
//                        LogUtil.d(TAG, "product name: " + productName);
//                        LogUtil.d(TAG, "mfg name: " + mfgName);
//                    }
//                    if (productName != null) {
//                        cameraInfo.setProductName(productName);
//                        if (mfgName != null) {
//                            cameraInfo.setManufacturerName(mfgName);
//                        }
//                        break;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            LogUtil.e(TAG, "Info: " + e.toString());
//        }
//
//        getCameraInfo2(cameraInfo);
//
//        return cameraInfo;
//    }
//
//    /**
//     * Get the usb (audio) input device number
//     */
//    // As audio engineer told, video call should stop service and guide the user to keep camera only
//    // when there are more than one (audio) input devices.
//    public static int getUSBInputDeviceNum() {
//        LogUtil.d(TAG, "getUSBInputDeviceNum start");
//
//        int numOfUsbInputDevices = 0;
//        try {
//
//            // Recommended invoke
////            IBinder b = ServiceManager.getService(Context.USB_SERVICE);
////            IUsbManager usbservice = IUsbManager.Stub.asInterface(b);
////            try{
////                int num = usbservice.getUsbAlsaInputDevicesNum();
////                LogUtil.d(TAG, "xiaomi:" + num);
////            }catch (Exception e) {
////                LogUtil.d(TAG, "xiaomi:" + "usbservice.getAlsaDevicesNum() fail");
////            }
//
//            // Reflection invoke
//            Class ServiceManager = Class.forName("android.os.ServiceManager");
//            Method getService = ServiceManager.getMethod("getService", String.class);
//            Object oRemoteService = getService.invoke(null, Context.USB_SERVICE);
//
//            Class cStub = Class.forName("android.hardware.usb.IUsbManager$Stub");
//            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
//            Object oIUsbManager = asInterface.invoke(null, oRemoteService);
//
//            Method getNum = oIUsbManager.getClass().getMethod("getUsbAlsaInputDevicesNum");
//
//            numOfUsbInputDevices = (int) getNum.invoke(oIUsbManager, (Object) null);
//
//            LogUtil.d(TAG, "getUSBInputDeviceNum: num = " + numOfUsbInputDevices);
//        } catch (ClassNotFoundException e) {
//            LogUtil.d(TAG, "ClassNotFoundException: " + e.getMessage());
//        } catch (SecurityException e) {
//            LogUtil.d(TAG, "SecurityException: " + e.getMessage());
//        } catch (NoSuchMethodException e) {
//            LogUtil.d(TAG, "NoSuchMethodException: " + e.getMessage());
//        } catch (IllegalArgumentException e) {
//            LogUtil.d(TAG, "IllegalArgumentException: " + e.getMessage());
//        } catch (IllegalAccessException e) {
//            LogUtil.d(TAG, "IllegalAccessException: " + e.getMessage());
//        } catch (InvocationTargetException e) {
//            LogUtil.d(TAG, "InvocationTargetException: " + e.getMessage());
//        } catch (Exception e) {
//            LogUtil.d(TAG, "Exception: " + e.getMessage());
//        }
//
//        LogUtil.d(TAG, "getUSBInputDeviceNum end");
//        return numOfUsbInputDevices;
//    }
//}