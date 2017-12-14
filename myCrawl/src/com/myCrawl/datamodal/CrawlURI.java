package com.myCrawl.datamodal;


import java.util.HashSet;
import java.util.Set;

import st.ata.util.AList;
import st.ata.util.HashtableAList;

public class CrawlURI implements CoreAttributeConstants{

//	/**
//	 * @param uuri
//	 */
//	public CrawlURI(UURI uuri) {
//		super(uuri);
//	}
	
	// flexible dynamic attributes
	private AList alist = new HashtableAList();

	public AList getAlist() {
		return alist;
	}

	public void setAlist(AList alist) {
		this.alist = alist;
	}
	
	
	/**
	 * @param u
	 */
	public void addLink(String u) {
		addToNamedSet(A_HTML_LINKS, u);
	}
	
	
	private void addToNamedSet(String key, Object o) {
		Set s;
		if(!alist.containsKey(key)) {
			s = new HashSet();
			alist.putObject(key, s);
		} else {
			s = (Set)alist.getObject(key);
		}
		s.add(o);
	}
	
	

	/**
	 * @param string
	 */
	public void addSpeculativeEmbed(String string) {
		addToNamedSet(A_HTML_SPECULATIVE_EMBEDS,string);
	}
	
	
	/**
	 * @param u
	 */
	public void addEmbed(String u) {
		addToNamedSet(A_HTML_EMBEDS, u);
	}

		
}
