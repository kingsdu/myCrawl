package com.myCrawl.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class My_read {

	public static void main(String[] args){
//		StringBuilder sb = readHtml();
//		System.out.println(sb);
		indexTest();
	}

	public static StringBuilder readHtml(){
		String path = "E:\\test\\hetritrix\\output\\sina\\16\\mirror\\news.sina.com.cn\\c\\nd\\2017-09-29\\doc-ifymksyw4958687.shtml";
		File file = new File(path);
		BufferedReader br = null;
		String line = null;
		StringBuilder sb = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
			sb = new StringBuilder();
			while((line = br.readLine())!=null){
				sb.append(line);
				sb.append("\r\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return sb;  
	}
	
	public static void indexTest(){
		String p = "E:\\test\\hetritrix\\output\\sina\\18\\mirror\\news.sina.com.cn\\index.html";
		String index = p.substring(p.lastIndexOf("\\")+1,p.lastIndexOf("."));
		if(index.equals("index")){
			System.out.println("true");
		}
	}

}
