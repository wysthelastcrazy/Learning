package com.example.commonlib.utils

import android.content.Context
import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager

/**
 * @author wangyasheng
 * usb设备相关
 */
object UsbDevicesHelper {
    enum class DeviceType{
        TYPE_ALL,
        TYPE_CAMERA,
        TYPE_AUDIO,
        TYPE_SPEAKER,
        TYPE_MIC
    }

    fun getUsbDevicesByType(context: Context,deviceType: DeviceType):List<UsbDevice>{
        val usbDeviceList = mutableListOf<UsbDevice>()
        val usbManager: UsbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceMap = usbManager.deviceList
        if (!deviceMap.isNullOrEmpty()){
            deviceMap.forEach { (_, usbDevice) ->
                when(deviceType){
                    DeviceType.TYPE_ALL ->{
                        usbDeviceList.add(usbDevice)
                    }
                    DeviceType.TYPE_CAMERA ->{
                        //usb摄像头
                        if (isUsbCamera(usbDevice)) {
                            usbDeviceList.add(usbDevice)
                        }
                    }
                    DeviceType.TYPE_AUDIO ->{
                        //usb音频设备（不区分扬声器和麦克风）
                        if (isUsbAudio(usbDevice)) {
                            usbDeviceList.add(usbDevice)
                        }
                    }
                    DeviceType.TYPE_MIC ->{
                        //usb 麦克风
                        if (isUsbMicrophone(usbDevice)) {
                            usbDeviceList.add(usbDevice)
                        }
                    }
                    DeviceType.TYPE_SPEAKER ->{
                        //usb扬声器
                        if (isUsbSpeaker(usbDevice)) {
                            usbDeviceList.add(usbDevice)
                        }
                    }
                }
            }
        }
        return usbDeviceList
    }
    fun isUsbCamera(usbDevice: UsbDevice): Boolean {
        val count = usbDevice.interfaceCount
        for (i in 0 until count) {
            val usbInterface = usbDevice.getInterface(i)
            if (usbInterface != null && usbInterface.interfaceClass == UsbConstants.USB_CLASS_VIDEO) {
                return true
            }
        }
        return false
    }
    fun isUsbAudio(usbDevice: UsbDevice):Boolean{
        val count = usbDevice.interfaceCount
        for (i in 0 until count) {
            val usbInterface = usbDevice.getInterface(i)
            if (usbInterface != null && usbInterface.interfaceClass == UsbConstants.USB_CLASS_AUDIO) {
                return true
            }
        }
        return false
    }
    /**
     * 是否为麦克风
     * 此方法未测试，不确定是否正确起效
     * @param usbDevice
     * @return
     */
    fun isUsbMicrophone(usbDevice: UsbDevice):Boolean{
        val count = usbDevice.interfaceCount
        for (i in 0 until count) {
            val usbInterface = usbDevice.getInterface(i)
            if (usbInterface != null && usbInterface.interfaceClass == UsbConstants.USB_CLASS_AUDIO && usbInterface.interfaceSubclass == UsbConstants.USB_CLASS_AUDIO) {
                return true
            }
        }
        return false
    }
     fun isUsbSpeaker(usbDevice: UsbDevice?): Boolean {
        if (usbDevice == null) {
            return false
        }
        val count = usbDevice.interfaceCount
        for (i in 0 until count) {
            val usbInterface = usbDevice.getInterface(i)
            if (usbInterface != null && usbInterface.interfaceClass == UsbConstants.USB_CLASS_AUDIO && usbInterface.interfaceSubclass == UsbConstants.USB_CLASS_COMM) {
                return true
            }
        }
        return false
    }

}