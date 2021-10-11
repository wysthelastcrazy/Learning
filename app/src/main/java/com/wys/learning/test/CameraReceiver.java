//package com.wys.learning.test;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.hardware.usb.UsbDevice;
//import android.hardware.usb.UsbManager;
//import android.util.Log;
//
//import com.xiaomi.mitv.tvvideocall.ChatLinkingReceiver;
//import com.xiaomi.mitv.tvvideocall.log.LogUtil;
//import com.xiaomi.mitv.tvvideocall.preference.PreferenceHelper;
//import com.xiaomi.mitv.tvvideocall.rx.com.wys.learning.utils.Rx;
//import com.xiaomi.mitv.tvvideocall.utils.MiStatUtil;
//import com.xiaomi.mitv.tvvideocall.utils.ThreadPool;
//
///**
// * This class receives and handles the broadcast that USB camera attached/detached.
// */
//public class CameraReceiver extends BroadcastReceiver {
//    private static final String TAG = "CameraReceiver";
//
//    private static final String ACTION_USB_DEVICE_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
//    private static final String ACTION_USB_DEVICE_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
//
//    private static final String ACTION_USB_CAMERA = "com.xiaomi.mitv.tvvideocall.action.USB_CAMERA";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if (intent == null) {
//            LogUtil.i(TAG, "onReceive: null");
//            return;
//        }
//
//        String action = intent.getAction();
//        Log.i(TAG, "onReceive action: " + action);
//        LogUtil.d(TAG, "onReceive action: " + action);
//
//        boolean isAgreed = PreferenceHelper.getBoolean(PreferenceHelper.KEY_APP_PRIVACY_STATE, false);
//        boolean isLogin = !PreferenceHelper.getString(PreferenceHelper.KEY_XIAOMI_ACCOUNT, "").equals("");
//
//        if (!isAgreed || !isLogin) {
//            Log.i(TAG, "onReceive No account");
//            LogUtil.i(TAG, "No account");
//            return;
//        }
//
//        UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//        boolean isUsbCam = CameraHelper.isUsbCamera(usbDevice);
//        boolean isUsbCam0 = CameraHelper.isUsbCamera0(usbDevice);
//        LogUtil.d(TAG, "isUsbCamera - " + isUsbCam + "/" + isUsbCam0);
//
//        if (isUsbCam || isUsbCam0) {
//            com.wys.learning.utils.Rx.async(()->CameraHelper.getNumOfCameras(), numOfCameras-> {
//                LogUtil.i(TAG, "camera num is " + numOfCameras);
//                if (ACTION_USB_DEVICE_DETACHED.equals(action)) {
//                    dismissLink(context);
//                } else if (ACTION_USB_DEVICE_ATTACHED.equals(action)) {
//
//                } else {
//                    LogUtil.i(TAG, "unknown action");
//                }
//            });
//        }
//    }
//
//    private void dismissLink(Context context) {
//        Intent intent = new Intent();
//        intent.setAction(ChatLinkingReceiver.ACTION_INVITE_CANCEL);
//        context.sendBroadcast(intent);
//    }
//}
