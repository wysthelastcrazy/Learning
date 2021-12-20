package com.wys.learning.utils;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.media.Image;

import com.example.commonlib.log.LogUtil;

import java.nio.ByteBuffer;

public class ImageUtils {
    private static final String TAG = "ImageUtils";
    public static final int COLOR_FormatI420 = 1;
    public static final int COLOR_FormatNV21 = 2;

    /**
     * 在image中获取yuv数据
     * @param image
     * @param colorFormat
     * @return
     */
    public static byte[] getByteFromImageAsType(Image image, int colorFormat){

        //获取源数据，如果是YUV格式的数据，planes.length = 3
        //planes[i] 里面的实际数据可能存在byte[].length <= capacity(缓冲区总大小)
        final Image.Plane[] planes = image.getPlanes();

        //数据有效宽高，一般的，图片width < rowStride,这也是导致byte[].length < capacity的原因
        //所以我们只取width部分
        int width = image.getWidth();
        int height = image.getHeight();
        //此处用来装填最终的YUV数据，需要1.5倍的图片大小，因为Y:U:V为4：1：1
        byte[] yuvBytes = new byte[width * height * 3 / 2];

        //临时存储yuv数据
        byte[] yBytes = new byte[width * height];
        byte[] uBytes = new byte[width * height / 4];
        byte[] vBytes = new byte[width * height / 4];

        int yIndex = 0;
        int uIndex = 0;
        int vIndex = 0;

        int pixelsStride, rowStride;

        for (int i = 0; i < planes.length; i++){
            pixelsStride = planes[i].getPixelStride();
            rowStride = planes[i].getRowStride();

            ByteBuffer buffer = planes[i].getBuffer();
            byte[] bytes = new byte[buffer.capacity()];
            buffer.get(bytes);

            int srcIndex = 0;
            //如果pixelsStride == 2，一般的Y的buffer长度 = width * height ，UV分量的长度 = width * height / 2 -1
            //源数据的索引，y的数据是byte中连续的，u的数据是v向左移一位生成的，两者都是偶数位为有效数据
            if (i == 0){
                //Y分量获取
                for (int h = 0; h < height; h++){
                   for (int w = 0; w < width; w++){
                       yBytes[yIndex++] = bytes[srcIndex];
                       srcIndex ++;
                   }
                }
            }else if (i == 1){
                //U分量获取
                for (int h = 0; h < height/2; h++){
                    for (int w = 0; w < width/2; w++){
                        uBytes[uIndex++] = bytes[srcIndex];
                        srcIndex += pixelsStride;
                    }
                }
            }else if (i == 2){
                //V分量获取
                for (int h = 0; h < height/2; h++){
                    for (int w = 0; w < width/2; w++){
                        vBytes[vIndex++] = bytes[srcIndex];
                        srcIndex += pixelsStride;
                    }
                }
            }
        }
        //根据要求的结果类型进行填充
        switch (colorFormat){
            case COLOR_FormatI420:
                System.arraycopy(yBytes,0,yuvBytes,0,yBytes.length);
                System.arraycopy(uBytes,0,yuvBytes,yBytes.length,uBytes.length);
                System.arraycopy(vBytes,0,yuvBytes,yBytes.length + uBytes.length, vBytes.length);
                break;
            case COLOR_FormatNV21:
                System.arraycopy(yBytes,0,yuvBytes,0,yBytes.length);
                int index1 = 0;
                for (int i = yBytes.length; i < yuvBytes.length; i += 2){
                    yuvBytes[i] = vBytes[index1];
                    yuvBytes[i+1] = uBytes[index1];
                    index1++;
                }
                break;
        }
        return yuvBytes;
    }

    public static byte[] rotateYUV420Degree270(byte[] data, int imageWidth,
                                               int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        int nWidth = 0, nHeight = 0;
        int wh = 0;
        int uvHeight = 0;
        if (imageWidth != nWidth || imageHeight != nHeight) {
            nWidth = imageWidth;
            nHeight = imageHeight;
            wh = imageWidth * imageHeight;
            uvHeight = imageHeight >> 1;// uvHeight = height / 2
        }
        // ??Y
        int k = 0;
        for (int i = 0; i < imageWidth; i++) {
            int nPos = 0;
            for (int j = 0; j < imageHeight; j++) {
                yuv[k] = data[nPos + i];
                k++;
                nPos += imageWidth;
            }
        }
        for (int i = 0; i < imageWidth; i += 2) {
            int nPos = wh;
            for (int j = 0; j < uvHeight; j++) {
                yuv[k] = data[nPos + i];
                yuv[k + 1] = data[nPos + i + 1];
                k += 2;
                nPos += imageWidth;
            }
        }
        return rotateYUV420Degree180(yuv, imageWidth, imageHeight);
    }

    public static byte[] rotateYUV420Degree180(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        int i = 0;
        int count = 0;
        for (i = imageWidth * imageHeight - 1; i >= 0; i--) {
            yuv[count] = data[i];
            count++;
        }
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (i = imageWidth * imageHeight * 3 / 2 - 1; i >= imageWidth
                * imageHeight; i -= 2) {
            yuv[count++] = data[i - 1];
            yuv[count++] = data[i];
        }
        return yuv;
    }

    public static byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight)
    {
        byte [] yuv = new byte[imageWidth*imageHeight*3/2];
        // Rotate the Y luma
        int i = 0;
        for(int x = 0;x < imageWidth;x++)
        {
            for(int y = imageHeight-1;y >= 0;y--)
            {
                yuv[i] = data[y*imageWidth+x];
                i++;
            }
        }
        // Rotate the U and V color components
        i = imageWidth*imageHeight*3/2-1;
        for(int x = imageWidth-1;x > 0;x=x-2)
        {
            for(int y = 0;y < imageHeight/2;y++)
            {
                yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+x];
                i--;
                yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+(x-1)];
                i--;
            }
        }
        return yuv;
    }

    public static byte[] getDataFromImage(Image image, int colorFormat){

        Rect crop = image.getCropRect();
        int format = image.getFormat();

        int width = crop.width();
        int height = crop.height();

        Image.Plane[] planes = image.getPlanes();

        byte[] data = new byte[width * height * ImageFormat.getBitsPerPixel(format) / 8];
        byte[] rowData = new byte[planes[0].getRowStride()];

        LogUtil.INSTANCE.v(TAG, "get data from " + planes.length + " planes");

        //将每个分量数据写入byte[]中时的偏移量
        int channelOffset = 0;
        //写入数据的步长
        int outputStride = 1;
        for (int i = 0; i < planes.length; i++) {
            switch (i) {
                case 0:
                    channelOffset = 0;
                    outputStride = 1;
                    break;
                case 1:
                    if (colorFormat == COLOR_FormatI420) {
                        channelOffset = width * height;
                        outputStride = 1;
                    } else if (colorFormat == COLOR_FormatNV21) {
                        channelOffset = width * height + 1;
                        outputStride = 2;
                    }
                    break;
                case 2:
                    if (colorFormat == COLOR_FormatI420) {
                        channelOffset = (int) (width * height * 1.25);
                        outputStride = 1;
                    } else if (colorFormat == COLOR_FormatNV21) {
                        channelOffset = width * height;
                        outputStride = 2;
                    }
                    break;
            }
            ByteBuffer buffer = planes[i].getBuffer();
            int rowStride = planes[i].getRowStride();
            int pixelStride = planes[i].getPixelStride();

            LogUtil.INSTANCE.v(TAG, "pixelStride " + pixelStride);
            LogUtil.INSTANCE.v(TAG, "rowStride " + rowStride);
            LogUtil.INSTANCE.v(TAG, "width " + width);
            LogUtil.INSTANCE.v(TAG, "height " + height);
            LogUtil.INSTANCE.v(TAG, "buffer size " + buffer.remaining());


            int shift = (i == 0) ? 0 : 1;
            int w = width >> shift;
            int h = height >> shift;
            buffer.position(rowStride * (crop.top >> shift) + pixelStride * (crop.left >> shift));
            for (int row = 0; row < h; row++) {
                int length;
                if (pixelStride == 1 && outputStride == 1) {
                    length = w;
                    buffer.get(data, channelOffset, length);
                    channelOffset += length;
                } else {
                    length = (w - 1) * pixelStride + 1;
                    buffer.get(rowData, 0, length);
                    for (int col = 0; col < w; col++) {
                        data[channelOffset] = rowData[col * pixelStride];
                        channelOffset += outputStride;
                    }
                }
                if (row < h - 1) {
                    buffer.position(buffer.position() + rowStride - length);
                }
            }
          LogUtil.INSTANCE.v(TAG, "Finished reading data from plane " + i);
        }
        return data;

    }
}
