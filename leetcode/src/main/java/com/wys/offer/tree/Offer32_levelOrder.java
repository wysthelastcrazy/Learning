package com.wys.offer.tree;

import com.wys.offer.tree.node.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author wangyasheng
 * @date 2021-03-10
 * @describe:剑指 Offer 32 - I. 从上到下打印二叉树(二叉树的广度优先搜索)
 *
 * 从上到下打印出二叉树的每个节点，同一层的节点按照从左到右的顺序打印。
 *
 * 例如:
 * 给定二叉树: [3,9,20,null,null,15,7],
 *
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 * 返回：
 *
 * [3,9,20,15,7]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/cong-shang-dao-xia-da-yin-er-cha-shu-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Offer32_levelOrder {

    /**
     * 解题思路：
     * 题目要求的二叉树的从上至下打印（即按层打印），又称为二叉树的广度优先搜索（BFS）；
     * BFS通常借助队列的先入先出特性来实现。
     *
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
     * @param root
     * @return
     */
    public int[] levelOrder(TreeNode root) {
        //root为null时返回空列表
        if (root == null)
            return new int[0];
        //创建包含根节点的队列
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        //创建存放数据的list
        ArrayList<Integer> ans = new ArrayList<>();

        while(!queue.isEmpty()){
            //取出并删除当前队列头节点
            TreeNode node = queue.poll();
            ans.add(node.val);
            if (node.left != null){
                //如果左子树不为null，把当前左子树的根节点加入队列
                queue.add(node.left);
            }
            if (node.right != null){
                //如果右子树不为null，把当前右子树的根节点加入队列
                queue.add(node.right);
            }
        }
        int[] res = new int[ans.size()];
        for(int i = 0; i < ans.size(); i++){
            res[i] = ans.get(i);
        }
        return res;
    }
}
