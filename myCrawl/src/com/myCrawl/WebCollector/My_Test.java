package com.myCrawl.WebCollector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class My_Test {

	public static void main(String args[]){
		int [] x = {3,5,2,7,4};
		int [] y = {8,0,13,6,9};
		int k=0;
		int []x_result = insertSort(x);
		int []y_result = insertSort(y);
		int [] xy = new int[y_result.length+x_result.length];
		for(int i=0;i<x_result.length;i++,k++){
			xy[k] = x_result[i];
		}
		for(int i=0;i<y_result.length;i++,k++){
			xy[k] = y_result[i];
		}
		int []init = recursively(xy,x_result.length,x_result.length+y_result.length);
		for(int i:init){
			System.out.print(i+" ");
		}
	}


	/**
	 * @Description: 分治排序>插入排序
	 * @param init:初始数组，里面含有2个已排好序的数组(X,Y)，但整体无序
	 * @param p:X数组的长度
	 * @param q:Y数组的长度
	 * @return:int[] init，整体排好序的数组
	 * @date: 2017-10-18  
	 */
	public static int[] recursively(int[] init,int p,int q){
		int x_index = p;
		int y_index = q-p;
		int []x_array = new int[x_index];
		int []y_array = new int[y_index];
		int k = 0;
		//分出两个数组中的值
		for(int i=0;i<p;i++){
			x_array[i] = init[i];
		}
		for(int j=0,i=x_index;i<q;i++,j++){
			y_array[j] = init[i];
		}
		int i=0,j=0;
		//将两个数组中的值进行比较
		while(i<p&&j<p){	
			if(x_array[i] < y_array[j]){
				init[k] = x_array[i];
				i++;
			}else{
				init[k] = y_array[j];
				j++;
			}
			k++;
		}
		return init;
	}
	


	/**
	 * @Description: 插入排序
	 * @return:
	 * @date: 2017-10-18  
	 */
	public static int[] insertSort(int []inputNum){
		//i代表待插入的数集
		//j代表i-1,待插入的数集的前一个数，下一个要进行比较的数字
		for(int i=1;i<inputNum.length;i++){
			int key = inputNum[i];//记录插入值
			int j = i-1;//待比较的值头部
			while(j>=0&&inputNum[j]>key){
				inputNum[j+1]=inputNum[j];//数字交换
				j--;//下标移位
			}
			inputNum[j+1]=key;//获取插入位置，插入
		}
		return inputNum;
	}


	
	
	public static void extractTime(){
		String sourceTime = "2017-09-27 21:32:04";
		String regex = "([1-2][0-9]{3})[^0-9]{1,5}?([0-1]?[0-9])[^0-9]{1,5}?([0-9]{1,2})[^0-9]{1,5}?([0-2]?[1-9])[^0-9]{1,5}?([0-9]{1,2})[^0-9]{1,5}?([0-9]{1,2})";
		String regex1 = "([1-2][0-9]{3})[^0-9]{1,5}?([0-1]?[0-9])[^0-9]{1,5}?([0-9]{1,2})";
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(sourceTime);

		if (matcher.find()) {
			System.out.println(matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3) + " " + matcher.group(4) + ":" + matcher.group(5) + ":" + matcher.group(6));
		}
	}
}
