package com.wys.leetcode.tree;

/**
 * @author wangyasheng
 * @date 2021/1/20
 * @Describe: 二叉树节点
 */
class TreeNode {
    public int val;
    public TreeNode left,right;
    public TreeNode(int val){
        this.val=val;
        this.left=this.right=null;
    }
}
