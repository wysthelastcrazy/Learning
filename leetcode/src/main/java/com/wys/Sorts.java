package com.wys;

/**
 * @author wangyasheng
 * @date 2021-02-16
 * @describe:常用排序算法
 */
public class Sorts {
    /**
     * 如何分析一个"排序算法"
     *
     * 学习排序算法，除了学习它的算法原理、代码实现之外，更重要的是
     * 要学会如何评价、分析一个排序算法。
     *
     * 分析一个排序算法，要从以下几个方面入手：
     *
     * 1、排序算法的执行效率：对于执行效率，一般从这几个方面衡量
     *    a、最好情况、最坏情况、平均情况时间复杂度（考虑原数组有序度不同的情况）
     *    b、时间复杂度的系数、常数、低阶
     *    c、比较次数和交换次数
     *
     *  2、排序算法的内存消耗
     *  内存消耗可以通过空间复杂度来衡量。对于排序算法，引入一个新的概念：原地排序。
     *  原地排序算法，就是特指空间复杂度为O(1)的排序算法。
     *
     *  3、排序算法的稳定性
     *  仅仅用执行效率和内存消耗来衡量排序算法的好坏是不够的。针对排序算法，
     *  我们还有一个重要的度量指标：稳定性。这个概念是说，如果待排序的序列中
     *  存在值相等的元素，经过排序之后，相等元素之间原有的先后顺序不变。
     *  稳定性的意义：有时需要排序的不是单纯的整数，而是一组对象，我们需要
     *  按照对象的某个key来排序，稳定算法可以保持key相同的两个对象
     *  在排序之后的前后顺序不变。
     *  例如：电商订单排序，要求按金额排序，金额相同的安时间排序。
     *  就可以先按时间排序完，再使用金额排序。
     */


    /**====================时间复杂度为O(n^2)的排序算法====================**/
    /**
     * 冒泡排序
     *
     * 冒泡排序只会操作相邻的两个数据。每次冒泡操作都会对相邻的两个
     * 元素进行比较，看是否满足大小关系。如果不满足就互换。
     * 一次冒泡会让至少一个元素移动到它应该在的位置，重复n次，
     * 就完成了n个数据的排序工作。
     *
     * 1、原地排序：冒泡排序过程中只涉及相邻数据的交换，只需要常量级
     * 的临时空间，所以它的空间复杂度是o(1)，是原地排序算法。
     *
     * 2、稳定性：在冒泡排序中，只有交换才可以该笔拿两个元素的前后顺序。
     * 为了保证冒泡排序算法的稳定性，当相邻的两个元素大小相等的时候，
     * 不做交换，相同大小的数据在排序前后不改变顺序，所有是稳定的排序算法。
     *
     * 3、时间复杂度：
     *   最好情况：要排序的数据已经是有序的了，只需要进行一次冒泡操作，
     *           所以最好情况的时间复杂度是O(n)。
     *   最坏情况：要排序的数据刚好是倒序排列，需要进行n次冒泡操作，
     *           所以最坏的时间复杂度为O(n^2)。
     *   平均时间复杂度：O(n^2)
     * @param arr
     */
    public static void BubbleSort(int[] arr){
        if (arr == null || arr.length<=1)
            return;
        int n = arr.length;
        for (int i = 0; i < n; i++){
            //提前推出冒泡循环的标志位
            boolean flag = false;
            for (int j = 0; j < n - i -1; j++){
                if (arr[j] > arr[j+1]){
                    //交换
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                    //表示有数据交换
                    flag = true;
                }
            }
            if (!flag)
                //如果没有数据交换，提前退出
                break;
        }
    }

    /**
     * 插入排序
     *
     * 插入排序将数组中的数据分为两个区间：已排序区间和未排序区间。
     * 初始已排序区间只有一个元素，就是第一个元素。然后取未排序
     * 区间中的元素，在已排序区间中找到合适的插入位置将其插入，
     * 并保证已排序区间数据一直有序。重复这个过程，直到未排序
     * 区间中的元素为空结束。
     *
     * 1、原地排序：插入排序不需要额外的存储空间，所以空间复杂度
     * 是o(1)，是一个原地排序算法。
     *
     * 2、稳定性：在插入排序汇总，对于值相同的元素，可以把后边
     * 出现的元素，插入到前面出现元素的后面，这样就可以保持原有
     * 的前后顺序不变，所以插入排序是稳定的排序算法。
     *
     * 3、时间复杂度：
     *    最好情况：排序的数据已经是有序的。不需要搬移任何数据。
     *    如果从尾到头在有序数组里面查找插入位置，每次只需要比
     *    较一个数据就能确定插入的位置。这情况下最好的时间复杂度
     *    是O(n)，注意，这里是从尾到头遍历已经有序的数据。
     *
     *    最坏情况：如果数组是倒序的，每次插入都相当于在数组的
     *    第一个位置插入新的数据，所以需要移动大量的数据，所以
     *    最坏情况时间复杂度是O(n^2).
     *
     *    平均情况：在数组中插入一个数据的平均复杂度是O(n)，所以
     *    对于插入排序来说，每次插入操作都相当于在数组中插入一个数据，
     *    循环执行n次插入操作，所以平均时间复杂度为O(n^2)。
     * @param a
     */
    public static void InsertionSort(int[] a){
        if (a == null || a.length <= 1)
            return;
        int n = a.length;
        //将1 - length-1位置的数据，依次插入到前边一排序数列中
        for (int i = 1 ; i < n; i++){
            int value = a[i];
            int j = i - 1;
            //在0-j中查找插入的位置，此时0 - j位置为有序数列
            for (; j >= 0; j--){
                if (a[j] > value){
                    //如果当前有序数列的最大值大于插入数据，
                    // 则数据移动，将该值后移一位
                    a[j+1] = a[j];
                }else{
                    break;
                }
            }
            //插入数据
            a[j+1] = value;
        }
    }

    /**
     * 选择排序
     *
     * 选择排序算法的实现思路类似插入排序，也分为已排序区间和未排序区间。
     * 但是选择排序每次会从未排序区间中找到最小的元素，将其放到
     * 已排序区间的末尾。
     *
     * 1、原地排序：空间复杂度是O(1)，是原地排序。
     *
     *
     * 2、稳定性：选择排序是一种不稳定的排序算法。选择排序每次都要找
     * 剩余未排序元素中的最小值，并和前面的元素交换位置，这样破坏了
     * 稳定性。
     *
     * 3、时间复杂度：最好情况、最坏情况和平均情况都是O(n^2)。
     * @param a
     */
    public static void SelectionSort(int[] a){
        if (a == null || a.length <= 1)
            return;
        int n = a.length;
        //需要比较的次数，数组长度减1
        for (int i = 0; i < n; i++){
            //先假设每次循环时，最小的索引为i
            int minIndex = i;
            for (int j = i+1; j < n;j++){
                if (a[j]<a[minIndex]){
                    //寻找最小数，将最小数的索引保存
                    minIndex = j;
                }
            }
            //经过一轮循环，就可以找出第一个最小值的索引，
            // 然后把最小值放到i的位置
            int temp = a[i];
            a[i] = a[minIndex];
            a[minIndex] = temp;
        }
    }


    /**====================时间复杂度为O(nlogn)的排序算法====================**/

    /**
     * 归并排序
     *
     * 归并排序的核心思想：先把数组从中间分成前后两部分，然后对前后
     * 两部分分别排序，再将排序好的两部分合并在一起。使用的是分治思想。
     * 分治算法一般都是用递归来实现的。分治是一种解决问题的处理思想，
     * 递归是一种编程技巧。
     *
     * 1、稳定性：归并排序稳不稳定关键要看merge()函数，在合并
     * 过程中，如果a[start...mid]和a[mid+1...end]之间有值
     * 相同的元素，可以先把a[start...mid]中的元素放入temp数组
     * 这样就保证了值相同的元素，在合并前后的先后顺序不变。
     *
     * 2、时间复杂度：归并排序的执行效率与要排序的原始
     * 数组的有序成都无关，所以其时间复杂度非常稳定，最好情况、
     * 最坏情况和平均情况，时间复杂度都是O(nlogn)。
     *
     * 3、空间复杂度：归并排序在合并两个有序数组为一个有序数组时，
     * 需要借助额外的存储空间，空间复杂度为O(n)，不是原地排序。
     * @param a
     */
    public static void MergeSort(int[] a){
        if (a == null || a.length<=1)
            return;
        int n = a.length;
        int[]temp = new int[n];
        mergeSort(a,0,n-1,temp);
    }
    private static void mergeSort(int[] a,int start,int end,int[] temp){
        if (start < end){
            int mid = (start + end)/2;
            //排序数组a中start到mid区间的元素
            mergeSort(a,start,mid,temp);
            //排序数组a中mid+1到end区间的元素
            mergeSort(a,mid+1,end,temp);
            //将数组a中的有序区间[start...mid]和[mid+1...end]进行合并
            merge(a,start,mid,end,temp);
        }
    }
    /**
     * 合并操作
     * @param a
     * @param start
     * @param mid
     * @param end
     * @param temp
     */
    private static void merge(int[] a, int start,int mid,int end,int[] temp){
        int i = 0;
        //左侧序列和右侧序列其实索引
        int j = start,k = mid + 1;
        //两个区间都没有遍历完成时，将两个区间当前元素较小
        //的添加进临时数组temp中
        while (j <= mid && k <= end){
            if (a[j] < a[k]){
                temp[i++] = a[j++];
            }else{
                temp[i++] = a[k++];
            }
        }
        //如果左侧区间没有遍历完成，则将左侧剩余元素添加到临时数组
        while (j <= mid){
            temp[i++] = a[j++];
        }
        //如果右侧区间没有遍历完成，则将左侧剩余元素添加到临时数组
        while (k <= end){
            temp[i++] = a[k++];
        }
        //将临时数组得到的归并后的有序数列，重新写进a数组的[start...end]区间中
        for (int t = 0; t < i; t++){
            a[start+t] = temp[t];
        }
    }


    /**
     * 快排
     *
     * 快速排序也是利用分治思想，将要排序的数组区间中，选择任意
     * 一个数据作为pivot（分区点），将小于pivot的放在左边，将
     * 大于pivot的放在右边，将pivot放在中间。根据分治、递归的
     * 处理思想，我们可以用递归将左侧和右侧区间进行处理，直到
     * 区间缩小为1，就说明所有的数据都有序了。
     *
     * 1、稳定性：不稳定，在分区中进行了位置交换。
     *
     * 2、空间复杂度：O(1)原地排序。
     *
     * 3、时间复杂度：平均时间复杂度O(nlogn)，
     * 最坏O(n^2)在分区极度不均衡时退化为O(n^2)
     * @param a
     */
    public static void QuickSort(int[] a){
        quickSort(a,0,a.length-1);
    }
    private static void quickSort(int[] a,int start,int end){
        if (start >= end)
            return;
        //将[start...end]区间分区，并获取分区点下标
        int pivot = partition(a,start,end);
        quickSort(a,start,pivot-1);
        quickSort(a,pivot+1,end);
    }

    /**
     * 分区函数
     * @param a
     * @param start
     * @param end
     * @return
     */
    private static int partition(int[]a,int start,int end){
        //默认最后一个元素为分区元素
        int pivot = a[end];
        //i第一个大于pivot的元素下标
        int i = start;
        for (int j = start; j < end;j++){
            if (a[j] < pivot){
                swap(a,i,j);
                i = i+1;
            }
        }
        swap(a,i,end);
        return i;
    }

    /**
     * 数组交换位置
     */
    private static void swap(int[] a,int i,int j){
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
