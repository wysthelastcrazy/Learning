package com.wys.learning;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsbDevicesHelper {
    private static final String TAG = "UsbDevicesHelper";
    enum DeviceType{
        TYPE_ALL,
        TYPE_CAMERA,
        TYPE_AUDIO
    }
    private Context mContext;

    private static class UsbDevicesHelperHolder{
        private static UsbDevicesHelper instance = new UsbDevicesHelper();
    }
    private UsbDevicesHelper(){}

    public static UsbDevicesHelper getInstance(){
        return UsbDevicesHelperHolder.instance;
    }
    public void init(@NonNull Context context){
        mContext = context;
    }

    /**
     * 获取指定类型Usb设备
     */
    public List<UsbDevice> getUsbDevices(DeviceType deviceType){
        Log.d(TAG,"[getUsbDevices] deviceType:" + deviceType);
        UsbManager mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> mDeviceMap = mUsbManager.getDeviceList();
        List<UsbDevice> usbDeviceList = new ArrayList<>();
        if (mDeviceMap != null){
            for (UsbDevice usbDevice: mDeviceMap.values()){
                if (deviceType == DeviceType.TYPE_CAMERA) {
                    if (isUsbCamera(usbDevice)) {
                        usbDeviceList.add(usbDevice);
                    }
                }else if (deviceType == DeviceType.TYPE_AUDIO){
                    if (isUsbAudio(usbDevice)){
                        usbDeviceList.add(usbDevice);
                    }
                }else if (deviceType == DeviceType.TYPE_ALL){
                    usbDeviceList.add(usbDevice);
                }

            }
        }
        return usbDeviceList;
    }
//    deviceName = /dev/bus/usb/003/002, deviceClass = 239, deviceId = 3002, deviceSubclass = 2
//    deviceName = /dev/bus/usb/004/002, deviceClass = 202, deviceId = 4002, deviceSubclass = 8

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

    private boolean isUsbAudio0(UsbDevice usbDevice){
        return usbDevice != null
                && 202 == usbDevice.getDeviceClass()
                && 8 == usbDevice.getDeviceSubclass();
    }

    /**
     * 判断usb设备为audio设备
     * @param usbDevice
     * @return
     */
    private boolean isUsbAudio(UsbDevice usbDevice){
        if (usbDevice == null){
            return false;
        }
        int count = usbDevice.getInterfaceCount();
        for (int i = 0; i < count; i++){
            UsbInterface usbInterface = usbDevice.getInterface(i);
            if (usbInterface != null && usbInterface.getInterfaceClass() == UsbConstants.USB_CLASS_AUDIO){
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为麦克风
     * 此方法未测试，不确定是否正确起效
     * @param usbDevice
     * @return
     */
    private boolean isMicrophone(UsbDevice usbDevice){
        if (usbDevice == null){
            return false;
        }
        int count = usbDevice.getInterfaceCount();
        for (int i = 0; i < count; i++){
            UsbInterface usbInterface = usbDevice.getInterface(i);
            if (usbInterface != null && usbInterface.getInterfaceClass() == UsbConstants.USB_CLASS_AUDIO
                    && usbInterface.getInterfaceSubclass() == UsbConstants.USB_CLASS_AUDIO){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为小米会议一体机
     * @param usbDevice
     * @return
     */
    private boolean isMiConferenceBar(UsbDevice usbDevice){
        if (usbDevice == null){
            return false;
        }
        if ("Mi Conference Bar".equalsIgnoreCase(usbDevice.getManufacturerName())
                && usbDevice.getDeviceClass() == 202
                && usbDevice.getDeviceSubclass() == 8){
            return true;
        }
        return false;
    }
}
