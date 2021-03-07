package com.wys.offer.linkedList;


import com.wys.offer.linkedList.node.ListNode;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author wangyasheng
 * @date 2021-03-07
 * @describe:剑指 Offer 06. 从尾到头打印链表
 */
public class Offer06_reversePrint {

    /**
     * 方法一、递归
     *
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
     * @param head
     * @return
     */
    public int[] reversePrint1(ListNode head) {
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        recur(head,tmp);
        int[] res = new int[tmp.size()];
        for(int i = 0; i < res.length; i++)
            res[i] = tmp.get(i);
        return res;
    }
    private void recur(ListNode head, ArrayList<Integer> tmp){
        if (head == null) return;
        recur(head.next,tmp);
        tmp.add(head.val);
    }

    /**
     * 方法二、辅助栈
     *
     * 时间复杂度：O(N)出栈和入栈共使用O(N)
     * 空间复杂度：O(N)辅助栈和数组
     * @param head
     * @return
     */
    public int[] reversePrint2(ListNode head) {
        ListNode curNode = head;
        LinkedList<Integer> stack = new LinkedList<>();
        while (curNode != null){
            stack.addLast(head.val);
            curNode = curNode.next;
        }
        int[] res = new int[stack.size()];
        for(int i = 0; i < res.length; i++)
            res[i] = stack.removeLast();
        return res;
    }

    /**
     * 方法三、直接使用数组
     *
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
     *
     * @param head
     * @return
     */
    public int[] reversePrint3(ListNode head) {
        ListNode cur = head;
        int size = 0;
        while (cur!=null){
            size ++;
            cur = cur.next;
        }
        int[] print = new int[size];
        cur = head;
        while (cur!=null){
            print[size - 1] = cur.val;
            size --;
            cur = cur.next;
        }
        return print;
    }

}
