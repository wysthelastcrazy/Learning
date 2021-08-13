package com.example.androidbase;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author wangyasheng
 * @date 2021-02-20
 * @describe:
 */
public class CustomViewGroup extends ViewGroup {
    public CustomViewGroup(Context context) {
        super(context);
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Handler mHandler = new Handler(){
            @Override
            public void dispatchMessage(@NonNull Message msg) {
                super.dispatchMessage(msg);
            }
        };
        mHandler.sendEmptyMessage(1);

        ArrayList<String> list = new ArrayList<>();
        list.add("ss");

        LinkedList<String> linkedList = new LinkedList();
        linkedList.add("");
        linkedList.remove("");

        Vector<String> v = new Vector<>();
        v.add("dd");
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        copyOnWriteArrayList.remove(1);

        Set set = new HashSet();
        HashMap map = new HashMap<>();
        BlockingQueue queue = new LinkedBlockingQueue();
        View view = new View(this.getContext());
//        view.animate().
    }
}
