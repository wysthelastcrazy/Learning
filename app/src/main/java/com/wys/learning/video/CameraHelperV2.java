package com.wys.learning.video;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.media.MicrophoneInfo;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 摄像头Helper
 */
public class CameraHelperV2 {
    private static final String TAG = "CameraHelperV2";
    private Context mContext;

    private static class CameraHelperHolder{
        private static CameraHelperV2 instance = new CameraHelperV2();
    }
    private CameraHelperV2(){}

    public static CameraHelperV2 getInstance(){
        return CameraHelperHolder.instance;
    }

    public void init(Context context){
        mContext = context;
    }

    public int getNumOfCameras(){
        int number = Camera.getNumberOfCameras();
        return number;
    }
    public int getNumOfCameras2(){
        int newNumber = 0;
        try {
            CameraManager mgr = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
            newNumber = mgr.getCameraIdList().length;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return newNumber;
    }
    public List<MCameraDevice> getAvailableCameraDevice(){
        List<MCameraDevice> cameraDevices = new ArrayList<>();
        try {
            CameraManager cm = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
            String[] cameraIds = cm.getCameraIdList();
            if (cameraIds != null){
                Log.d(TAG,"[getAvailableCameraDevice] cameraIds size:" + cameraIds.length);
                for (int i = 0; i < cameraIds.length; i++){
                    MCameraDevice device = new MCameraDevice();
                    device.cameraId = cameraIds[i];
                    cameraDevices.add(device);
                    Log.d(TAG,"[getAvailableCameraDevice] cameraId:" + device.cameraId);
                }
            }else{
                Log.d(TAG,"[getAvailableCameraDevice] cameraIds is null");
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return cameraDevices;
    }
    public class MCameraDevice{
        public String cameraId;
    }

    /**
     * 是否有usb摄像头
     * @return
     */
    public boolean hasUsbCamera(){
        UsbManager mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> mDeviceMap = mUsbManager.getDeviceList();
        if (mDeviceMap != null){
            for (UsbDevice usbDevice: mDeviceMap.values()){
                if (isUsbCamera(usbDevice)){

                    //usb摄像头
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isUsbCamera0(UsbDevice usbDevice){
        return usbDevice != null
                && UsbConstants.USB_CLASS_MISC == usbDevice.getDeviceClass()
                && UsbConstants.USB_CLASS_COMM == usbDevice.getDeviceSubclass();
    }

    /**
     * 判断usb设备是否为摄像头
     * @param usbDevice
     * @return
     */
    private boolean isUsbCamera(UsbDevice usbDevice){
        if (usbDevice == null){
            return false;
        }
        int count = usbDevice.getInterfaceCount();
        for (int i = 0; i < count; i++){
            UsbInterface usbInterface = usbDevice.getInterface(i);
            if (usbInterface != null && usbInterface.getInterfaceClass() == UsbConstants.USB_CLASS_VIDEO){
                return true;
            }
        }
        return false;
    }
}
