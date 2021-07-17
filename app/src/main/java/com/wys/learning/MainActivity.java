package com.wys.learning;

import android.Manifest;
import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wys.learning.audio.AudioHelper;
import com.wys.learning.video.CameraHelperV2;

import java.util.List;


//import com.example.rxjava.Function;
//import com.example.rxjava.observable.Observable;
//import com.example.rxjava.observable.ObservableOnSubscribe;
//import com.example.rxjava.obsever.Observer;


/**
 * @author wangyasheng
 * @date 2021-03-02
 * @describe:
 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] PERMISSIONS_MEETING = {
                Manifest.permission.RECORD_AUDIO,
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMISSIONS_MEETING, 0x100);
        }
        AudioHelper.getInstance().init(this);
//        CameraHelperV2.getInstance().init(this);
        UsbDevicesHelper.getInstance().init(this);
    }

    public void createRecord(View view) {
        CameraHelperV2.getInstance().getAvailableCameraDevice();
    }

    public void startRecord(View view) {

    }

    public void stopRecord(View view) {

    }

    public void checkMic(View view) {
        AudioHelper.getInstance().checkLocalMicEnable(new AudioHelper.ICheckLocalMicEnableCallback() {
            @Override
            public void onLocalMicEnable(boolean enable, int code) {
                Log.d("AudioRecorder","checkMic result: enable = " + enable + ",code = " + code);
            }
        });
    }

    public void getAllUsbDevices(View view) {
        List<UsbDevice> usbDevices = UsbDevicesHelper.getInstance().getUsbDevices(UsbDevicesHelper.DeviceType.TYPE_ALL);
        if (usbDevices != null){
            Log.d(TAG,"[getAllUsbDevices] size:" + usbDevices.size());
            for (int i = 0; i < usbDevices.size(); i++){
                UsbDevice device = usbDevices.get(i);
                Log.d(TAG, "[getAllUsbDevices] all : deviceName = " + device.getDeviceName() + ","
                        + " deviceClass = " +device.getDeviceClass() + ","
                        + " deviceId = " +device.getDeviceId() + ","
                        + " deviceSubclass = " +device.getDeviceSubclass());
            }
        }
    }

    public void getUsbCamera(View view) {
        List<UsbDevice> usbDevices = UsbDevicesHelper.getInstance().getUsbDevices(UsbDevicesHelper.DeviceType.TYPE_CAMERA);
        if (usbDevices != null){
            Log.d(TAG,"[getUsbCamera] size:" + usbDevices.size());
            for (int i = 0; i < usbDevices.size(); i++){
                UsbDevice device = usbDevices.get(i);
                Log.d(TAG, "[getUsbCamera] camera : deviceName = " + device.getDeviceName() + ","
                        + " deviceClass = " +device.getDeviceClass() + ","
                        + " deviceId = " +device.getDeviceId() + ","
                        + " deviceSubclass = " +device.getDeviceSubclass());
            }
        }
    }

    public void getUsbAudio(View view) {
        List<UsbDevice> usbDevices = UsbDevicesHelper.getInstance().getUsbDevices(UsbDevicesHelper.DeviceType.TYPE_AUDIO);
        if (usbDevices != null){
            Log.d(TAG,"[getUsbAudio] size:" + usbDevices.size());
            for (int i = 0; i < usbDevices.size(); i++){
                UsbDevice device = usbDevices.get(i);
                Log.d(TAG, "[getUsbAudio] audio : deviceName = " + device.getDeviceName() + ","
                        + " deviceClass = " +device.getDeviceClass() + ","
                        + " deviceId = " +device.getDeviceId() + ","
                        + " deviceSubclass = " +device.getDeviceSubclass());
            }
        }
    }
}
