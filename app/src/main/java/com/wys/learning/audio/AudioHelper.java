package com.wys.learning.audio;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.MicrophoneInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class AudioHelper {
    private static final String TAG = "AudioHelper";


    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static Context mContext;

    private static class AudioRecorderHolder{
        private static AudioHelper instance = new AudioHelper();
    }

    private AudioHelper(){

    }
    public static AudioHelper getInstance(){
        return AudioRecorderHolder.instance;
    }
    public void init(Context context){
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public int getNumOfMic(){
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int number = 0;
        try {
            List<MicrophoneInfo> microphones = audioManager.getMicrophones();
            if (microphones != null){
                for (int i = 0; i < microphones.size(); i++){
                    /**
                     * location:the location of the microphone
                     * LOCATION_UNKNOWN: A microphone that the location is unknown.
                     * LOCATION_MAINBODY:A microphone that locate on main body of the device.
                     * LOCATION_MAINBODY_MOVABLE: A microphone that locate on a movable main body of the device.
                     * LOCATION_PERIPHERAL:A microphone that locate on a peripheral.
                     */
                    Log.d(TAG,"[getNumOfMic] location:" + microphones.get(i).getLocation());
                }

                number = microphones.size();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return number;
    }

    /**
     * 检测麦克风是否可用
     * @param callback
     */
    public void checkLocalMicEnable(final ICheckLocalMicEnableCallback callback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //判断权限
                int checkResult = mContext.checkSelfPermission(Manifest.permission.RECORD_AUDIO);
                if (checkResult != PackageManager.PERMISSION_GRANTED){
                    //无权限
                    Log.d(TAG,"[startRecord] 无权限+++++++++++");
                    callback.onLocalMicEnable(false,1);
                    return;
                }
                AudioRecord audioRecord = null;
                try{
                    //step1 创建 audioRecord
                    /**
                     * audioSource：采集来源，mic - 麦克风
                     * sampleRateInHz:采样频率
                     * channelConfig：声道 AudioFormat.CHANNEL_IN_MONO - 单声道
                     * audioFormat：编码（返回的数据格式）AudioFormat.ENCODING_PCM_16BIT
                     * bufferSizeInBytes：音频数据写入缓冲区大小
                     */
                    int bufferSizeInBytes = AudioRecord.getMinBufferSize(44100,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_8BIT);
                    audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                            44100,AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_8BIT,bufferSizeInBytes);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        List<MicrophoneInfo> infos = audioRecord.getActiveMicrophones();
                        if (infos !=null ){
                            for (int i = 0; i < infos.size(); i++){
                                Log.d(TAG,"microphoneInfo:   {id:" + infos.get(i).getId() + ",type:" + infos.get(i).getType()
                                        + ",location:" + infos.get(i).getLocation() +", address:" + infos.get(i).getAddress() + "}");
                            }
                        }

                    }
                    //This ensures that the appropriate hardware resources have been acquired.
                    int state = audioRecord.getState();
                    if (state == AudioRecord.STATE_UNINITIALIZED){
                        Log.d(TAG,"[startRecord] audioRecord unInitialized+++++++");
                        callback.onLocalMicEnable(false,2);
                        return;
                    }

                    Log.d(TAG,"[startRecord] audioRecord initialized+++++++");

                    //step2 开始录音500ms
                    byte[] audioData = new byte[bufferSizeInBytes];
                    int readSize = 0;
                    long time = System.currentTimeMillis();
                    LinkedHashSet<Byte> audioSet = new LinkedHashSet<Byte>();
                    audioRecord.startRecording();
                    while ((System.currentTimeMillis() - time) < 10000){
                        readSize += audioRecord.read(audioData, 0, bufferSizeInBytes);
                        Log.d(TAG,"[startRecord] audioRecord readSize:" + readSize);
                        for (int i = 0; i < audioData.length; i++){
                            audioSet.add(audioData[i]);
                        }
                    }
                    Log.d(TAG, "checkLocalMicEnable audioSet size:" + audioSet.size());
                    Iterator<Byte> it = audioSet.iterator();
                    while (it.hasNext()) {
                        Log.d(TAG, "checkLocalMicEnable audioSet item:" + it.next());
                    }
                    if (readSize > 0){
                        //有录音数据
                        callback.onLocalMicEnable(true,0);
                    }else {
                        //无录音数据
                        callback.onLocalMicEnable(false,2);
                    }
                    //step3 结束录音并释放资源
                    audioRecord.release();
                    audioRecord = null;
                }catch (Exception e){
                    e.printStackTrace();
                    callback.onLocalMicEnable(false,3);
                }finally {
                    if (audioRecord != null && audioRecord.getState() == AudioRecord.STATE_INITIALIZED){
                        audioRecord.release();
                        audioRecord = null;
                    }
                }
            }
        });

    }

    public void downMusicVolume(){
//        STREAM_ALARM：手机闹铃的声音
//        STREAM_MUSIC：手机音乐的声音
//        STREAM_DTMF：DTMF音调的声音
//        STREAM_RING：电话铃声的声音
//        STREAM_NOTFICATION：系统提示的声音
//        STREAM_SYSTEM：系统的声音
//        STREAM_VOICE_CALL：语音电话声音
        AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        //打开扬声器
        mAudioManager.setSpeakerphoneOn(true);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);

    }
    public void upMusicVolume(){
        AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        //打开扬声器
        mAudioManager.setSpeakerphoneOn(true);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);

    }

    public void upSystemVolume(){
        AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        //打开扬声器
        mAudioManager.setSpeakerphoneOn(true);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,volume+1,AudioManager.FLAG_SHOW_UI);
    }
    public void downSystemVolume(){
        AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        //打开扬声器
        mAudioManager.setSpeakerphoneOn(true);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,volume-1,AudioManager.FLAG_SHOW_UI);
    }
    public interface ICheckLocalMicEnableCallback{
        void onLocalMicEnable(boolean enable,int code);
    }
}
