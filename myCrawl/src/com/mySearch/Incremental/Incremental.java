package com.mySearch.Incremental;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.myCrawl.recovergz.RecoveryJournal;
import com.mySearch.BloomFilter.BloomFilter_Heritrix;

public class Incremental {

	private static List<String> li = new ArrayList<String>();
	/**
	 * @Description: TODO
	 * @return:
	 * @date: 2017-9-25  
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis(); // 记录起始时间
		//首次爬取
		//1  获取url
//		getURL();
//		//2 BloomFliter
//		bloomFliterURL();
//		//3  生成recover.gz
		genRecoverGZ();
		//非首次爬取
		//1 读取recover.gz

		//2 初始化BloomFliter
//		readGZFile();
		//3更新recover.gz
		long end = System.currentTimeMillis();       // 记录结束时间
		System.out.println(end-start);              // 相减得出运行时间

	}


	/**
	 * @Description: 获取url
	 * @return:
	 * @date: 2017-9-25  
	 */
	public static void getURL(){
		String sourceFilePath = "E:/test/myheritrix_output_config1.txt";
		InputStream is=null;  
		InputStreamReader isr=null;  
		BufferedReader br=null;  
		File sourceFile=null;  
		String line=null; 

		try {
			sourceFile=new File(sourceFilePath); 
			//读取文件内容  
			is=new FileInputStream(sourceFile);  
			isr=new InputStreamReader(is);  
			br=new BufferedReader(isr);

			//一行一行写入recover.gz文件  
			while((line=br.readLine())!=null){  
				if(!line.equals("")){  
					li.add(line);
				}  
			}  	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{  
			try {    
				if(br!=null){  
					br.close();  
				}  
				if(isr!=null){  
					isr.close();  
				}  
				if(is!=null){  
					is.close();  
				}  
			} catch (Exception e) {  
				e.printStackTrace();  
			}  
		}   
	}


	/**
	 * @Description: 过滤URL
	 * @return:
	 * @date: 2017-9-25  
	 */
	public static void bloomFliterURL(){
		BloomFilter_Heritrix boom = new BloomFilter_Heritrix(2000,24);
		for(String url : li){
			if(!boom.contains(url)){
				boom.add(url);	
				writeUrl(url);
			}
		}
	}



	public static void writeUrl(String uri){
		String path = "E:\\test\\myheritrix_bloom.txt";
		BufferedWriter bw = null;
		try {
			if(path!=null){
				File file = new File(path);
				bw = new BufferedWriter(new FileWriter(file,true));
				if(uri!=null){
					bw.append(uri);
					bw.newLine();
					bw.flush();
				}	
			}	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}  		
	}



	/**
	 * @Description: 生成recover.gz
	 * @return:
	 * @date: 2017-9-25  
	 */
	public static boolean genRecoverGZ(){
		String recoverGzFileName = null;
		String recoverGzDir = "E:/test";
		RecoveryJournal recover = null; 

		String line=null; 
		InputStream is=null;  
		InputStreamReader isr=null;  
		BufferedReader br=null;  
		File sourceFile=null; 
		String sourceFileEncoding = "utf-8";

		String sourceFilePath = "E:\\test\\hetritrix\\output\\sina\\6\\recoverAll.txt";

		//recover.gz文件为空则采用默认名字  
		if(recoverGzFileName==null||recoverGzFileName.equals("")){  
			recoverGzFileName="recover.gz";  
		}  
		try {
			sourceFile=new File(sourceFilePath); 
			recover = new RecoveryJournal(recoverGzDir,recoverGzFileName);
			//读取文件内容  
			is=new FileInputStream(sourceFile);  
			isr=new InputStreamReader(is,sourceFileEncoding);  
			br=new BufferedReader(isr);  

			//一行一行写入recover.gz文件  
			while((line=br.readLine())!=null){  
				if(!line.equals("")){  
					recover.writeLine(RecoveryJournal.F_SUCCESS, line);  
				}  
			}  	 	
		} catch (IOException e) {
			e.printStackTrace();
		}finally{  
			try {  
				if(recover!=null){  
					recover.close();  
				}    
			} catch (Exception e) {  
				e.printStackTrace();  
			}  
		}  
		return false;	
	}



	/**
	 * @Description: 读取recoverGZ压缩文件,读取压缩文件存在bug,有信息的丢失，故弃用
	 * @return:
	 * @date: 2017-9-27  
	 */
	public static void readGZFile(){
		String sourceFilePath = "E:/test/hetritrix/output/sina/6/recover.gz";
		File f = new File(sourceFilePath);
		InputStream in = null;
		GZIPInputStream gz = null;
		FileOutputStream fos = null;
		byte[] buf = new byte[1024];
		byte[] a = "+++++++++++++++++++++++++++++++".getBytes();
		try {
			fos = new FileOutputStream("E:/test/hetritrix/output/sina/6/aaa.txt");
			in = new BufferedInputStream(new FileInputStream(f)); 
			gz = new GZIPInputStream(in);
			while(gz.read(buf)!=-1){
				fos.write(buf);
				fos.write(a);
				fos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(fos!=null){
					fos.close();
				}
				if(in!=null){
					in.close();
				}
				if(gz!=null){
					gz.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}


}
