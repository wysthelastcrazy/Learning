package com.wys.learning.customaudio;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;


public class AudioRecordService extends Service {
    private static final String TAG = "AudioRecordService";

    public static final int DEFAULT_SAMPLE_RATE = 16000;
    public static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;
    /**1 corresponds to AudioFormat.CHANNEL_IN_MONO;
     * 2 corresponds to AudioFormat.CHANNEL_IN_STEREO*/
    public static final int DEFAULT_CHANNEL_COUNT = 2;

    private AudioRecordThread mRecordThread;

    private volatile boolean stopped;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        startRecording();
        return super.onStartCommand(intent, flags, startId);
    }
    private void startForeground(){

    }
    private void startRecording(){
        Log.i(TAG,"startRecording++++++++");
        mRecordThread = new AudioRecordThread();
        mRecordThread.start();
    }
    private void stopRecording(){
        Log.i(TAG,"stopRecording++++++++");
        stopped = true;
    }

    @Override
    public void onDestroy() {
        stopRecording();
        super.onDestroy();
    }

    public class AudioRecordThread extends Thread{

        private AudioRecord mAudioRecord;
        private byte[] mBuffer;

        AudioRecordThread(){
            int bufferSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLE_RATE,DEFAULT_CHANNEL_CONFIG,
                    AudioFormat.ENCODING_PCM_16BIT);
            mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,DEFAULT_SAMPLE_RATE,DEFAULT_CHANNEL_CONFIG,
                    AudioFormat.ENCODING_PCM_16BIT,bufferSize);
            mBuffer = new byte[bufferSize];
        }

        @Override
        public void run() {
            try {
                mAudioRecord.startRecording();
                while (!stopped){
                    int result = mAudioRecord.read(mBuffer,0,mBuffer.length);
                    if (result >= 0){
//                        VoipSdk.getInstance().getEngine().sendCustomAudioData(mBuffer, System.currentTimeMillis());
                    }else{
                        logRecordError(result);
                    }
                }
                release();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void logRecordError(int error){
            String message = "";
            switch (error)
            {
                case AudioRecord.ERROR:
                    message = "generic operation failure";
                    break;
                case AudioRecord.ERROR_BAD_VALUE:
                    message = "failure due to the use of an invalid value";
                    break;
                case AudioRecord.ERROR_DEAD_OBJECT:
                    message = "object is no longer valid and needs to be recreated";
                    break;
                case AudioRecord.ERROR_INVALID_OPERATION:
                    message = "failure due to the improper use of method";
                    break;
            }
            Log.e(TAG, message);
        }
        private void release(){
            Log.i(TAG,"release++++++++");
            if (mAudioRecord != null){
                mAudioRecord.stop();
                mBuffer = null;
            }
        }
    }
}
