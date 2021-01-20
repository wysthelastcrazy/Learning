package com.wys.leetcode.other;

import java.util.*;

/**
 * Created by yas on 2019/3/14
 * Describe:排序
 */
public class Rudiments {

    /**
     * 冒泡排序
     * @param arr
     * @return
     */
    public static int[] bubbleSort(int[] arr){
        //外层循环控制冒泡次数
        for (int i=0;i<arr.length-1;i++){
            //是否有数据交换标识位，没有数据交换则提前退出循环
            boolean flag=false;
            //内层循环控制每趟排序多少次
            for (int j=0;j<arr.length-1-i;j++){
                if (arr[j]>arr[j+1]){
                    int temp=arr[j];
                    arr[j]=arr[j+1];
                    arr[j+1]=temp;
                    flag=true;
                }

            }
            if (!flag) break;
        }
        return arr;
    }
    /**
     * 计算阶乘末尾0的个数
     * https://www.cnblogs.com/kuliuheng/p/4102917.html
     * @param n
     * @return
     */
    public long trailingZeros(long n){
        if (n<5){
            return 0;
        }else{
            return (n/5+trailingZeros(n/5));
        }
    }

    /**
     * 合并有序数组
     * @param a
     * @param b
     * @return
     */
    public int[] mergeSortedArray(int [] a,int[] b){
        if (a==null)return b;
        if (b==null)return a;

        HashMap map=new HashMap();
        map.put("","");
        int[] arr=new int[a.length+b.length];
        int i=0,j=0,k=0;
        while (k<=arr.length-1){
            if (i<=a.length-1&&j<=b.length-1){
                if (a[i]<=b[j]){
                    arr[k]=a[i];
                    i++;
                }else {
                    arr[k]=b[j];
                    j++;
                }
            }else if (j<=b.length-1){
                arr[k]=b[j];
                j++;
            }else if (i<=a.length-1){
                arr[k]=a[i];
                i++;
            }
            k++;
        }
        return arr;
    }

}
