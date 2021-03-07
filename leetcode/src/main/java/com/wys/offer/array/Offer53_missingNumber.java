package com.wys.offer.array;

/**
 * @author wangyasheng
 * @date 2021-03-07
 * @describe:剑指 Offer 53 - II. 0～n-1中缺失的数字
 *
 * 一个长度为n-1的递增排序数组中的所有数字都是唯一的，并且每个数字都在范围0～n-1之内。
 * 在范围0～n-1内的n个数字中有且只有一个数字不在该数组中，请找出这个数字。
 *
 *  
 *
 * 示例 1:
 *
 * 输入: [0,1,3]
 * 输出: 2
 * 示例 2:
 *
 * 输入: [0,1,2,3,4,5,6,7,9]
 * 输出: 8
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/que-shi-de-shu-zi-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Offer53_missingNumber {

    /**
     * 解题思路：
     * 排序数组中的搜索问题，首先想到二分法 解决；
     * 根据题意，数字可以按照一下规则划分为两部分
     * 左子数组：nums[i] = i,右子数组：nums[i] != i；
     * 确实的数字等于"右子数组的首位元素"对应的索引；
     * 因此考虑使用二分法查找"右子数组的首位元素"。
     *
     * 时间复杂度：O(logN)
     * 空间复杂度：O(1)
     * @param nums
     * @return
     */
    public int missingNumber(int[] nums){
        int start = 0;
        int end = nums.length -1;
        while (start <= end){
            int mid = (start + end)/2;
            if (nums[mid] == mid){
                 start = mid + 1;
            }else{
                end = mid - 1;
            }
        }
        return start;
    }
}
