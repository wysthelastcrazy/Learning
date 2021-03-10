package com.wys.offer.tree;

import com.wys.offer.tree.node.TreeNode;

/**
 * @author wangyasheng
 * @date 2021-03-05
 * @describe:剑指 Offer 55 - II. 平衡二叉树
 *
 * 输入一棵二叉树的根节点，判断该树是不是平衡二叉树。如果某二叉树中任意节点的左右子树的深度相差不超过1，那么它就是一棵平衡二叉树。
 */
public class Offer55_isBalanced {

    /**
     * 方法一、后序遍历 + 剪枝
     * 思路是对二叉树做后序遍历，从底至顶返回子树深度，
     * 若判断某子树不是平衡树，则"剪枝"，直接向上返回。
     *
     * 算法流程：
     * recur(root)函数：
     * 1、返回值：
     * 当节点root左/右子树的深度差<= 1。则返回当前子树的深度，即节点root的左/右子树的深度最大值+1；
     * 当节点root左/右子树的深度差>2，则返回-1，代表此子树不是平衡树。
     * 2、终止条件：
     * 当root为空，说明越过叶节点，因此返回高度0；
     * 当左/右子树深度为-1，代表此树的左/右子树不是平衡树，因此剪枝，直接返回-1；
     *
     * isBalanced(root)函数：
     * 1、返回值：
     * 若recur(root) != -1，则说明此树平衡，返回true；否则返回false。
     * @param root
     * @return
     */
    public boolean isBalance(TreeNode root){
        return recur(root) != -1;
    }
    private int recur(TreeNode root){
        if (root == null) {
            return 0;
        }

        int left = recur(root.left);
        if (left == -1) {
            return -1;
        }
        int right = recur(root.right);
        if (right == -1){
            return -1;
        }

        return Math.abs(left - right) < 2 ? Math.max(left,right)+1 : -1;
    }

    /**
     * 方法二、 先序遍历 + 判断深度（从顶至底）
     * @param root
     * @return
     */
    public boolean isBalance2(TreeNode root){
        if (root == null)
            return true;
        return Math.abs(depth(root.left) - depth(root.right)) <= 1
                && isBalance2(root.left)&&isBalance2(root.right);
    }
    private int depth(TreeNode root){
        if (root == null)
            return 0;
        return Math.max(depth(root.left),depth(root.right))+1;
    }

}
