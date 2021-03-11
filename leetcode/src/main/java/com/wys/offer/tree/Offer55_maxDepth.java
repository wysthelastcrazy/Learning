package com.wys.offer.tree;

import com.wys.offer.linkedList.node.ListNode;
import com.wys.offer.tree.node.TreeNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author wangyasheng
 * @date 2021-03-11
 * @describe:剑指 Offer 55 - I. 二叉树的深度
 * 输入一棵二叉树的根节点，求该树的深度。从根节点到叶节点依次经过的节点（含根、叶节点）形成树的一条路径，最长路径的长度为树的深度。
 *
 * 例如：
 *
 * 给定二叉树 [3,9,20,null,null,15,7]，
 *
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 * 返回它的最大深度 3 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/er-cha-shu-de-shen-du-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Offer55_maxDepth {
    /**
     * 后序遍历（DFS）
     *
     * 时间复杂度 O(N)O(N) ： NN 为树的节点数量，计算树的深度需要遍历所有节点。
     * 空间复杂度 O(N)O(N) ： 最差情况下（当树退化为链表时），递归深度可达到 NN
     *
     * @param root
     * @return
     */
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return Math.max(maxDepth(root.left),maxDepth(root.right)) + 1;
    }

    /**
     *层序遍历（BFS）
     *
     * 时间复杂度 O(N)O(N) ： NN 为树的节点数量，计算树的深度需要遍历所有节点。
     * 空间复杂度 O(N)O(N) ： 最差情况下（当树平衡时），队列 queue 同时存储 N/2N/2 个节点。
     *
     * @param root
     * @return
     */
    public int maxDepth2(TreeNode root){
        if (root == null) return 0;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int res = 0;
        while (!queue.isEmpty()){
            int size = queue.size();
            for (int i = 0; i< size; i++){
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null){
                    queue.add(node.right);
                }
            }
            res++;
        }
        return res;
    }
}
