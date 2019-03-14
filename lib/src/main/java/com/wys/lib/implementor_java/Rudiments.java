package com.wys.lib.implementor_java;

import java.util.LinkedList;

/**
 * Created by yas on 2019/3/14
 * Describe:入门
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
     * 获取二叉树最大节点
     * 使用递归
     * @param root
     * @return
     */
    public TreeNode maxNode(TreeNode root){
        if (root==null) return null;
        //第一种实现方式，使用递归
        TreeNode maxLeft=maxNode(root.left);
        TreeNode maxRight=maxNode(root.right);
        if (maxLeft!=null){
           if (root.val<maxLeft.val){
               root=maxLeft;
           }
       }
       if (maxRight!=null){
           if (root.val<maxRight.val){
               root=maxRight;
           }
       }
       return root;

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
     * 二叉树节点
     */
    class TreeNode{
        public int val;
        public TreeNode left,right;
        public TreeNode(int val){
            this.val=val;
            this.left=this.right=null;
        }
    }
}
