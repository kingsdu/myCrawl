package com.myCrawl.test;

public class RegexTest {

	public static void main(String[] args) {
		regexTest();
	}
	
	
	
	
	/**
	 * @Description: 正则表达式捕获多个空格
	 * @param:
	 * @return:
	 * @date: 2017-12-16  
	 */
	public static void regexTest(){
		String regex = "\\s+";
		String words = "经营/vn 方面/n    生产/vn 经营/vn 方面/n ";
		String[] res = words.split(regex);
		for(int i=0;i<res.length;i++){
			System.out.println(res[i]);
		}		
	}
}
