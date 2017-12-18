package com.myCrawl.cvalue;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ICTCLAS.I3S.AC.ITCTCLAS;

/**
 * @Description: TODO
 * @author: DU 
 * @date: 2017-12-16  
 */
public class CvalueUtils {


	private static List<String> allPath_one;//记录某路径下的所有路径
	/**
	 * @Description: 调用ITCLAS接口分词 
	 * @param:
	 * @return:
	 * @date: 2017-12-16  
	 */
	public static String ITCTILS(String str){
		return ITSplit(str);
	}


	private static String ITSplit(String sInput){      
		return ITCTCLAS.ICTCLAS_ParagraphProcess(sInput);
	}


	/**
	 * @Description: 根据语言规则匹配
	 * @param:
	 * @return:
	 * @date: 2017-12-16  
	 */
	public static void ruleMatch(String rulesPath,String sourcePath,String outputPath){
		try {
			String result = "";
			List<String> rulesList = getRules(rulesPath);
			String sourceString = getContent(sourcePath);
			for(String rule : rulesList){
				result += getContentUseRegex(rule, sourceString, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * 判断字符为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		boolean b = false;
		if(null == str || "".equals(str)){
			b = true;
		}
		return b;
	}




	/**
	 * 读取评价对象抽取的规则
	 * @param inputPath 规则的完整路径
	 * @return
	 * xx/n
	 */
	public static List<String> getRules(String inputPath){
		List<String> result = new ArrayList<String>();
		File file = new File(inputPath); 
		FileReader fr = null;
		BufferedReader br = null;
		String regex = "[\u4e00-\u9fa5a-zA-Z0-9]*";
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String temp = "";
			while((temp=br.readLine()) != null){
				String[] temps = temp.split(ConstantParams.SINGLE_BLANK);
				String str = "";
				for(String rule : temps){
					str += (regex+"/"+rule+"(.?)"+ConstantParams.SINGLE_BLANK);
				}
				result.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(br != null){
					br.close();
				}
				if(fr != null){
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}



	/**
	 * @Description: 获取全部的content的内容
	 * @param:
	 * @return:
	 * @date: 2017-12-16  
	 */
	public static String getContent(String inputPath){
		String encoding = getFileEncode(inputPath);  
		File file = new File(inputPath);  
		Long filelength = file.length();  
		byte[] filecontent = new byte[filelength.intValue()];  
		try {  
			FileInputStream in = new FileInputStream(file);  
			in.read(filecontent);  
			in.close();  
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		try {  
			return new String(filecontent, encoding);  
		} catch (UnsupportedEncodingException e) {  
			System.err.println("The OS does not support " + encoding);  
			e.printStackTrace();  
			return null;  
		}  
	}


	/**
	 * @Description: 根据正则获取匹配出的候选术语
	 * @param:
	 * @return:
	 * @date: 2017-12-16  
	 */
	public static String getContentUseRegex(String regexString,String sourceString,String splitMark){
		String result = "";
		if(isEmpty(regexString) || isEmpty(sourceString)){
			return result;
		}
		if(isEmpty(splitMark)){
			splitMark = ConstantParams.CHENG_LINE;
		}
		try {
			Pattern pattern = Pattern.compile(regexString);
			Matcher matcher = pattern.matcher(sourceString);
			while(matcher.find()){
				result += matcher.group()+splitMark;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
	 * @Description: 读取规则匹配后的候选关键词
	 * @param:
	 * @return:
	 * @date: 2017-12-16  
	 */
	public static List<String> readDicWords(String inputpath){
		getAllPath(inputpath);
		List<String> pageWords = new ArrayList<>();
		BufferedReader br;
		try {
			for(String path : allPath_one){
				br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)),getFileEncode(path)));			
				String word = "";
				while((word = br.readLine())!=null){
					pageWords.add(word);//所有文章的词
				}
			}
			return pageWords;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageWords;
	}



	/**
	 * @Description: 获取某个路径下所有的文件路径,没有递归统计
	 * @param:
	 * @return:
	 * @date: 2017-12-12  
	 */
	public static List<String> getAllPath(String path){
		allPath_one = new ArrayList<>();
		try (DirectoryStream<Path> entries = Files.newDirectoryStream(Paths.get(path))){
			for(Path entry : entries){
				allPath_one.add(entry.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allPath_one;
	}


}
