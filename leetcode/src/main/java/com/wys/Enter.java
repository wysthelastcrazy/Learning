package com.wys;

import javax.jws.soap.SOAPBinding;
import javax.swing.SortOrder;

/**
 * @author wangyasheng
 * @date 2021-02-16
 * @describe:
 */
public class Enter {
    public static void main(String[] args) {
        System.out.println("this is Enter!!");
        int[] arr = {9,8,7,4,5,6,3,2,1};
        System.out.print("before sort:");
        for (int i = 0; i < arr.length; i++){
            System.out.print(arr[i]+" ");
        }
        System.out.println();
//        Sorts.BubbleSort(arr);
//        Sorts.InsertionSort(arr);
//        Sorts.SelectionSort(arr);
//        Sorts.MergeSort(arr);
        Sorts.QuickSort(arr);
        System.out.print("after sort:");
        for (int i = 0; i < arr.length; i++){
            System.out.print(arr[i]+" ");
        }
    }
}
