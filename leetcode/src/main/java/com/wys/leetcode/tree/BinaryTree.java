package com.wys.leetcode.tree;

import com.wys.leetcode.tree.node.TreeNode;

import java.util.*;

/**
 * Created by yas on 2019/3/18
 * Describe: 二叉树
 */
public class BinaryTree {
    /**
     * 获取二叉树最大节点
     * 使用递归
     * @param root
     * @return
     */
    public TreeNode maxNode(TreeNode root){
        if (root==null) return null;
        //第一种实现方式，使用递归
        TreeNode maxLeft=maxNode(root.left);
        TreeNode maxRight=maxNode(root.right);
        if (maxLeft!=null){
            if (root.val<maxLeft.val){
                root=maxLeft;
            }
        }
        if (maxRight!=null){
            if (root.val<maxRight.val){
                root=maxRight;
            }
        }
        return root;

    }

    /**
     * 二叉树的前序遍历
     * 递归
     * @param root
     * @return
     */
    public List<Integer> preorderTraversal(TreeNode root){
        List<Integer> mList=new ArrayList<>();
        if (root==null) return mList;

        TreeNode left=root.left;
        TreeNode right=root.right;
        List<Integer> leftList=preorderTraversal(left);
        List<Integer> rightList=preorderTraversal(right);
        if (left==null&&right==null){
            mList.add(root.val);
        }else if (left==null){
            mList.add(root.val);
            mList.addAll(rightList);
        }else if (right==null){
            mList.add(root.val);
            mList.addAll(leftList);
        }else{
            mList.add(root.val);
            mList.addAll(leftList);
            mList.addAll(rightList);
        }

        return mList;
    }

    /**
     * 二叉树前序遍历
     * 非递归
     * @param root
     * @return
     */
    public List<Integer> preorderTraversal2(TreeNode root){
        List<Integer> mList=new ArrayList<>();
        Stack<TreeNode> stack=new Stack<>();
        while (root!=null||!stack.empty()){
            while (root!=null){
                mList.add(root.val);
                stack.push(root);
                root=root.left;
            }
            root=stack.pop();
            root=root.right;

        }
        return mList;
    }

    /**
     * 二叉树中序遍历
     * @param root
     * @return
     */
    public List<Integer> inorderTraversal(TreeNode root){
        List<Integer> mList=new ArrayList<>();
        if (root==null) return mList;

        TreeNode left=root.left;
        TreeNode right=root.right;
        List<Integer> leftList=inorderTraversal(left);
        List<Integer> rightList=inorderTraversal(right);
        mList.addAll(leftList);
        mList.add(root.val);
        mList.addAll(rightList);
        return mList;
    }

    /**
     * 二叉树层次遍历(从顶向下)
     * @param root
     * @return
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        Queue<TreeNode> queue=new LinkedList<>();
        List<List<Integer>> wrapList=new LinkedList<>();

        if (root==null)return wrapList;

        queue.offer(root);
        while (!queue.isEmpty()){
            int levelNum = queue.size();
            List<Integer> subList = new LinkedList<Integer>();
            for(int i=0; i<levelNum; i++) {
                if(queue.peek().left != null) queue.offer(queue.peek().left);
                if(queue.peek().right != null) queue.offer(queue.peek().right);
                subList.add(queue.poll().val);
            }
            wrapList.add(subList);
        }

        return wrapList;
    }

    /**
     * 二叉树层次遍历（从底向上）
     * @param root
     * @return
     */
    public List<List<Integer>> levelOrderBottom(TreeNode root){
        Queue<TreeNode> queue=new LinkedList<>();
        List<List<Integer>> wrapList=new LinkedList<>();

        if (root==null)return wrapList;

        queue.offer(root);
        while (!queue.isEmpty()){
            int levelNum = queue.size();
            List<Integer> subList = new LinkedList<Integer>();
            for(int i=0; i<levelNum; i++) {
                if(queue.peek().left != null) queue.offer(queue.peek().left);
                if(queue.peek().right != null) queue.offer(queue.peek().right);
                subList.add(queue.poll().val);
            }
            ((LinkedList<List<Integer>>) wrapList).addFirst(subList);
        }

        return wrapList;
    }


    /**
     * 二叉树的最大深度
     * @param root
     * @return
     */
    public int maxDepth(TreeNode root){
        if (root==null) return 0;
        if (root.left==null&&root.right==null){
            return 1;
        }else{
            return Math.max(maxDepth(root.right),maxDepth(root.left))+1;
        }
    }

    /**
     * 二叉树最小深度
     * @param root
     * @return
     */
    public int minDepth(TreeNode root){
        if(root==null) return 0;
        int leftDepth=minDepth(root.left);
        int rightDepth=minDepth(root.right);

        if (leftDepth>0&&rightDepth>0){
            return Math.min(leftDepth,rightDepth)+1;
        }else if (leftDepth==0){
            return rightDepth+1;
        }else if (rightDepth==0){
            return leftDepth+1;
        }else{
            return 1;
        }
    }


}
