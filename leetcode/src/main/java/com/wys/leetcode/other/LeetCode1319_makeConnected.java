package com.wys.leetcode.other;

import java.util.Arrays;

/**
 * @author wangyasheng
 * @date 2021/1/23
 * @Describe: leetCode1319:连通网络的操作次数
 *
 * 用以太网线缆将 n 台计算机连接成一个网络，计算机的编号从 0 到 n-1。线缆用 connections 表示，其中 connections[i] = [a, b] 连接了计算机 a 和 b。
 *
 * 网络中的任何一台计算机都可以通过网络直接或者间接访问同一个网络中其他任意一台计算机。
 *
 * 给你这个计算机网络的初始布线 connections，你可以拔开任意两台直连计算机之间的线缆，并用它连接一对未直连的计算机。请你计算并返回使所有计算机都连通所需的最少操作次数。如果不可能，则返回 -1 。
 *
 * 示例 1：
 * 输入：n = 4, connections = [[0,1],[0,2],[1,2]]
 * 输出：1
 * 解释：拔下计算机 1 和 2 之间的线缆，并将它插到计算机 1 和 3 上。
 *
 * 示例 2：
 *
 * 输入：n = 6, connections = [[0,1],[0,2],[0,3],[1,2]]
 * 输出：-1
 * 解释：线缆数量不足
 *
 * 示例 3：
 *
 * 输入：n = 5, connections = [[0,1],[0,2],[3,4],[2,3]]
 * 输出：0
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/number-of-operations-to-make-network-connected
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
class LeetCode1319_makeConnected {

    /**
     * 使用并查集
     * 利用并查集将connections已经连接的点统计出来，
     * 然后总数减去已经连接的点，就是未连接的点，未连接
     * 的点数量，即为需要操作的次数。
     * 时间复杂度：O(m⋅α(n))，其中 mm 是数组 \textit{connections}connections 的长度，
     * α是阿克曼函数的反数。
     * 空间复杂度：O(n)即为并查集需要使用的空间。
     *
     * @param n
     * @param connections
     * @return
     */
    public int makeConnected(int n, int[][] connections) {
        if (connections.length < n - 1) {
            return -1;
        }

        UnionFind uf = new UnionFind(n);
        //n个节点未联通时，需要n-1次操作
        int count = n - 1;
        for (int[] conn : connections) {
            //将已连接集合connections中未加入生成树的节点合并入生成树，同时需要操作的次数-1
            if (uf.unite(conn[0], conn[1])) {
                // uf.merge(conn[0], conn[1]);
                count--;
            }
        }
        return count;
    }

    // 并查集模板
    class UnionFind {
        int[] parent;
        int[] size;
        int n;


        public UnionFind(int n) {
            this.n = n;
            this.parent = new int[n];
            this.size = new int[n];
            Arrays.fill(size, 1);
            for (int i = 0; i < n; ++i) {
                parent[i] = i;
            }
        }

        public int find(int x) {
            return parent[x] == x ? x : (parent[x] = find(parent[x]));
        }

        public boolean unite(int x, int y) {
            int fx = find(x);
            int fy = find(y);
            if (fx == fy) {
                return false;
            }
            mergeInner(fx,fy);
            return true;
        }

        private void mergeInner(int fx , int fy){
            //如果根节点相等，则已经在同一个生成树中，直接返回
            if (fx == fy){
                return;
            }
            if (size[fx] < size[fy]) {
                int temp = fx;
                fx =fy;
                fy = temp;
            }
            parent[fy] = fx;
            if (size[fx] == size[fy]){
                //如果深度相同且根节点不同，
                //则新的根节点深度+1（+1是因为深度相同是，合并之后总深度只增加了一层，
                // 如果被合并的树深度小，则合并后的树深度不变）
                //此处rank[fy]++是因为深度相同时，fx合并到fy上
                size[fy]++;
            }
        }
        /**
         * 合并x，y两个节点所在的生成树
         * @param x
         * @param y
         */
        public void merge(int x,int y){
            //先找到两个生成树的根节点
            int fx = find(x),fy = find(y);
            mergeInner(fx,fy);
        }
    }
}
