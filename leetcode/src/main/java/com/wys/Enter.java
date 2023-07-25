package com.wys;


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
        String s = "12345";
        f(s,null);
    }
    public static String f(String str1, String str2){

        int i = charToInt(str1.charAt(0));
        System.out.print("i = " + i);
        return null;
    }
    public static int charToInt(char c){
        return c - '0';
    }
}
