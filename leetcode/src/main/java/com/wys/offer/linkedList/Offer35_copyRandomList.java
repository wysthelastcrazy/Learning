package com.wys.offer.linkedList;

import com.wys.offer.linkedList.node.RandomListNode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyasheng
 * @date 2021-03-07
 * @describe:剑指 Offer 35. 复杂链表的复制
 * 请实现 copyRandomList 函数，复制一个复杂链表。在复杂链表中，
 * 每个节点除了有一个 next 指针指向下一个节点，
 * 还有一个 random 指针指向链表中的任意节点或者 null。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/fu-za-lian-biao-de-fu-zhi-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Offer35_copyRandomList {
    /**
     * 方法一、哈希表
     *
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
     * @param head
     * @return
     */
    public RandomListNode copyRandomList(RandomListNode head) {
        if (head == null)
            return null;
        RandomListNode cur = head;
        Map<RandomListNode,RandomListNode> map = new HashMap<>();
        //复制各节点，并建立"原节点 -> 新节点"的Map映射关系
        while (cur != null){
            map.put(cur,new RandomListNode(cur.val));
            cur = cur.next;
        }
        cur = head;
        //构建新链表的next和random指向
        while (cur != null){
            map.get(cur).next = map.get(cur.next);
            map.get(cur).random = map.get(cur.random);
            cur = cur.next;
        }
        //返回新链表的头节点
        return map.get(head);
    }

    /**
     * 方法二、拼接+拆分
     * @param head
     * @return
     */
    public RandomListNode copyRandomList2(RandomListNode head) {
        if (head == null)
            return null;
        RandomListNode cur = head;
        //1、复制各节点，并构建拼接链表
        while (cur != null){
            RandomListNode tmp = new RandomListNode(cur.val);
            tmp.next = cur.next;
            cur.next = tmp;
            cur = tmp.next;
        }
        //2、构建各新节点的random指向
        cur = head;
        while (cur != null){
            if (cur.random != null){
                cur.next.random = cur.random.next;
            }
            cur = cur.next.next;
        }
        //3、拆分两链表
        cur = head.next;
        RandomListNode pre = head;
        RandomListNode res = head.next;
        while (cur.next != null){
            pre.next = pre.next.next;
            cur.next = cur.next.next;
            pre = pre.next;
            cur = cur.next;
        }
        //单独处理原链表尾节点
        pre.next = null;
        //返回新链表头节点
        return res;
    }
}
