package com.Algorithm.sort;

/**
 * Created by Administrator on 2017/11/7.
 *
 * 算法实现的模版
 * 综合了一些基本的算法实现需要的方法
 */
public class Example {

    /**
     * @Description:比较两个元素
     * @param:
     * @return: 如果v<w,返回true
     * @Author: Du
     * @Date: 2017/11/7 14:22
     */
    public static boolean less(Comparable v,Comparable w){
        return v.compareTo(w)<0;
    }


    /**
     * @Description:实现两数字的交换
     * @param: i，j表示交换的位置
     * @return:
     * @Author: Du
     * @Date: 2017/11/7 14:25
     */
    public static void exch(Comparable[] a,int i,int j){
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    /**
     * @Description:
     * @param:
     * @return:
     * @Author: Du
     * @Date: 2017/11/7 14:28
     */
    public static void show(Comparable[] a){
        for(int i=0;i<a.length;i++){
            System.out.println(a[i]+" ");
        }
        System.out.println();
    }


    /**
     * @Description:测试数组是否有序.比较后面一个数是否比前面一个数字小
     * @param:
     * @return:
     * @Author: Du
     * @Date: 2017/11/7 14:30
     */
    public static boolean isSorted(Comparable[] a){
        for (int i = 1; i < a.length; i++) {
            if(less(a[i],a[i-1])) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        Integer[] a = {5,3};
        if(less(a[0],a[1])){
            System.out.println("a");
        }
        show(a);
    }

}
