package com.wys.offer.array;

/**
 * @author wangyasheng
 * @date 2021-03-04
 * @describe:剑指 Offer 53 - I. 在排序数组中查找数字 I
 *
 * 统计一个数字在排序数组中出现的次数。
 *
 *
 * 示例 1:
 *
 * 输入: nums = [5,7,7,8,8,10], target = 8
 * 输出: 2
 * 示例 2:
 *
 * 输入: nums = [5,7,7,8,8,10], target = 6
 * 输出: 0
 *  
 *
 * 限制：
 *
 * 0 <= 数组长度 <= 50000
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/zai-pai-xu-shu-zu-zhong-cha-zhao-shu-zi-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Offer53_search {

    public int search(int[] nums, int target) {
        int i = 0;
        int j = nums.length - 1;
        //二分查找右边界
        while(i <= j){
            int m = (i+j)/2;
            if (nums[m]<= target){
                i = m + 1;
            }else{
                j = m -1;
            }
        }
        int right = i;
        //若数组中无target，则提前返回
        if (j >= 0 &&nums[j] !=target) return 0;

        //查找左边界
        i = 0;
        j = nums.length - 1;
        while(i <= j){
            int m = (i+j)/2;
            if (nums[m] < target){
                i = m + 1;
            }else{
                j = m -1;
            }
        }
        int left = j;
        return right - left -1;
    }
}
