package com.wys.learning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;

import java.util.List;

public class StartRemoteAssistanceActivity extends AppCompatActivity {
    private static final String TAG ="AssistanceActivity";

    private SensorManager mSensorManager;
    private OrientationListener mOrientationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_remote_assistance);

       try {
            throw new NullPointerException();
        } catch (Exception e) {
            Log.d(TAG, "Exception e = " + e.toString());
        }

        initSensor();
        if (mOrientationEventListener.canDetectOrientation()){
            Log.d(TAG, "开启方向旋转");
            mOrientationEventListener.enable();
        }else{
            Log.d(TAG, "无法检测方向旋转");
        }
    }

    public void startRemoteAssistance(View view) {
        Intent intent = new Intent("com.xiaomi.mitime.action.RemoteAssistance");
        startActivity(intent);
    }
    public void startGroupCall(View view) {
        Intent intent = new Intent("com.xiaomi.mitv.tvvideocall.action.GROUP_CALL");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrientationEventListener.disable();
//        unRegisterListener();
    }

    private void initSensor(){
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        List<Sensor> allSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sb = new StringBuilder();
        sb.append("当前设备支持传感器数：" + allSensors.size() + "   分别是：\n\n");
        for (Sensor s:allSensors){
            switch (s.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    sb.append("加速度传感器(Accelerometer sensor)" + "\n");
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    sb.append("陀螺仪传感器(Gyroscope sensor)" + "\n");
                    break;
                case Sensor.TYPE_LIGHT:
                    sb.append("光线传感器(Light sensor)" + "\n");
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    sb.append("磁场传感器(Magnetic field sensor)" + "\n");
                    break;
                case Sensor.TYPE_ORIENTATION:
                    sb.append("方向传感器(Orientation sensor)" + "\n");
                    break;
                case Sensor.TYPE_PRESSURE:
                    sb.append("气压传感器(Pressure sensor)" + "\n");
                    break;
                case Sensor.TYPE_PROXIMITY:
                    sb.append("距离传感器(Proximity sensor)" + "\n");
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    sb.append("温度传感器(Temperature sensor)" + "\n");
                    break;
                default:
                    sb.append("其他传感器" + "\n");
                    break;
            }
            sb.append("设备名称：" + s.getName() + "\n 设备版本：" + s.getVersion() + "\n 供应商："
                    + s.getVendor() + "\n\n");
        }
        Log.i(TAG,"sensors：" + sb.toString());
//        mOrientationListener = new OrientationListener();
//        mSensorManager.registerListener(mOrientationListener,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_NORMAL);

    }
    private void unRegisterListener(){
        if (mOrientationListener != null){
            mSensorManager.unregisterListener(mOrientationListener);
            mOrientationListener = null;
        }
    }


    private class OrientationListener implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Log.i("OrientationListener","type: " + sensorEvent.sensor.getType());

            //update set decode params 2020/6/2
            //merge login radio 2021/3/3 11/11
            float[] values = sensorEvent.values;
            float x = values[0];
            float y = values[1];
            Log.i("OrientationListener","x: " + x + ",y: " + y);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    //记录当前的方向
    private int mCurrOrientation = -1;
    /**
     * 手机方向旋转事件监听
     */
    private OrientationEventListener mOrientationEventListener = new OrientationEventListener(MApp.getContext()) {

        @Override
        public void onOrientationChanged(int orientation) {
            Log.i(TAG, "onOrientationChanged orientation = " + orientation);
            //-1：手机平放，检测不到角度
            //0：用户竖直拿着手机
            //90：用户右侧横屏拿着手机
            //180：用户反向竖直拿着手机
            //270：用户左侧横屏拿着手机

            //记录上一次的方向位置
            int lastOrientation = mCurrOrientation;

            if (orientation == ORIENTATION_UNKNOWN) {
                mCurrOrientation = -1;
            }

            if (orientation > 330 || orientation < 30) {
                //0度，用户竖直拿着手机
                mCurrOrientation = 0;
            } else if (orientation > 60 && orientation < 120) {
                //90度，用户右侧横屏拿着手机
                mCurrOrientation = 90;
            } else if (orientation > 150 && orientation < 210) {
                //180度，用户反向竖直拿着手机
                mCurrOrientation = 180;
            } else if (orientation > 240 && orientation < 300) {
                //270度，用户左侧横屏拿着手机
                mCurrOrientation = 270;
            }

            //如果用户关闭了手机的屏幕旋转功能，不在开启代码自动旋转，直接return,产品要求横竖屏切换不受用户设置影响，因此注释掉此处
            //1 手机已开启屏幕旋转功能
            //0 手机未开启屏幕旋转功能
//            try {
//                if (Settings.System.getInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION) == 0){
//                    return ;
//                }
//            } catch (Settings.SettingNotFoundException e) {
//                e.printStackTrace();
//            }
            //当检测到用户手机位置距离上一次的位置发生了变化，开启屏幕旋转
            if (lastOrientation != mCurrOrientation) {
                switch (mCurrOrientation) {
                    case 0:
                        //正向竖屏
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                    case 90:
                        //反向横屏（横屏分正向横屏：靠左手反向横屏  反向横屏：靠右手反向横屏）
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                        break;
                    case 180:
                        //反向竖屏
                        break;
                    case 270:
                        //正向横屏（横屏分正向横屏：靠左手反向横屏  反向横屏：靠右手反向横屏）
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        break;
                }
            }
        }
    };



}