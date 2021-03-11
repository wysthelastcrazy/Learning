package com.wys.offer.array;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyasheng
 * @date 2021-03-11
 * @describe:剑指 Offer 57. 和为s的两个数字
 *
 * 输入一个递增排序的数组和一个数字s，在数组中查找两个数，使得它们的和正好是s。如果有多对数字的和等于s，则输出任意一对即可。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：nums = [2,7,11,15], target = 9
 * 输出：[2,7] 或者 [7,2]
 * 示例 2：
 *
 * 输入：nums = [10,26,30,31,47,60], target = 40
 * 输出：[10,30] 或者 [30,10]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/he-wei-sde-liang-ge-shu-zi-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Offer57_twoSum {

    /**
     * 双指针 + 证明
     *
     * 时间复杂度：O(N)
     * 空间复杂度：O(1)
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum(int[] nums, int target) {
        int start = 0;
        int end = nums.length -1;
        while (start < end){
            int sum = nums[start] + nums[end];
            if (sum < target){
                start ++;
            }else if(sum > target){
                end --;
            }else{
                return new int[]{nums[start],nums[end]};
            }

        }
        return new int[0];
    }
    /**
     * 哈希表：
     * 利用哈希表存取元素时间复制度为O（1）的特性，来实现值的比较
     * 时间复杂度：O（N）
     * 空间复杂度：O（N）
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum2(int[] nums,int target){
        Map<Integer,Integer> map  = new HashMap();
        for (int i = 0;i < nums.length ; i++){
            if (map.containsKey(target - nums[i])){
                return new int[]{map.get(target-nums[i]),i};
            }
            map.put(nums[i],i);
        }
        return null;
    }
    /**
     * 暴力枚举法
     * 时间复杂度：O（N^2）
     * 空间复杂度：O（1）
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum3 (int[] nums,int target){
        for (int i = 0;i<nums.length; i++){
            for (int j = i+1; j<nums.length; j++){
                if (nums[i]+nums[j] == target){
                    return new int[]{i,j};
                }
            }
        }
        return null;
    }


}
