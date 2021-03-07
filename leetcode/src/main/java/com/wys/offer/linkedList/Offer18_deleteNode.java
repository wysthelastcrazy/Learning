package com.wys.offer.linkedList;

import com.wys.offer.linkedList.node.ListNode;

/**
 * @author wangyasheng
 * @date 2021-03-07
 * @describe:剑指 Offer 18. 删除链表的节点
 *
 * 给定单向链表的头指针和一个要删除的节点的值，定义一个函数删除该节点。
 *
 * 返回删除后的链表的头节点。
 */
public class Offer18_deleteNode {
    public ListNode deleteNode(ListNode head, int val){
        if (head == null)
            return null;
        //如果删除的节点为头节点，则直接返回head.next
        if (head.val == val){
            return head.next;
        }
        ListNode pre = head;
        ListNode cur = pre.next;
        while (cur != null){
            if (cur.val == val){
                pre.next = cur.next;
                break;
            }
            pre = cur;
            cur = cur.next;
        }
        return head;
    }
}
