package com.wys.learning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = "MainActivity2";
    //耳机广播
    public static final String TAG_LISTEN = "android.intent.action.HEADSET_PLUG";
    //usb线的广播
    private static final String TAG_USB = "android.hardware.usb.action.USB_STATE";
    //外设的广播
    private static final String TAG_IN = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private static final String TAG_OUT = "android.hardware.usb.action.USB_DEVICE_DETACHED";

    private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        IntentFilter filter = new IntentFilter();
        filter.addAction(TAG_IN);
        filter.addAction(TAG_OUT);
        filter.addAction(TAG_USB);

        registerReceiver(receiver,filter);
    }
    private void hasUsbCamera(){
        UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> mDeviceMap = mUsbManager.getDeviceList();
        if (mDeviceMap != null){
            for (UsbDevice usbDevice: mDeviceMap.values()){

            }
        }
    }
    private boolean isUsbCamera(UsbDevice usbDevice){
        if (usbDevice == null){
            return false;
        }
        int count = usbDevice.getInterfaceCount();
        for (int i = 0;i < count; i++){
            UsbInterface intf = usbDevice.getInterface(i);
            if (intf != null && intf.getInterfaceClass() == UsbConstants.USB_CLASS_AUDIO){
                return true;
            }
        }
        return false;
    }
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(TAG_IN)){
                //外设已经连接
                UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                Log.d(TAG,"attached device:" + usbDevice.getDeviceName());
            }else if (action.equals(TAG_OUT)){
                //外设已经移除
                UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                Log.d(TAG,"detached device:" + usbDevice.getDeviceName());
                usbDevice.getVendorId();
                usbDevice.getProductId();
            }else if (action.equals(TAG_USB)){
                boolean connected = intent.getExtras().getBoolean("connected");
                if (connected){
                    //usb 已经连接
                }else {
                    //usb 断开
                }
            }else if (action.equals(TAG_LISTEN)){
                //state 0 -- 拔出，1 -- 插入
                int intExtra = intent.getIntExtra("state",0);
                //type  1 -- 有麦克风， 0 -- 没有麦克风
                int intType = intent.getIntExtra("microphone",0);
            }
        }
    };
}