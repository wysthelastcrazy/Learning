package com.wys.offer.tree;

import com.wys.offer.tree.node.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * @author wangyasheng
 * @date 2021-03-12
 * @describe:二叉树的前、中、后序遍历
 */
public class Traversal {

    /**
     * 前序遍历 + 递归
     *
     * 时间复杂度：O(n)，其中 n 是二叉树的节点数。每一个节点恰好被遍历一次。
     * 空间复杂度：O(n)，为递归过程中栈的开销，平均情况下为 O(logn)，
     * 最坏情况下树呈现链状，为 O(n)
     * @param root
     * @return
     */
    public List<Integer> preOrderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        preOrder(root,res);
        return res;
    }
    public void preOrder(TreeNode root,List<Integer> res){
        if (root == null){
            return;
        }
        res.add(root.val);
        preOrder(root.left,res);
        preOrder(root.right,res);
    }

    /**
     * 前序遍历 + 迭代
     *
     * 时间复杂度：O(n)，其中 n 是二叉树的节点数。每一个节点恰好被遍历一次。
     * 空间复杂度：O(n)，为迭代过程中显式栈的开销，平均情况下为 O(logn)，
     * 最坏情况下树呈现链状，为 O(n)
     *
     * @param root
     * @return
     */
    public List<Integer> preOrderTraversal2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null){
            return res;
        }
        Stack<TreeNode> stack = new Stack<>();
        //此处赋值是为了保证外部的root节点不改变
        TreeNode node = root;
        while (!stack.isEmpty() || node != null){
            //遍历left子树，将左子树的根添加并移向下一级左子树
            while (node != null);{
                res.add(node.val);
                stack.push(node);
                node = node.left;
            }
            node = stack.pop();
            node = node.right;
        }
        return res;
    }


    /**
     * 后序遍历 + 递归
     * @param root
     * @return
     */
    public List<Integer> postOrderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        postOrder(root,res);
        return res;
    }
    private void postOrder(TreeNode root, List<Integer> res){
        if (root == null){
            return;
        }
        postOrder(root.left,res);
        postOrder(root.right,res);
        res.add(root.val);
    }

    /**
     * 后序遍历 + 迭代
     * @param root
     * @return
     */
    public List<Integer> postOrderTraversal2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null){
            return res;
        }
        Stack<TreeNode> stack = new Stack<>();
        TreeNode prev = null;
        while (root != null || !stack.isEmpty()){
            while (root != null){
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            if (root.right == null || root.right == prev){
                res.add(root.val);
                prev = root;
                root = null;
            }else{
                stack.push(root);
                root = root.right;
            }
        }
        return res;
    }

    /**
     * 中序遍历 + 递归
     * @param root
     * @return
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        inorder(root, res);
        return res;
    }

    public void inorder(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        inorder(root.left, res);
        res.add(root.val);
        inorder(root.right, res);
    }

    /**
     * 中序遍历 + 非递归
     * @param root
     * @return
     */
    public List<Integer> inorderTraversal2(TreeNode root){
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        while (root != null || !stack.isEmpty()){
            while (root !=null){
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            res.add(root.val);
            root = root.right;
        }
        return res;
    }
}
