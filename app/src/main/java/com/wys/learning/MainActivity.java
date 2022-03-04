package com.wys.learning;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.commonlib.log.LogUtil;
import com.wys.learning.audio.AudioHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


//import com.example.rxjava.Function;
//import com.example.rxjava.observable.Observable;
//import com.example.rxjava.observable.ObservableOnSubscribe;
//import com.example.rxjava.obsever.Observer;
import android.renderscript.Type.Builder;

/**
 * @author wangyasheng
 * @date 2021-03-02
 * @describe:
 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private CameraReceiver receiver;
    private FrameLayout flContainer;
    private TextureView textureView;
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
        receiver = new CameraReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(ACTION_USB_DEVICE_DETACHED);

        registerReceiver(receiver,filter);
        LogUtil.INSTANCE.i(TAG,"onCreate++++++++++++++++++++++++");
        LogUtil.INSTANCE.i(TAG,"date: " + stampToTime(1629699429990L));

        flContainer = findViewById(R.id.fl_container);
        textureView = new TextureView(this);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                LogUtil.INSTANCE.i(TAG,"onSurfaceTextureAvailable++++++++++++++++++++++++");
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
                LogUtil.INSTANCE.i(TAG,"onSurfaceTextureSizeChanged++++++++++++++++++++++++");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                LogUtil.INSTANCE.i(TAG,"onSurfaceTextureDestroyed++++++++++++++++++++++++");
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                LogUtil.INSTANCE.i(TAG,"onSurfaceTextureUpdated++++++++++++++++++++++++");
            }
        });

    }

    private void gotoActivity2(){
        Intent intent = new Intent(this,MainActivity2.class);
        startActivity(intent);
    }
    public String stampToTime(long stamp) {
        String sd = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        sd = sdf.format(new Date(stamp)); // 时间戳转换日期

        return sd;

    }
    public void checkMic(View view) {
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AudioHelper.getInstance().checkLocalMicEnable(new AudioHelper.ICheckLocalMicEnableCallback() {
            @Override
            public void onLocalMicEnable(boolean enable, int code) {
                LogUtil.INSTANCE.d("AudioRecorder","checkMic result: enable = " + enable + ",code = " + code);
            }
        });
    }

    public void getAllUsbDevices(View view) {
        gotoActivity2();
//        List<UsbDevice> usbDevices = UsbDevicesHelper.getInstance().getUsbDevices(UsbDevicesHelper.DeviceType.TYPE_ALL);
//        if (usbDevices != null){
//            LogUtil.INSTANCE.d(TAG,"[getAllUsbDevices] size:" + usbDevices.size());
//            for (int i = 0; i < usbDevices.size(); i++){
//                UsbDevice device = usbDevices.get(i);
//                LogUtil.INSTANCE.d(TAG,"[getAllUsbDevices] device"+ i +": "+ device);
//                int count = device.getInterfaceCount();
//                LogUtil.INSTANCE.d(TAG,"[getAllUsbDevices] count:" + count);
//            }
//        }
    }

    public void getUsbCamera(View view) {
        List<UsbDevice> usbDevices = UsbDevicesHelper.getInstance().getUsbDevices(UsbDevicesHelper.DeviceType.TYPE_CAMERA);
        if (usbDevices != null){
            LogUtil.INSTANCE.d(TAG,"[getUsbCamera] size:" + usbDevices.size());
            for (int i = 0; i < usbDevices.size(); i++){
                UsbDevice device = usbDevices.get(i);
                LogUtil.INSTANCE.d(TAG, "[getUsbCamera] camera : deviceName = " + device.getDeviceName() + ","
                        + " deviceClass = " +device.getDeviceClass() + ","
                        + " deviceId = " +device.getDeviceId() + ","
                        + " deviceSubclass = " +device.getDeviceSubclass());
            }
        }
    }

    public void getUsbAudio(View view) {
        List<UsbDevice> usbDevices = UsbDevicesHelper.getInstance().getUsbDevices(UsbDevicesHelper.DeviceType.TYPE_AUDIO);
        if (usbDevices != null){
            LogUtil.INSTANCE.d(TAG,"[getUsbAudio] size:" + usbDevices.size());
            for (int i = 0; i < usbDevices.size(); i++){
                UsbDevice device = usbDevices.get(i);
                LogUtil.INSTANCE.d(TAG, "[getUsbAudio] audio : deviceName = " + device.getDeviceName() + ","
                        + " deviceClass = " +device.getDeviceClass() + ","
                        + " deviceId = " +device.getDeviceId() + ","
                        + " deviceSubclass = " +device.getDeviceSubclass());
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public Bitmap yuvToBitmap(byte[] data, int width, int height) {
        int frameSize = width * height;
        int[] rgba = new int[frameSize];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int y = (0xff & ((int) data[i * width + j]));
                int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 0]));
                int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
                y = y < 16 ? 16 : y;
                int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));
                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);
                rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
            }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.setPixels(rgba, 0 , width, 0, 0, width, height);
        return bmp;
    }


    public byte[] I420ToNV21(byte[] input, int width, int height) {
        byte[] output = new byte[4147200];
        int frameSize = width * height;
        int qFrameSize = frameSize / 4;
        int tempFrameSize = frameSize * 5 / 4;
        System.arraycopy(input, 0, output, 0, frameSize);

        for(int i = 0; i < qFrameSize; ++i) {
            output[frameSize + i * 2] = input[tempFrameSize + i];
            output[frameSize + i * 2 + 1] = input[frameSize + i];
        }

        return output;
    }

    public Bitmap NV21ToBitmap(Context context, byte[] nv21, int width, int height) {
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
        Builder yuvType = (new Builder(rs, Element.U8(rs))).setX(nv21.length);
        Allocation in = Allocation.createTyped(rs, yuvType.create(), 1);
        Builder rgbaType = (new Builder(rs, Element.RGBA_8888(rs))).setX(width).setY(height);
        Allocation out = Allocation.createTyped(rs, rgbaType.create(), 1);
        in.copyFrom(nv21);
        yuvToRgbIntrinsic.setInput(in);
        yuvToRgbIntrinsic.forEach(out);
        Bitmap bmpout = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        out.copyTo(bmpout);
        return bmpout;
    }

    private static final String ACTION_USB_DEVICE_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private static final String ACTION_USB_DEVICE_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";

    public void onVolumeUp(View view) {
        AudioHelper.getInstance().upSystemVolume();
    }

    public void onVolumeDown(View view) {
        AudioHelper.getInstance().downSystemVolume();
    }

    public void onMusicVolumeUp(View view) {
        AudioHelper.getInstance().upMusicVolume();
    }

    public void onMusicVolumeDown(View view) {
        AudioHelper.getInstance().downMusicVolume();
    }

    public void addTextureView(View view) {
        flContainer.removeAllViews();
        flContainer.addView(textureView,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void removeTextureView(View view) {
        flContainer.removeAllViews();
    }

    public void gone(View view) {
        textureView.setVisibility(View.GONE);
    }

    public void visible(View view) {
        textureView.setVisibility(View.VISIBLE);
    }

    private class CameraReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null){
                return;
            }
            String action = intent.getAction();
            if (ACTION_USB_DEVICE_ATTACHED == action) {
                //usb 设备插入
                Toast.makeText(MainActivity.this,"usb attached",Toast.LENGTH_LONG).show();
                Log.d(TAG, "[onReceive] usb attached++++++++++++++++++++++++++++");
            } else if (ACTION_USB_DEVICE_DETACHED == action) {
                //usb 设备拔出
                Toast.makeText(MainActivity.this,"usb detached",Toast.LENGTH_LONG).show();
                Log.d(TAG, "[onReceive] usb detached++++++++++++++++++++++++++++");
            }
        }
    }


    /**
     * yuv转换为bitmap，用于TextureView size为0获取不到bitmap时，直接从流获取截图
     */
//    private fun yuvToBitmap(data: ByteArray, width: Int, height: Int): Bitmap? {
//        LogUtil.d(LOG_TAG, "yuvToBitmap width = $width ,height = $height, data.size = ${data.size}")
//        //YUV_420P I420数据格式：YUV分量分别存放，先是w*h长度的Y，后面跟w*h*0.25长度的U，
//        // 最后是w*h*0.25长度的V，总长度为 w*h*1.5
//        val output = ByteArray(data.size)
//        val frameSize = width * height
//        val qFrameSize = frameSize / 4
//        val tempFrameSize = frameSize * 5 / 4
//        System.arraycopy(data, 0, output, 0, frameSize)
//        //将I420转换为NV21
//        for (i in 0 until qFrameSize) {
//            output[frameSize + i * 2] = data[tempFrameSize + i]
//            output[frameSize + i * 2 + 1] = data[frameSize + i]
//        }
//        //使用RenderScript将数据转换为bitmap
//        val rs = RenderScript.create(Sdk.getContext())
//        val yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs))
//        var yuvType = Type.Builder(rs, Element.U8(rs)).setX(output.size)
//        val dataIn = Allocation.createTyped(rs, yuvType.create(), 1)
//        val rgbaType = Type.Builder(rs, Element.RGBA_8888(rs))
//                .setX(width).setY(height)
//        val dateOut = Allocation.createTyped(rs, rgbaType.create(), 1)
//        dataIn.copyFrom(output)
//        yuvToRgbIntrinsic.setInput(dataIn)
//        yuvToRgbIntrinsic.forEach(dateOut)
//        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//        dateOut.copyTo(bitmap)
//        return bitmap
//    }
}
