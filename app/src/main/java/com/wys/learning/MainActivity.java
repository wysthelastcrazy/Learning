package com.wys.learning;

import android.os.Bundle;
import android.os.Handler;
import android.os.MessageQueue;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


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
        Handler handler = new Handler();
        String s = "30";
        Log.d("",s.compareTo("3")+"");

        //{5,7,9,10,1,3},查找给出value对应的index



    }
    public int find(int[] arr, int target){

        int startIndex = 0;
        int endIndex = arr.length - 1;
        int sp = endIndex;
        //1、先查找分割位置
        while(arr[startIndex] > arr[sp]){
            sp --;
        }
        //分成两个有序数组 arr[startIndex] - arr[sp]和
        //arr[sp + 1] - arr[endIndex]

        if(arr[startIndex] > target){
            //在arr[sp + 1] - arr[endIndex]范围查找
            return find(arr,target,sp+1, endIndex);
        }else {
            //在arr[startIndex] - arr[sp]范围查找
            return find(arr,target,startIndex,sp);
        }
    }

    /**
     * 在指定范围内进行二分查找
     * @param arr
     * @param target
     * @param startIndex
     * @param endIndex
     * @return
     */
    private int find(int[] arr,int target,int startIndex,int endIndex){
        while (startIndex < endIndex){
            int mid = (startIndex + endIndex)/2;
            if (arr[mid] == target){
                return mid;
            }else if(arr[mid] > target){
                endIndex = mid - 1;
            }else {
                startIndex = mid + 1;
            }
        }
        return  -1;
    }

    /**
     * 使用HashMap
     * @param arr
     * @return
     */
    public int notRep(int[] arr){
        HashMap<Integer,Integer> map = new HashMap<>();
        for(int i = 0; i < arr.length; i++){
            if (map.containsKey(arr[i])){
                map.remove(arr[i]);
            }else{
                map.put(arr[i],1);
            }
        }
       
       return -1;
    }










    // 如 123456789123456789 * 56789123456789
// 已经按照低位到高位转成整数数组
// a = {9, 8, 7, 6, 5, 4, 3, 2, 1, 9, 8, 7, 6, 5, 4, 3, 2, 1}
// b = {9, 8, 7, 6, 5, 4, 3, 2, 1, 9, 8, 7, 6, 5}
    int[] multiply(int[] a, int[] b) {
        int i = (a[a.length - 1] * b[b.length - 1]) > 10 ? 1 : 0;
        int size = (a.length - 1) + (b.length - 1) + i + 1;
        int[] arr = new int[size];
        for(int j = 0; j < a.length; j++ ){
            for(int k = 0; k < b.length; k++){
                int sum = arr[j + k] + (a[j] * b[k]);
                arr[j + k] =  sum%10;

                arr[j + k +1] = arr[j + k +1] + sum/10 ;
            }
        }
        return arr;
    }
}
