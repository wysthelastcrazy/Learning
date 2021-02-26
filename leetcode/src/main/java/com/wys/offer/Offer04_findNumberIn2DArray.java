package com.wys.offer;

/**
 * @author wangyasheng
 * @date 2021-02-25
 * @describe:剑指 Offer 04. 二维数组中的查找
 *
 * 在一个 n * m 的二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个高效的函数，
 * 输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
 * 
 *
 * 示例:
 *
 * 现有矩阵 matrix 如下：
 *
 * [
 *   [1,   4,  7, 11, 15],
 *   [2,   5,  8, 12, 19],
 *   [3,   6,  9, 16, 22],
 *   [10, 13, 14, 17, 24],
 *   [18, 21, 23, 26, 30]
 * ]
 * 给定 target = 5，返回 true。
 *
 * 给定 target = 20，返回 false。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/er-wei-shu-zu-zhong-de-cha-zhao-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Offer04_findNumberIn2DArray {
    /**
     * 思路一：暴力法
     * 暴力法就是一个一个比较，没有利用有序信息
     *
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(1)
     * @param matrix
     * @param target
     * @return
     */
    public static boolean findNumberIn2DArray1(int[][] matrix, int target) {
        //行数
        int row = matrix.length;
        //列数
        int col = matrix[0].length;
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                if (matrix[i][j] == target)
                    return true;
            }
        }
        return false;
    }

    /**
     * 思路二：线性查找
     * 1）如果在左上角找，那么如果当数比目标值小的话，向下或者向有移动都可以让
     * 数变大，方向就不能确定；
     * 2）所以应该找从两个方向走一个变大一个变小的位置作为起始位置，
     * 右上角和左下角都满足；
     * 3）此实现以右上角为例
     *
     * 时间复杂度：O(m+n)
     * 空间复杂度：O(1)
     * @param matrix
     * @param target
     * @return
     */
    public static boolean findNumberIn2DArray2(int[][] matrix, int target) {
        //行数
        int row = matrix.length;
        //列数
        int col = matrix[0].length;
        //当前位置所在的行和列，起始位置设置为右上角
        int curRow = 0,curCol = col - 1;
        while (curRow < row && curCol >= 0){
            if (matrix[curRow][curCol] == target){
                return true;
            }
            if (matrix[curRow][curCol] < target){
                //target大于当前位置时，位置下移
                curRow++;
            }else {
                //target小于当前位置时，位置左移
                curCol--;
            }
        }
        return false;
    }
}
