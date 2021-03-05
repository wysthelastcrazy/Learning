package com.wys.offer.array;

/**
 * @author wangyasheng
 * @date 2021-03-04
 * @describe:剑指 Offer 29. 顺时针打印矩阵
 */
public class Offer29_spiralOrder {
    public int[] spiralOrder(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return new int[0];
        }
        int[] order = new int[matrix.length*matrix[0].length];

        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;
        int index = 0;
        while (top <= bottom && left <= right) {
            // 上
            for (int i = left; i <= right; i++) {
                order[index++] = matrix[top][i];
            }
            // 右
            for (int i = top + 1; i <= bottom; i++) {
                order[index++] = matrix[i][right];
            }
            if (top != bottom)
                // 下
                for (int i = right - 1; i >= left; i--) {
                    order[index++] = matrix[bottom][i];
                }
            if (left != right)
                // 左
                for (int i = bottom - 1; i > top; i--) {
                    order[index++] = matrix[i][left];
                }
            top++; bottom--; left++; right--;
        }
        return order;
    }
}
