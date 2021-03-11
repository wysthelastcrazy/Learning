package com.wys.offer.array;

/**
 * @author wangyasheng
 * @date 2021-03-11
 * @describe:剑指 Offer 39. 数组中出现次数超过一半的数字
 * 数组中有一个数字出现的次数超过数组长度的一半，请找出这个数字。
 *
 * 你可以假设数组是非空的，并且给定的数组总是存在多数元素。 
 *
 * 示例 1:
 *
 * 输入: [1, 2, 3, 2, 2, 2, 5, 4, 2]
 * 输出: 2
 *  
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/shu-zu-zhong-chu-xian-ci-shu-chao-guo-yi-ban-de-shu-zi-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Offer39_majorityElement {

    /**
     * 本题常见的三种解法：
     * 1、哈希表统计法：遍历数组nums，用HashMap统计数量,
     * 时间和空间复杂度均为O(N);
     *
     * 2、数组排序法：将数组nums排序，数组中点的元素一定为众数；
     *
     * 3、摩尔投票法：核心理念为票数正负抵消，时间复杂度为O(N)空间复杂度为O(1);
     * 设输入数组nums的众数为x，数组长度为n，则：
     * 推论一：若记众数的票数为+1，非众数票数为-1，则一定有所有数组的票数和 > 0；
     * 推论二：若数组的前a个数字的票数和 = 0，则数组剩余（n - a）个是和票数和 > 0,
     *        即后（n - a）个数字的众数仍为x；
     * @param nums
     * @return
     */
    public int majorityElement(int[] nums) {
        //众数
        int x = 0;
        //票数统计
        int votes = 0;
        for (int num: nums){
            //票数为0时，重新设置众数
            if (votes == 0){
                x = num;
            }
            //数字和众数一样则+1，否则-1
            votes += num == x ? 1: -1;
        }
        return x;
    }
}
