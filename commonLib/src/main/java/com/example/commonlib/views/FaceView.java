package com.example.commonlib.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyasheng
 * @date 2023/8/22
 */
public class FaceView extends View {
    private List<Rect> rect;
    private List<Path> paths;
    private Paint paint = new Paint();
    //圆弧半径
    private int radius = 50;
    public FaceView(Context context) {
        this(context,null);
    }

    public FaceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        rect = new ArrayList<>();
        paths = new ArrayList<>();
        paint.setARGB(255,0,187,134);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10.0f);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addRect(Rect rect){
        this.rect.add(rect);
        int width = rect.right - rect.left;
        int height = rect.bottom - rect.top;
        if (width > height){
            rect.top = rect.top - (width - height)/2;
            rect.bottom = rect.bottom + (width - height)/2;
        }else{
            rect.left = rect.left - (height - width)/2;
            rect.right = rect.right + (height - width)/2;
        }

        Path path = new Path();
        //左上角
        path.arcTo(rect.left,rect.top,rect.left+radius*2,rect.top+radius*2,180,90,true);
        //右上角
        path.arcTo(rect.right - radius*2,rect.top,rect.right,rect.top+radius*2,270,90,true);
        //左下角
        path.arcTo(rect.left,rect.bottom - radius*2,rect.left+radius*2,rect.bottom,90,90,true);
        //右下角
        path.arcTo(rect.right - radius*2,rect.bottom - radius*2,rect.right,rect.bottom,0,90,true);
        this.paths.add(path);
    }
    public void clear(){
        rect.clear();
        paths.clear();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < paths.size(); i++){
            canvas.drawPath(paths.get(i),paint);
        }
    }
}
