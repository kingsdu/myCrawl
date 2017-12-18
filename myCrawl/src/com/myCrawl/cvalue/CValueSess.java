package com.myCrawl.cvalue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ICTCLAS.I3S.AC.ITCTCLAS;

/**
 * C-Value session.
 * 
 * Usage:
 * 
 * cvalSess = new CValueSess();
 * 
 * for (String filterMatch : corpus.filter()) {
 *     cvalSess.observe(filterMatch);
 * }
 * 
 * cvalSess.calculate();
 * 
 * Now there's a collection of Candidate instances with C-Value ready.
 * 
 * candList = new ArrayList<Candidate>(cvalSess.getCandidates());
 * Collections.sort(candList, new CValueComparator());
 * 
 * for (Candidate cand : candList) {
 *     System.out.println(cand.getString() + " " + cand.getCValue());
 * }
 */
public class CValueSess {
	private HashMap<String, Candidate> candidates;//<基词,候选词>

	public CValueSess() {
		candidates = new HashMap<String, Candidate>();
	}

	/**
	 * @Description: 某词组candStr是否被记录
	 * @param:
	 * @return:
	 * @date: 2017-12-14  
	 */
	public void observe(String candStr) {
		Candidate candidate;
		if ((candidate = candidates.get(candStr)) == null) {
			candidate = new Candidate(candStr);
			candidates.put(candStr, candidate);
		}
		candidate.observe();
	}

	public Collection<Candidate> getCandidates() {
		return candidates.values();//所有基串
	}



	/**
	 * @Description: 统计所有词计算c_value需要的属性（1 是否属于嵌套词   2 词频    3 等等）
	 * 1 遍历所有基词（已经统计过词频）
	 * 2 获取基词的子串
	 * 3 遍历基词子串，与基词表中所有词进行匹配，更新基词表中词信息（1 是否属于嵌套词   2 词频    3 等等）
	 * 4 统计完成，计算c-value
	 * @param:
	 * @return:
	 * @date: 2017-12-15  
	 */
	public void calculate() {
		Collection<Candidate> cands = this.getCandidates();//获取所有基串
		List<Candidate> candList = new ArrayList<Candidate>(cands);
		Collections.sort(candList, new CValueLenFreqComparator());//根据所有基串的长度、词频排序
		Collections.reverse(candList);//降序
		Candidate hit;

		for (Candidate cand : candList) {//所有基串
			Collection<String> substrings = cand.getSubstrings();//获取当前基串的所有子串
			//有子串才进行后面计算 ，没有子串则不需要下面计算
			for (String substr : substrings) {//所有子串
				//判读基串中是否有子串，若有，则代码基串为嵌套词串，否则为非嵌套词串
				if ((hit = candidates.get(substr)) != null) {
					hit.observeNested();
					hit.incrementFreqNested(cand.getFrequency());
				}
			} 
		}
	}
	
	
	
	
	
	
	
}
