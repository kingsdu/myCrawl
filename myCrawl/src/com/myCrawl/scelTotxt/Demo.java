package com.myCrawl.scelTotxt;

import java.io.IOException;

public class Demo {

	/**
	 * @Description: 通过代码的方式，将搜狗词库转化为Txt文件。----该功能可用软件代替
	 * @param:
	 * @return:
	 * @date: 2017-12-11  
	 */
	public static void main(String[] args) {  
		//单个scel文件转化    
		FileProcessing scel = new SougouScelFileProcessing();  
//		scel.parseFile("G:\\TermExtract\\dic\\shiyou.scel", "G:\\TermExtract\\dic\\shiyou.txt", true);  

		//多个scel文件转化为一个txt (格式：拼音字母 词)  
//		try {  
//			scel.parseFiles("G:\\TermExtract\\dic", "G:\\TermExtract\\dic\\allDic_words.txt", true);  
//		} catch (IOException e) {  
//			e.printStackTrace();  
//		}  
		//多个scel文件转化为多个txt文件  
		scel.setTargetDir("G:\\TermExtract\\dic\\scel_txt");//转化后文件的存储位置  
		scel.parseFile("G:\\TermExtract\\dic\\scel_txt",false);  
	}  
}
