package com.myCrawl.WebCollector;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

public class My_ContentExtractor {

	protected Document doc;

	public static ArrayList<My_CountInfo>sortCount = new ArrayList<>();

	protected HashMap<Element, My_CountInfo> infoMap = new HashMap<Element, My_CountInfo>();

	protected static List<String> linkList = new ArrayList<>();

	My_ContentExtractor(Document doc) {
		this.doc = doc;
	}


	protected void clean() {
		doc.select("script,noscript,style,iframe,br").remove();
		//		System.out.println(doc.body());
	}

	public Element getContentElement() throws Exception {
		clean();
		computeInfo(doc.body());
		double maxScore = 0;
		Element content = null;
		for (Map.Entry<Element, My_CountInfo> entry : infoMap.entrySet()) {
			Element tag = entry.getKey();
			if (tag.tagName().equals("a") || tag == doc.body()) {
				continue;
			}
			double score = computeScore(tag);
			if (score > maxScore) {
				maxScore = score;
				content = tag;
			}
		}
		sortCountInfo(sortCount,true);
		//二次抽取
		if(!meetsRules()){
			for(int i=0;i<5;i++){
				if(sortCount.get(i).punctuation>2 && sortCount.get(i).linkTextCount<10){
					return sortCount.get(i).tag;
				}
			}
			return null;
		}
		//		if((sortCount.get(0).textCount+sortCount.get(0).linkTextCount)<250){
		//			computeScoreSecond(sortCount);
		//			return null;
		//		}
		//		sortCountInfo(sortCount,true);
		return content;
	}



	/**
	 * 方法一抽取出来的内容是否符合要求
	 * 
	 * */
	public Boolean meetsRules(){
		//1、排名第一模块标点<3   2、a标签中无内容
		if((sortCount.get(0).punctuation>3) && (sortCount.get(0).linkTextCount<10)){
			return true;
		}
		return false;		
	}


	//	double score = Math.log(var) * countInfo.densitySum * Math.log(countInfo.textCount - countInfo.linkTextCount + 1) * Math.log10(countInfo.pCount + 2);
	protected My_CountInfo computeInfo(Node node) {	
		//节点是元素
		if (node instanceof Element) {
			Element tag = (Element) node;

			My_CountInfo countInfo = new My_CountInfo();
			for (Node childNode : tag.childNodes()) {
				My_CountInfo childCountInfo = computeInfo(childNode);
				countInfo.textCount += childCountInfo.textCount;//文本信息的长度
				countInfo.linkTextCount += childCountInfo.linkTextCount;//超链接文本信息节点（1 a标签中文本信息长度 ）
				countInfo.tagCount += childCountInfo.tagCount;//所有标签节点个数
				countInfo.linkTagCount += childCountInfo.linkTagCount;//<a>链接的个数
				countInfo.leafList.addAll(childCountInfo.leafList);//叶子节点信息(文本信息)的长度
				countInfo.densitySum += childCountInfo.density;//文本路径标签比
				countInfo.pCount += childCountInfo.pCount;//p标签节点
				countInfo.punctuation += childCountInfo.punctuation;//标点个数
				countInfo.strongCount += childCountInfo.strongCount;//strong个数
				countInfo.imageCount += childCountInfo.imageCount;//image个数
			}
			countInfo.tagCount++;
			String tagName = tag.tagName();
			if (tagName.equals("a")) {
				countInfo.linkTextCount = countInfo.textCount;
				countInfo.linkTagCount++;
				//				linkList.add(tag.attr("href"));
			} else if (tagName.equals("p")) {
				countInfo.pCount++;
			} else if (tagName.equals("strong")){
				countInfo.strongCount++;
			} else if(tagName.equals("img")){
				countInfo.imageCount++;
			}

			int pureLen = countInfo.textCount - countInfo.linkTextCount;//文本信息的长度-a标签中文本信息长度
			int len = countInfo.tagCount - countInfo.linkTagCount;//所有标签节点个数-<a>链接的个数
			if (pureLen == 0 || len == 0) {
				countInfo.density = 0;
			} else {
				countInfo.density = (pureLen + 0.0) / len;//文本标签路径比=可达文本节点的文本长度之和/标签路径数
			}

			infoMap.put(tag, countInfo);
			return countInfo;
		} else if (node instanceof TextNode) {//节点是文本
			TextNode tn = (TextNode) node;
			My_CountInfo countInfo = new My_CountInfo();
			String text = tn.text();
			int pLen = calPunctuation(text);
			countInfo.punctuation = pLen;//标点个数
			int len = text.length();
			countInfo.textCount = len;
			countInfo.leafList.add(len);
			return countInfo;
		} else {
			return new My_CountInfo();
		}
	}



	/**
	 * @Description: 计算文本中标点符号个数
	 * @return:
	 * @date: 2017-10-25  
	 */
	protected int calPunctuation(String text){	
		String str = text.replaceAll("[\\,，\\。\\？?\\!！\\、\\；;\\‘’]", "^");
		if(str.contains("^")){
			if(str.subSequence(str.lastIndexOf("^"), str.length()).equals("^")){
				String s [] = str.split("\\^");
				return s.length;
			}else{
				String s [] = str.split("\\^");
				return s.length-1;
			}
		}
		return 0;
	}






	protected double computeScore(Element tag) {
		My_CountInfo countInfo = infoMap.get(tag);
		double var = Math.sqrt(computeVar(countInfo.leafList) + 1);//计算叶子节点方差
		double score = Math.log(var) * countInfo.densitySum * Math.log(countInfo.textCount - countInfo.linkTextCount + 1) * Math.log10(countInfo.pCount + 2);
		//		double score = Math.log(var) * countInfo.densitySum * Math.log(countInfo.textCount - countInfo.linkTextCount + 1) * Math.log10(countInfo.pCount + 2)* (countInfo.strongCount+1);
		//score = log(叶子节点方差) * 所有文本密度之和     * log(文本标签路径比值) * log10(<p>个数+2)          *Math.log10(countInfo.punctuation)*(countInfo.strongCount+1)
		//score越高 = 叶子节点文本数差异越大  * 所有文本密度之和越大 * 文本字数总和越大 * <p>个数越多
		countInfo.score = score;
		countInfo.tag = tag;
		sortCount.add(countInfo);
		//		System.out.println(countInfo.toString());
		return score;
	}
	//

	/**
	 * @Description: 根据score排序
	 * @return:
	 * @date: 2017-10-25  
	 */
	protected void sortCountInfo(ArrayList<My_CountInfo> sortCount,boolean flag){
		Collections.sort(sortCount, new Comparator<My_CountInfo>() {
			@Override//降序排列
			public int compare(My_CountInfo o1, My_CountInfo o2) {
				Integer score1 = (int) o1.score;
				Integer score2 = (int) o2.score;
				return score2.compareTo(score1);// 正确的方式  
			}
		});
		if(flag){
			writeHtml_sort(sortCount);
		}
	}



	protected void computeScoreSecond(ArrayList<My_CountInfo> sortCount){
		ArrayList<My_CountInfo> secondSortCount = new ArrayList<My_CountInfo>();
		for (int i = 0; i < sortCount.size(); i++) {
			if(sortCount.get(i).textCount<200){
				double var = Math.sqrt(computeVar(sortCount.get(i).leafList) + 1);//计算叶子节点方差
				sortCount.get(i).var = var;
				double score = Math.log(var) * sortCount.get(i).densitySum * Math.log(sortCount.get(i).textCount - sortCount.get(i).linkTextCount + 1) * Math.log10(sortCount.get(i).pCount + 2)*(sortCount.get(i).imageCount+1)*(sortCount.get(i).punctuation)*(sortCount.get(i).strongCount+1);
				sortCount.get(i).score = score;
				secondSortCount.add(sortCount.get(i));
			}
		}
		sortCountInfo(secondSortCount,true);
	}


	/**
	 * @Description: 写出节点的排序结果
	 * @return:
	 * @date: 2017-10-25  
	 */
	public static void writeHtml_sort(ArrayList<My_CountInfo> sortCount){
		String path = "F:\\data\\test\\sort1.html";		
		BufferedWriter bw = null;
		try {
			bw	= new BufferedWriter(new FileWriter(new File(path)));
			for (int i = 0; i < sortCount.size(); i++) {
				bw.write(sortCount.get(i).toString());
				bw.newLine();
				bw.write("======================================");
				bw.newLine();
				bw.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bw!=null){
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}



	public static void writeHtml(Object node){
		String path = "E:\\test\\remove1.html";		
		try {
			FileOutputStream outStream = new FileOutputStream(new File(path));
			outStream.write(node.toString().getBytes());
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	protected double computeVar(ArrayList<Integer> data) {
		if (data.size() == 0) {
			return 0;
		}
		if (data.size() == 1) {
			return data.get(0) / 2;
		}
		double sum = 0;
		//总和
		for (Integer i : data) {
			sum += i;
		}
		double ave = sum / data.size();//平均值
		sum = 0;
		for (Integer i : data) {
			sum += (i - ave) * (i - ave);
		}
		sum = sum / data.size();
		return sum;
	}



	/**
	 * @Description: 读取html文档内容，作为数据源
	 * @return:
	 * @date: 2017-9-27  
	 */
	public static String readHtml(){
		String path = "E:\\test\\hetritrix\\output\\sina\\18\\mirror\\news.sina.com.cn\\c\\2017-09-30\\doc-ifymksyw5133862.shtml";
		File file = new File(path);
		Long fileLengthLong = file.length();  
		byte[] fileContent = new byte[fileLengthLong.intValue()];  
		try {
			FileInputStream inputStream = new FileInputStream(file);  
			inputStream.read(fileContent);  
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return new String(fileContent);  
	}


	public static StringBuilder readHtml1(String path){
		//		String path = "E:\\test\\hetritrix\\output\\sina\\18\\mirror\\news.sina.com.cn\\c\\2017-09-30\\doc-ifymksyw5133862.shtml";
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





	public void getNews(){
		String url = "http://news.sina.com.cn/o/2017-09-27/doc-ifymksyw4372976.shtml";
		String html = readHtml();
		try {
			Document doc = Jsoup.parse(html, url);
			Element contentElement;

			contentElement = getContentElement();
			System.out.println(contentElement);
		} catch (Exception e) {
			e.printStackTrace();
		}     
	}


	protected String getTime(Element contentElement) throws Exception {
		String regex = "([1-2][0-9]{3})[^0-9]{1,5}?([0-1]?[0-9])[^0-9]{1,5}?([0-9]{1,2})[^0-9]{1,5}?([0-2]?[1-9])[^0-9]{1,5}?([0-9]{1,2})[^0-9]{1,5}?([0-9]{1,2})";
		Pattern pattern = Pattern.compile(regex);
		Element current = contentElement;
		for (int i = 0; i < 2; i++) {
			if (current != null && current != doc.body()) {
				Element parent = current.parent();
				if (parent != null) {
					current = parent;
				}
			}
		}
		for (int i = 0; i < 6; i++) {
			if (current == null) {
				break;
			}
			String currentHtml = current.outerHtml();
			Matcher matcher = pattern.matcher(currentHtml);
			if (matcher.find()) {//年份和时间信息
				return matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3) + " " + matcher.group(4) + ":" + matcher.group(5) + ":" + matcher.group(6);
			}
			if (current != doc.body()) {
				current = current.parent();
			}
		}

		try {
			return getDate(contentElement);
		} catch (Exception ex) {
			throw new Exception("time not found");
		}

	}

	//仅仅含有年份信息
	protected String getDate(Element contentElement) throws Exception {
		String regex = "([1-2][0-9]{3})[^0-9]{1,5}?([0-1]?[0-9])[^0-9]{1,5}?([0-9]{1,2})";
		Pattern pattern = Pattern.compile(regex);
		Element current = contentElement;
		for (int i = 0; i < 2; i++) {
			if (current != null && current != doc.body()) {
				Element parent = current.parent();
				if (parent != null) {
					current = parent;
				}
			}
		}
		for (int i = 0; i < 6; i++) {
			if (current == null) {
				break;
			}
			String currentHtml = current.outerHtml();
			Matcher matcher = pattern.matcher(currentHtml);
			if (matcher.find()) {
				return matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3);
			}
			if (current != doc.body()) {
				current = current.parent();
			}
		}
		throw new Exception("date not found");
	}



	protected String getTitle(final Element contentElement) throws Exception {
		final ArrayList<Element> titleList = new ArrayList<Element>();
		final ArrayList<Double> titleSim = new ArrayList<Double>();
		final AtomicInteger contentIndex = new AtomicInteger();
		final String metaTitle = doc.title().trim();
		if (!metaTitle.isEmpty()) {
			doc.body().traverse(new NodeVisitor() {
				@Override
				public void head(Node node, int i) {
					if (node instanceof Element) {
						Element tag = (Element) node;
						if (tag == contentElement) {
							contentIndex.set(titleList.size());
							return;
						}
						String tagName = tag.tagName();
						//抽取出html中h1-h6的标签，计算其中文字和title之间的相似度
						if (Pattern.matches("h[1-6]", tagName)) {
							String title = tag.text().trim();
							double sim = strSim(title, metaTitle);//计算两个字符串之间的相似度
							titleSim.add(sim);
							titleList.add(tag);
						}
					}
				}
				@Override
				public void tail(Node node, int i) {
				}
			});
			//取出html中h1-h6的标签中的相似度，并计算其中相似度最大的文本作为标题
			int index = contentIndex.get();
			if (index > 0) {
				double maxScore = 0;
				int maxIndex = -1;
				for (int i = 0; i < index; i++) {
					double score = (i + 1) * titleSim.get(i);
					if (score > maxScore) {
						maxScore = score;
						maxIndex = i;
					}
				}
				if (maxIndex != -1) {
					return titleList.get(maxIndex).text();
				}
			}
		}
		//若metaTitle无信息，抽取出html中的title几乎所有包含title的部分，将其中的第一个作为标题
		Elements titles = doc.body().select("*[id^=title],*[id$=title],*[class^=title],*[class$=title]");
		if (titles.size() > 0) {
			String title = titles.first().text();
			if (title.length() > 5 && title.length()<40) {
				return titles.first().text();
			}
		}
		try {
			return getTitleByEditDistance(contentElement);
		} catch (Exception ex) {
			throw new Exception("title not found");
		}
	}


	/**
	 * @Description:将HTML中所有的文本和metaTitle计算字符串相似度，取出最相似的作为标题
	 * @return:
	 * @date: 2017-9-29  
	 */
	protected String getTitleByEditDistance(Element contentElement) throws Exception {
		final String metaTitle = doc.title();

		final ArrayList<Double> max = new ArrayList<Double>();
		max.add(0.0);
		final StringBuilder sb = new StringBuilder();
		doc.body().traverse(new NodeVisitor() {

			public void head(Node node, int i) {

				if (node instanceof TextNode) {
					TextNode tn = (TextNode) node;
					String text = tn.text().trim();
					double sim = strSim(text, metaTitle);
					if (sim > 0) {
						if (sim > max.get(0)) {
							max.set(0, sim);
							sb.setLength(0);
							sb.append(text);
						}
					}

				}
			}

			public void tail(Node node, int i) {
			}
		});
		if (sb.length() > 0) {
			return sb.toString();
		}
		throw new Exception();

	}




	protected double strSim(String a, String b) {
		int len1 = a.length();
		int len2 = b.length();
		if (len1 == 0 || len2 == 0) {
			return 0;
		}
		double ratio;
		if (len1 > len2) {
			ratio = (len1 + 0.0) / len2;
		} else {
			ratio = (len2 + 0.0) / len1;
		}
		if (ratio >= 3) {
			return 0;
		}
		return (lcs(a, b) + 0.0) / Math.max(len1, len2);
	}



	protected int lcs(String x, String y) {

		int M = x.length();
		int N = y.length();
		if (M == 0 || N == 0) {
			return 0;
		}
		int[][] opt = new int[M + 1][N + 1];

		for (int i = M - 1; i >= 0; i--) {
			for (int j = N - 1; j >= 0; j--) {
				if (x.charAt(i) == y.charAt(j)) {
					opt[i][j] = opt[i + 1][j + 1] + 1;
				} else {
					opt[i][j] = Math.max(opt[i + 1][j], opt[i][j + 1]);
				}
			}
		}

		return opt[0][0];
	}


	public static void writeList(List<String> aList,String path){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path),true)));
			bw.newLine();
			bw.write("+++++++++++++++++++++++++++++++++++++++++++");
			bw.newLine();
			for(int i=0;i<aList.size();i++){				
				bw.write(aList.get(i));
				bw.newLine();
				bw.flush();
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static String getDomain(){
		return null;
		//		HttpServletRequest httpRequest=(HttpServletRequest)request;  
		//  
		//		String strBackUrl = "http://" + request.getServerName() //服务器地址  
		//		                      
		//		+ ":"   
		//		                      
		//		+ request.getServerPort()//端口号  
		//		                      
		//		+ httpRequest.getContextPath()      //项目名称  
		//		                     
		//		 + httpRequest.getServletPath()      //请求页面或其他地址  
		//		                 
		//		 + "?" + (httpRequest.getQueryString()); //参数  
	}


	public static void main(String args[]){
		String url = "http://www.sinopecnews.com.cn/news/content/2012-06/26/content_1189270.shtml";
		String path = "F:\\data\\test\\sinop\\content_1179622.shtml";
		StringBuilder html = readHtml1(path);
		//		writeHtml(html.toString());
		Document doc = Jsoup.parse(html.toString(), url);
		My_ContentExtractor m = new My_ContentExtractor(doc);
		Element contentElement = null;
		try {
			contentElement = m.getContentElement();
			//			writeList(linkList,"F:\\data\\test\\listOut.txt");
			//			String title = m.getTitle(contentElement);
			//			String time = m.getTime(contentElement);
			System.out.println(contentElement);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//		int a = calPunctuation_t(" 中国石油化工集团，公司版权所有　未经授权，禁止复制或建立镜像　　　 　广电经营许可证（广媒）字第180号 信息网络传播视听节目许可证：0110459号");
		//		System.out.println(a);
	}



	public static int calPunctuation_t(String text){
		String str = text.replaceAll("[\\,，\\。\\？?\\!！\\、\\；;\\“”\\'‘’]", "^");
		if(str.contains("^")){
			if(str.subSequence(str.lastIndexOf("^"), str.length()).equals("^")){
				String s [] = str.split("\\^");
				return s.length;
			}else{
				String s [] = str.split("\\^");
				return s.length-1;
			}
		}
		return 0;
	}






}
