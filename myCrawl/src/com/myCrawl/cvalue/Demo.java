package com.myCrawl.cvalue;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Demo {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//读取规则抽取关键词
		List<String> words = CvalueUtils.readDicWords("C:\\resource\\行业网站\\crawlData\\oilGas\\中石化新闻网\\1\\mirror\\filterwords");
		CValueSess cvals;
		ArrayList<Candidate> candList;

		cvals = new CValueSess();

		for (String phrase : words) {
			cvals.observe(phrase);
		}

		cvals.calculate();

		candList = new ArrayList<Candidate>(cvals.getCandidates());
		Collections.sort(candList, new CValueComparator());
		Collections.reverse(candList);//降序
		
		//写出结果
		String path = "C:\\resource\\行业网站\\crawlData\\oilGas\\中石化新闻网\\1\\mirror\\cvalue\\allResult.txt";
		BufferedWriter bw = null;
		try {
			bw =  new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path)),"utf-8"));
			for (Candidate cand : candList) {
//				String resultLine = 
//						cand.getLength() + " " + 
//								cand.getFrequency() + "  " + 
//								cand.getNesterCount() + " " +
//								cand.getFreqNested() + " " +
//								cand.getString() + " " +
//								cand.getCValue();
				String writeLine = 
								cand.getString() + " " +
								cand.getCValue();
//				System.out.println(resultLine);
				bw.write(writeLine);
				bw.newLine();
				bw.flush();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(bw != null){
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

