package com.myCrawl.WebCollector;

import cn.edu.hfut.dmic.contentextractor.ContentExtractor;
import cn.edu.hfut.dmic.contentextractor.News;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;

public class NewsCrawler {

	public static void main(String []args){
		demo2();
	}


	public static void demo_01(){
		String url = "http://news.cnpc.com.cn/system/2017/09/27/001663300.shtml";
		News news = null;
		try {
			HttpRequest request = new HttpRequest(url);
			String html = request.getResponse().getHtmlByCharsetDetect();
			news = ContentExtractor.getNewsByHtml(html, url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(news);
	}
	
	//http://news.sina.com.cn/o/2017-09-27/doc-ifymksyw4372976.shtml//11111
    public static void demo2(){
        String url = "http://news.sina.com.cn/o/2017-09-27/doc-ifymksyw4372976.shtml";
        News news = null;
		try {
			news = ContentExtractor.getNewsByUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println(news);
    }
	
    
    
//    /*输入HTML，输出制定网页的正文*/
//    public static void demo3() throws Exception {
//        String url = "http://www.huxiu.com/article/121959/1.html";
//        HttpRequest request = new HttpRequest(url);
//        String html = request.getResponse().getHtmlByCharsetDetect();
//        String content = ContentExtractor.getContentByHtml(html, url);
//        System.out.println(content);
//
//        //也可抽取网页正文所在的Element
//        //Element contentElement = ContentExtractor.getContentElementByHtml(html, url);
//        //System.out.println(contentElement);
//    }

    /*输入URL，输出制定网页的正文*/
    public static void demo4() throws Exception {
        String url = "http://www.huxiu.com/article/121959/1.html";
        String content = ContentExtractor.getContentByUrl(url);
        System.out.println(content);

        //也可抽取网页正文所在的Element
        //Element contentElement = ContentExtractor.getContentElementByUrl(url);
        //System.out.println(contentElement);
    }
}
