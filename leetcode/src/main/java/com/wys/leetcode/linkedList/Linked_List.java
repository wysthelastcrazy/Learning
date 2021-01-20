package com.wys.leetcode.linkedList;

/**
 * Created by yas on 2019/3/18
 * Describe:链表
 */
public class Linked_List {

    /**
     * 就地反转
     * @param head
     * @return
     */
    public static ListNode reverseList1(ListNode head){
        if (head==null)
            return head;

        ListNode dummy=new ListNode(-1);
        dummy.next=head;
        ListNode prev=dummy.next;
        ListNode pCur=prev.next;

        while (pCur!=null){
            prev.next=pCur.next;
            pCur.next=dummy.next;
            dummy.next=pCur;
            pCur=prev.next;
        }
        return dummy.next;
    }

    /**
     * 反转单链表，新建链表从头插入
     * @param head
     * @return
     */
    public static ListNode  reverseList2(ListNode head){
        if (head==null)
            return head;
        ListNode dummy=new ListNode(-1);
        while (head!=null){
            ListNode next=head.next;
            head.next=dummy.next;
            dummy.next=head;
            head=next;
        }
        return dummy.next;
    }
    /**
     * 单链表节点
     */
    public static class ListNode{
        int val;
        public ListNode next;
        public ListNode(int val){
            this.val=val;
            next=null;
        }

        public int getVal() {
            return val;
        }
    }
}
