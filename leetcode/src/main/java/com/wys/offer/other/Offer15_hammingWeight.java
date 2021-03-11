package com.wys.offer.other;

/**
 * @author wangyasheng
 * @date 2021-03-11
 * @describe:剑指 Offer 15. 二进制中1的个数
 *
 * 请实现一个函数，输入一个整数（以二进制串形式），输出该数二进制表示中 1 的个数。例如，把 9 表示成二进制是 1001，有 2 位是 1。因此，如果输入 9，则该函数输出 2。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/er-jin-zhi-zhong-1de-ge-shu-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Offer15_hammingWeight {
    // you need to treat n as an unsigned value

    /**
     * 方法一、逐位判断
     * 根据与运算定义，则：
     * 若n&1 = 0，则n二进制的最右一位为0；
     * 若n&1 = 1，则n二进制的最右一位为1；
     *
     * 根据以上特点，可以使用循环判断：
     * 1、判断n最右一位是否为1，根据结果计数；
     * 2、将n右移一位。
     *
     * 时间复杂度：O(log2^n)，log2^n代表数字n最高位1所在位数。
     * 空间复杂度：O(1)
     * @param n
     * @return
     */
    public int hammingWeight(int n) {
        int count = 0;
        while (n != 0){
            count += n & 1;
            n >>>= 1;
        }
        return count;
    }
    /**
     * 方法二、巧用n&(n -1)
     * 1、（n - 1）解析：二进制数字n最右边的1变成0，此1右边的0都变成1；
     * 2、n&(n - 1)解析：二进制数字n最右边的1变成0，其余不变；
     *
     * 空间复杂度：O(M)M为二进制数字中1的个数；
     * 时间复杂度：O(1)
     * @param n
     * @return
     */
    public int hammingWeight2(int n) {
        int count = 0;
        while (n != 0){
            count ++;
            n &= n-1;
        }
        return count;
    }
}
