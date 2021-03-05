package com.wys.offer.array;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyasheng
 * @date 2021-02-06
 * @describe:剑指 Offer 03. 数组中重复的数字
 *
 * 找出数组中重复的数字。
 *
 *
 * 在一个长度为 n 的数组 nums 里的所有数字都在 0～n-1 的范围内。数组中某些数字是重复的，但不知道有几个数字重复了，也不知道每个数字重复了几次。请找出数组中任意一个重复的数字。
 *
 * 示例 1：
 *
 * 输入：
 * [2, 3, 1, 0, 2, 5, 3]
 * 输出：2 或 3
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/shu-zu-zhong-zhong-fu-de-shu-zi-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Offer03_findRepeatNumber {

    /**
     * 思路一：哈希表
     * 用哈希表记录已经出现过的数字，一旦遍历到的数字已经在
     * 哈希表中出现过，就说明数字重复，返回该数字。
     *
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * @param nums
     * @return
     */
    public static int findRepeatNumber1(int[] nums){
        Map<Integer,Integer> hasMap = new HashMap<>();
        for (int num : nums){
            if (hasMap.containsKey(num)){
                return num;
            }
            hasMap.put(num,0);
        }
        return -1;
    }

    /**
     * 思路二：原地交换
     * 数字的范围与数组长度相同，可以把数组看成是哈希表；
     * 把数组的索引看成试试哈希表的key，数组元素看成哈希表的val；
     * 把值为val的元素放在键也为val的位置上，就是哈希表的键值对的映射
     * 关系为key == val；
     * 如果当前数字nums[i]和索引不相等，就把nums[i]放在
     * 索引也为nums[i]的位置去，就把索引为nums[i]和i的元素对换；
     * 如果数组在索引为nums[i]位置的数在交换前就已经是nums[i]，
     * 说明nums[i]是重复数字，返回nums[i]；
     * 如果交换后在nums[i]仍不等于i，要继续交换，这是使用while循环的原因。
     *
     * 时间复杂度：O(n)
     * 空间复杂度:O(1)
     * @param nums
     * @return
     */
    public static int findRepeatNumber2(int[] nums){
        for (int i = 0; i < nums.length ;i++){
            while (nums[i] == nums[nums[i]]){
                return nums[i];
            }
            int tmp = nums[nums[i]];
            nums[nums[i]] = nums[i];
            nums[i] = tmp;
        }
        return -1;
    }
}
