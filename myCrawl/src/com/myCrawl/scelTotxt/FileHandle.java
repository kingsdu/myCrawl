package com.myCrawl.scelTotxt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.mySearch.BloomFilter.BloomFilter_Heritrix;

/**
 * @Description: 对转化完的词典进行去重、合并
 * @author: DU 
 * @date: 2017-12-11  
 */
public class FileHandle {

	public static void main(String[] args) {
//		delRepeat("G:\\TermExtract\\dic\\allDic_words.txt","G:\\TermExtract\\dic\\allDic_word.txt",true);
//		readDic("G:\\TermExtract\\dic\\scel_txt","G:\\TermExtract\\dic\\allDic_word.txt");
		readDic_a("G:\\TermExtract\\dic\\qq\\dic","G:\\TermExtract\\dic\\allDic_word.txt");
	}
	/**
	 * @Description: 去除词典中的重复信息
	 * @param:
	 * @return:
	 * @date: 2017-12-11  
	 */
	public static void delRepeat(String path,String targetPath,boolean isAppend){
		BloomFilter_Heritrix boom = new BloomFilter_Heritrix(2000,24);
		BufferedReader in = null;
		BufferedWriter bw = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
			bw = new BufferedWriter(new FileWriter(targetPath,isAppend));
			String word_a = "";
			while((word_a = in.readLine())!=null){
				if(!word_a.equals("")){
					String[]split = word_a.split(" ");
					String word_b = split[1];
					if(!boom.contains(word_b)){
						boom.add(word_b);	
						bw.write(word_b);
						bw.newLine();
						bw.flush();
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(bw!=null){
					bw.close();
				}
				if(bw!=null){
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}   	
	}
	
	
	
	/**
	 * @Description: 读取前面带拼音的词
	 * @param:fileDirPath 输入文件夹（多个文件在其中） targetPath  输出txt文件
	 * @return:
	 * @date: 2017-12-11  
	 */
	public static void readDic(String fileDirPath,String targetPath){
		BloomFilter_Heritrix boom = new BloomFilter_Heritrix(2000,24);
		File dir = new File(fileDirPath);
		File txts[] = dir.listFiles();  
		for (int i = 0; i < txts.length; i++) {  
			if (!txts[i].getName().endsWith(".txt")) {  
				continue;  
			}  
			BufferedReader in = null;
			BufferedWriter bw = null;
			try {
				in = new BufferedReader(new InputStreamReader(new FileInputStream(txts[i]),"GBK"));
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(targetPath),true), "UTF-8"));
				String word_a = "";
				while((word_a  = in.readLine())!=null){
					if(!word_a.equals("")){
						String[]split = word_a.split(" ");
						String word_b = split[1];
						if(!boom.contains(word_b)){
							boom.add(word_b);	
							bw.write(word_b);
							bw.newLine();
							bw.flush();
						}
					}
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(bw!=null){
						bw.close();
					}
					if(bw!=null){
						bw.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}  
	}
	
	
	
	
	
	
	
	/**
	 * @Description: 读取前面不带拼音的词
	 * @param: fileDirPath 输入文件夹（多个文件在其中） targetPath  输出txt文件
	 * @return:
	 * @date: 2017-12-11  
	 */
	public static void readDic_a(String fileDirPath,String targetPath){
		BloomFilter_Heritrix boom = new BloomFilter_Heritrix(2000,24);
		File dir = new File(fileDirPath);
		File txts[] = dir.listFiles();  
		for (int i = 0; i < txts.length; i++) {  
			if (!txts[i].getName().endsWith(".txt")) {  
				continue;  
			}  
			BufferedReader in = null;
			BufferedWriter bw = null;
			try {
				in = new BufferedReader(new InputStreamReader(new FileInputStream(txts[i]),"UTF-8"));
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(targetPath),true), "UTF-8"));
				String word_a = "";
				while((word_a  = in.readLine())!=null){
					if(!word_a.equals("")){
						if(!boom.contains(word_a)){
							boom.add(word_a);	
							bw.write(word_a);
							bw.newLine();
							bw.flush();
						}
					}
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(in!=null){
						in.close();
					}
					if(bw!=null){
						bw.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}  
	}

}
