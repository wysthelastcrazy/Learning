package com.wys.offer.linkedList;

import com.wys.offer.linkedList.node.ListNode;

/**
 * @author wangyasheng
 * @date 2021-03-07
 * @describe:剑指 Offer 52. 两个链表的第一个公共节点
 *
 * 输入两个链表，找出它们的第一个公共节点。
 *
 */
public class Offer52_getIntersectionNode {

    /**
     * 解题思路：
     * @param headA
     * @param headB
     * @return
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode A = headA;
        ListNode B = headB;

        while (A != B){
            A = A != null ? A.next: headB;
            B = B != null ? B.next:headA;
        }
        return A;
    }
}
