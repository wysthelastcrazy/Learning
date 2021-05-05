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
     * 删除倒数第k个节点
     * @param head
     * @param k
     */
    public static void delNode(ListNode head,int k){
        int size = 0;
        ListNode startNode = head;
        while (startNode!=null){
            size++;
            startNode = startNode.next;
        }
        startNode = head;
        ListNode pre = startNode;
        int j = 0;
        while ((size - j)>k){
            pre = startNode;
            startNode = startNode.next;
            j ++;
        }
        pre.next = startNode.next;
    }

    /**
     * 删除倒数第k个节点
     * @param head
     * @param k
     */
    public static void delNode2(ListNode head,int k){
        ListNode slow = head,fast = head;
        while (k>0 && fast!=null){
            fast = fast.next;
            k--;
        }
        System.out.println("fast:"+fast.getVal());
        while (fast.next!=null){
            fast = fast.next;
            slow = slow.next;
            System.out.println("........");
        }
//        System.out.println("fast:"+fast.getVal());
        System.out.println("slow:"+slow.getVal());
        slow.next = slow.next.next;
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
