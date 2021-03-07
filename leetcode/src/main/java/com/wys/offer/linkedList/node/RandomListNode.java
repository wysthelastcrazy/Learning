package com.wys.offer.linkedList.node;

/**
 * @author wangyasheng
 * @date 2021-03-07
 * @describe:复杂链表节点
 * 每个节点除了有一个next指针指向下一个节点，
 * 还有一个random指针指向链表中的任意节点或null
 */
public class RandomListNode {
    public int val;
    public RandomListNode next;
    public RandomListNode random;
    public RandomListNode(int val){
        this.val = val;
    }
}
