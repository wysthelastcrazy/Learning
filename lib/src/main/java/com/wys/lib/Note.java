package com.wys.lib;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by yas on 2019/3/15
 * Describe:
 */
public class Note {
    /**
     * 在二叉树的非递归遍历中，使用到栈（即先进后出）数据结构；
     * 栈有两种实现方式：一个是用Stack类，一个用LinkedList类实现。
     */
    public static Stack<String> stack;
    public static LinkedList<String> stack1;
    public static LinkedList<String> list;

    public static void init(){

        stack=new Stack<>();
        stack.push("1");
        stack.push("2");
        stack.push("3");
        stack.push("4");

        stack1=new LinkedList<>();
        stack1.push("1");
        stack1.push("2");
        stack1.push("3");
        stack1.push("4");

        list=new LinkedList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        System.out.println(stack);
        System.out.println(stack1);
        System.out.println(list);
    }
    /**
     * 通过以上打印可以发现Stack实现和LinkedList实现的输出是不一样的，顺序相反。
     * Stack实现时，push和pop都是对数组最高下表成员进行操作，从而实现先进后出（LIFO）。
     *但是LinkedList进行push和pop操作时，时通过对链表first节点进行操作得到LIFO效果。
     * 差异主要是因为Stack使用数据实现的，而LinkedList是用链表（双链表）实现的，如果Stack在头部
     *进行push和pop，那么每一次都要对后面的元素进行调整。
     */



    /**=================Queue（队列）用法=============**/

    /**
     * 队列是一种特殊的线性表，它只允许在表的前端进行删除操作，而在后端进行插入操作（即先进先出）。
     * Queue接口与List、Set同一级别，都是继承了Collection接口。LinkedList实现了Deque接口。
     *
     * Queue的实现：https://blog.csdn.net/qq_33524158/article/details/78578370
     * 1、没有实现阻塞阻塞接口的队列
     * 2、实现了阻塞接口的队列
     *
     * Queue使用是要尽量避免Collection的add和remove方法，而是使用offer来添加元素，poll来
     * 获取并移除元素。它们的优点是通过返回值可以判断成功与否，add和remove方法在失败的时候会抛出异常，
     * 如果要使用前端而不移除该元素，使用 element或者peek
     * */
    public static void initQueue(){
        Queue<String> queue=new LinkedList();
        queue.offer("string1");
        queue.offer("string2");
        queue.offer("string3");
        String str=queue.element();
        System.out.println(queue);
        System.out.println(str);
    }











}
