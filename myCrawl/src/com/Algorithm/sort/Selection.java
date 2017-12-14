package com.Algorithm.sort;

/**
 * Created by Administrator on 2017/11/7.
 * 选择排序
 */
public class Selection {

    /**
     * @Description:选择排序，封装的思想很棒，很值得学习！
     * 总体思想：
     * 首先找到数组中最小（最大）的一个元素，将它和第一个元素交换，然后在剩下的元素中找到最小（最大）的元素在与第二个元素交换
     * .....循环...实现排序
     *
     * 1次比较需要交换1个元素，比较N-1-i次
     * N次比较的过程中，该算法总共有N次交换，N*N/2次交换
     *
     * 选择排序有两个很鲜明的特点：
     * 1 运行时间和输入无关。第一次的扫描寻找最小元素的过程并不能为第二次的过程提供任何帮助。
     * 2 数据移动是最少的。
     * @param:
     * @return:
     * @Author: Du
     * @Date: 2017/11/7 14:39
     */
    public static void sort(Comparable[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            int min = i;
            for (int j = i+1; j < N; j++) {
                if(Example.less(a[j],a[min])){
                     min = j;
                }
            }
            Example.exch(a,i,min);
        }
        Example.show(a);
    }


    public static void main(String[] args) {
        Integer[] a = {3,5,2,9,6,7};
        sort(a);
    }
}
