package com.myCrawl.WebCollector;

import org.jsoup.nodes.Element;

public class My_News {
	protected String url = null;
	protected String title = null;
	protected String content = null;
	protected String time = null;
	
	protected Element contentElement = null;
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
	
}
