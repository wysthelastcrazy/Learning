package com.wys.lib;


import com.wys.lib.implementor_java.Linked_List;


public class Runner {
    public static void main(String[] args) {
//        Note.init();

        Linked_List.ListNode head=new Linked_List.ListNode(1);
        Linked_List.ListNode node1=new Linked_List.ListNode(2);
        Linked_List.ListNode node2=new Linked_List.ListNode(3);
        Linked_List.ListNode node3=new Linked_List.ListNode(4);
        head.next=node1;
        node1.next=node2;
        node2.next=node3;
        node3.next=null;

        Linked_List.ListNode node= Linked_List.reverseList2(head);
        StringBuilder builder=new StringBuilder();
        while (node!=null){
            builder.append(node.getVal()+"  ");
            node=node.next;
        }
        System.out.println(builder.toString());

    }
}
