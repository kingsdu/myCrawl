package com.myCrawl.WebCollector;

import java.util.ArrayList;
import org.jsoup.nodes.Element;

public class My_CountInfo {

	int textCount = 0;
	int linkTextCount = 0;
	int tagCount = 0;
	int linkTagCount = 0;
	double density = 0;
	double densitySum = 0;
	double score = 0;
	int pCount = 0;
	int punctuation = 0;//标点符号的个数
	int strongCount = 0;//strong标签
	int imageCount = 0;//image个数
	double var = 0;//叶子节点方差
	Element tag = null;
	ArrayList<Integer> leafList = new ArrayList<Integer>();
	@Override
	public String toString() {
		return "My_CountInfo [textCount=" + textCount + ", linkTextCount="
				+ linkTextCount + ", tagCount=" + tagCount + ", linkTagCount="
				+ linkTagCount + ", density=" + density + ", densitySum="
				+ densitySum + ", score=" + score + ", pCount=" + pCount
				+ ", punctuation=" + punctuation + ", strongCount="
				+ strongCount + ", imageCount=" + imageCount + ", var=" + var
				+ ", tag=" + tag + ", leafList=" + leafList + "]";
	}
	
	
}
