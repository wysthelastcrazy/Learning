package com.example.avdemo.part04;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.text.TextUtils;

import com.example.commonlib.log.LogUtil;

import java.io.IOException;

public class MediaHelper {
    private static final String TAG = "MediaHelper";
    public void init(String path){
        //1、构造MediaExtractor
        MediaExtractor mediaExtractor = new MediaExtractor();

        try {
            //2、设置数据源
            mediaExtractor.setDataSource(path);
            //3、获取轨道数
            int trackCount = mediaExtractor.getTrackCount();
            LogUtil.INSTANCE.i(TAG,"init trackCount = " + trackCount);

            //4、遍历轨道，查看音频轨或视频轨道信息
            for (int i = 0; i < trackCount; i++){
                //获取某一轨道的媒体格式
                MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
                String keyMime = trackFormat.getString(MediaFormat.KEY_MIME);
                LogUtil.INSTANCE.i(TAG,"init keyMime = " + keyMime);

                if (TextUtils.isEmpty(keyMime)){
                    continue;
                }
                //通过mime信息识别音频轨或视频轨道
                if (keyMime.startsWith("video/")){
                    //打印视频的宽高
                    LogUtil.INSTANCE.i(TAG,"videoWidth = " + trackFormat.getInteger(MediaFormat.KEY_WIDTH) + "," +
                            "videoHeight = " + trackFormat.getInteger(MediaFormat.KEY_HEIGHT));
                }else if (keyMime.startsWith("audio/")){
                    //打印音频的通道数以及比特率
                    LogUtil.INSTANCE.i(TAG," channelCount = " + trackFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT) +
                            ",bitRate = " + trackFormat.getInteger(MediaFormat.KEY_BIT_RATE));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            mediaExtractor.release();
        }
    }
}
