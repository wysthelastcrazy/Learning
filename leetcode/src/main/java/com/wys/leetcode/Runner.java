package com.wys.leetcode;


import com.wys.leetcode.linkedList.Linked_List;

public class Runner {
    public static void main(String[] args) {
//        Note.init();
//        Note.initQueue();
        Linked_List.ListNode head = new Linked_List.ListNode(1);
        Linked_List.ListNode node1 = new Linked_List.ListNode(2);
        Linked_List.ListNode node2 = new Linked_List.ListNode(3);
        Linked_List.ListNode node3 = new Linked_List.ListNode(4);
        Linked_List.ListNode node4 = new Linked_List.ListNode(5);


        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;

        Linked_List.ListNode start = head;
        while (start!=null){
            System.out.print(start.getVal()+",");
            start = start.next;
        }
        System.out.println();
        Linked_List.delNode2(head,2);

        start = head;
        while (start!=null){
            System.out.print(start.getVal()+",");
            start = start.next;
        }
    }
}
