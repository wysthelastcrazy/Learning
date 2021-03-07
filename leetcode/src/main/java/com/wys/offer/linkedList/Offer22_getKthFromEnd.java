package com.wys.offer.linkedList;


import com.wys.offer.linkedList.node.ListNode;

/**
 * @author wangyasheng
 * @date 2021-03-07
 * @describe:剑指 Offer 22. 链表中倒数第k个节点
 *
 * 输入一个链表，输出该链表中倒数第k个节点。为了符合大多数人的习惯，本题从1开始计数，即链表的尾节点是倒数第1个节点。
 *
 * 例如，一个链表有 6 个节点，从头节点开始，它们的值依次是 1、2、3、4、5、6。这个链表的倒数第 3 个节点是值为 4 的节点。
 *
 *  
 *
 * 示例：
 *
 * 给定一个链表: 1->2->3->4->5, 和 k = 2.
 *
 * 返回链表 4->5.
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/lian-biao-zhong-dao-shu-di-kge-jie-dian-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Offer22_getKthFromEnd {

    /**
     * 解题思路：使用双指针
     * 前指针former先向前走k步，结束后前指针和后指针相距k步；
     * 循环中双指针同时向前走，直至前指针走过尾节点时跳出循环；
     * 返回后指针latter即可。
     * @param head
     * @param k
     * @return
     */
    public ListNode getKthFromEnd(ListNode head, int k) {
        ListNode former = head;
        ListNode latter = head;
        for (int i = 0; i < k; i++){
            former = former.next;
        }
        while (former != null){
            former = former.next;
            latter = latter.next;
        }
        return latter;
    }
}
