package com.myCrawl.HTMLExtractor;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class FileHandle {


	/**
	 * @Description: 计算文件大小
	 * @param:
	 * @return:
	 * @date: 2017-11-12  
	 */
	public static double calFileSize(File file){
		if (file.exists()) {  
			if (!file.isDirectory()) {     
				return (double) file.length();
			} 
		}
		return 0;		
	}



	/**
	 * @Description: 计算文件夹大小
	 * @param:
	 * @return:
	 * @date: 2017-11-12  
	 */
	public static double getDirSize(File file) {     
		//判断文件是否存在     
		if (file.exists()) {     
			//如果是目录则递归计算其内容的总大小    
			if (file.isDirectory()) {     
				File[] children = file.listFiles();     
				double size = 0;     
				for (File f : children)     
					size += getDirSize(f);     
				return size;     
			} else {//如果是文件则直接返回其大小,以“兆”为单位   
				double size = (double) file.length();        
				return size;     
			}     
		} else {     
			System.out.println("文件或者文件夹不存在，请检查路径是否正确！");     
			return 0.0;     
		}     
	}


	/**
	 * @Description: 获取文件夹内，最大文件名称
	 * @param:
	 * @return:
	 * @date: 2017-11-12  
	 */
	public static String getMaxFileName(File file){
		Map<File,Double> fileSize = new HashMap<>();	
		//判断文件是否存在     
		if (file.exists()) {     
			//如果是目录则递归计算其内容的总大小    
			if (file.isDirectory()) {     
				File[] children = file.listFiles();        
				for(int i=0;i<children.length;i++){
					fileSize.put(children[i], 0.0);
				}
			}
		}else {     
			System.out.println("文件或者文件夹不存在，请检查路径是否正确！");      
		}  
		double max = 0.0;
		File keyName = null;
		for(File key : fileSize.keySet()){
			if(max < getDirSize(key)){
				max = getDirSize(key);
				keyName = key;
			}
		}       
		return keyName.getName();	
	}


	private static int count;

	/**
	 * @Description: 删除指定后缀名称文件
	 * 遍历文件夹中所有文件，获取名称，删除指定名称或者指定后缀文件
	 * @param:
	 * @return:
	 * @date: 2017-11-12  
	 */
	public static int filterFile(File file){
		//判断文件是否存在     
		if (file.exists()) {     
			//如果是目录则递归计算其内容的总大小    
			if (file.isDirectory()) {     
				File[] children = file.listFiles();        
				for (File f : children){
					filterFile(f);
				}
			}else {//如果是文件则直接返回其大小,以“兆”为单位   
				if(suffixDelFile(file)||calFileSize(file)<5000){
					file.delete();
					count++;
				}    
			}      
		} else {     
			System.out.println("文件或者文件夹不存在，请检查路径是否正确！");       
		}  
		return count;
	}


	/**
	 * @Description: 删除0kB的文件夹
	 * @param:
	 * @return:
	 * @date: 2017-11-13  
	 */
	public static int deleteZeroSize(File file){
		if (file.exists()) {     
			//如果是目录则递归计算其内容的总大小    
			if (file.isDirectory()) {     
				File[] children = file.listFiles();        
				for (File f : children){
					deleteZeroSize(f);
				}			
			}     
		}
		if (file.exists()&&file.length()==0) {
			file.delete();//删除文件夹 
			count++;
		} 
		return count;
	}




	/**
	 * @Description: 判断需要删除的文件类型
	 * @param:
	 * @return:
	 * @date: 2017-11-12  
	 */
	public static boolean suffixDelFile(File file){
		if(file.getName().contains("index")||file.getName().contains("default")
				||file.getName().contains("gif") || file.getName().contains(".php") 
				||file.getName().endsWith(".jpeg") || file.getName().contains(".ico")
				|| file.getName().endsWith(".jpg") || file.getName().endsWith(".JPEG")
				|| file.getName().endsWith(".JPG") || file.getName().endsWith(".JSP")
				|| file.getName().endsWith(".jsp") || file.getName().endsWith(".ppt")
				|| file.getName().endsWith(".pptx") || file.getName().endsWith(".gif")
				|| file.getName().endsWith(".css") || file.getName().endsWith(".doc")
				|| file.getName().endsWith(".docx") || file.getName().endsWith(".zip")
				|| file.getName().endsWith(".png") || file.getName().endsWith(".js")
				|| file.getName().endsWith(".swf") || file.getName().endsWith(".xml")
				|| file.getName().endsWith(".xlsx") || file.getName().endsWith(".pdf")
				|| file.getName().endsWith(".xls") || file.getName().endsWith(".rar")
				|| file.getName().endsWith(".exe") || file.getName().endsWith(".txt")
				|| file.getName().endsWith(".mp4") || file.getName().endsWith(".wmv")
				|| file.getName().endsWith(".mp3") || file.getName().endsWith(".flv")
				|| file.getName().endsWith(".mpg") || file.getName().endsWith(".action")
				|| file.getName().endsWith(".aspx") || file.getName().endsWith("3")
				|| file.getName().endsWith("4") || file.getName().endsWith("5")
				|| file.getName().endsWith("6") || file.getName().endsWith("7")
				|| file.getName().endsWith("8") || file.getName().endsWith("9")
				|| file.getName().endsWith("0") || file.getName().contains("mailto") 
				||file.getName().contains("b2b")||file.getName().contains("info")){
			return true;
		} 
		return false;		
	}


	private static String beforeGenRecoverGZ(){
		BloomFilter_utils boom = new BloomFilter_utils(2000,24);
		String RECOVERGZ1 = "F:\\data\\test\\txt\\coon.txt";
		String RECOVERGZ2 = "F:\\data\\test\\txt\\cnoocGZ.txt";
		RandomAccessFile br = null;
		BufferedWriter bw = null;
		String url = null;	
		try {
			//先读取RECOVERGZ_All中所有的url，建立BloomFliter（之前历史url）
			br = new RandomAccessFile(new File(RECOVERGZ1),"r");
			bw = new BufferedWriter(new FileWriter(RECOVERGZ2,true));
			while((url = br.readLine())!=null){
				//建立BloomFliter
				if(!boom.contains(url)){
					boom.add(url);	
					bw.write(url);
					bw.newLine();
					bw.flush();
				}
			}
			br.close();
			bw.close();
			return RECOVERGZ1;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(br!=null){
					br.close();
				}
				if(bw!=null){
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}



	/**
	 * @Description: 获取文件编码格式
	 * @param:
	 * @return:
	 * @date: 2017-11-13  
	 */
	public static void getTxtEncoding(String path){
		File file = new File(path);
		byte[] b;
		try {
			InputStream in= new java.io.FileInputStream(file);
			b = new byte[3];
			in.read(b);
			in.close();
			if (b[0] == -17 && b[1] == -69 && b[2] == -65)
				System.out.println(file.getName() + "：编码为UTF-8");
			else
				System.out.println(file.getName() + "：可能是GBK，也可能是其他编码");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/** 
	 * 利用第三方开源包cpdetector获取文件编码格式 
	 *  
	 * @param path 
	 *            要判断文件编码格式的源文件的路径 
	 * @author huanglei 
	 * @version 2012-7-12 14:05 
	 */  
	public static String getFileEncode(String path) {  
	    /* 
	     * detector是探测器，它把探测任务交给具体的探测实现类的实例完成。 
	     * cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法 加进来，如ParsingDetector、 
	     * JChardetFacade、ASCIIDetector、UnicodeDetector。 
	     * detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的 
	     * 字符集编码。使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和cpdetector.jar 
	     * cpDetector是基于统计学原理的，不保证完全正确。 
	     */  
	    CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();  
	    /* 
	     * ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于 
	     * 指示是否显示探测过程的详细信息，为false不显示。 
	     */  
	    detector.add(new ParsingDetector(false));  
	    /* 
	     * JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码 
	     * 测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以 
	     * 再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。 
	     */  
	    detector.add(JChardetFacade.getInstance());// 用到antlr.jar、chardet.jar  
	    // ASCIIDetector用于ASCII编码测定  
	    detector.add(ASCIIDetector.getInstance());  
	    // UnicodeDetector用于Unicode家族编码的测定  
	    detector.add(UnicodeDetector.getInstance());  
	    java.nio.charset.Charset charset = null;  
	    File f = new File(path);  
	    try {  
	        charset = detector.detectCodepage(f.toURI().toURL());  
	    } catch (Exception ex) {  
	        ex.printStackTrace();  
	    }  
	    if (charset != null)  
	        return charset.name();  
	    else  
	        return null;  
	}  


	/**
	 * @Description:爬取文件夹的处理
	 * 1 过滤文件夹中的文件，过滤规则  ：a、删除指定后缀或者名称文件（.jpg、.docx、.flv、index.html）;
	 *                       b、删除小于5kb的文件(无效文件);
	 *                       c、删除0kB的文件夹(加快抽取的速度);
	 * 2 采用文本标签路径比进行提取（无法提取到指定的全部信息，则跳过该条信息）。
	 * @param:
	 * @return:
	 * @date: 2017-11-13  
	 */
	public static void main(String[] args) {
		//		beforeGenRecoverGZ();
//		String filePath = "G:\\data\\e430\\hetritrix\\output\\cnooc\\1\\mirror";
//		System.out.println("name is   "+filterFile(new File(filePath)));
//		System.out.println("Delete file:   "+deleteZeroSize(new File(filePath)));
		
		String filePath = "F:\\data\\test\\001532254.shtml";
//		getTxtEncoding(filePath);
		System.out.println(getFileEncode(filePath));
	}
}
