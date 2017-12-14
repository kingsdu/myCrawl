package com.myCrawl.myFrontier;

import java.io.FileNotFoundException;
import java.util.Map.Entry;
import java.util.Set;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.DatabaseException;

public class BDBFrontier extends AbstractFrontier{

	private StoredMap pendingUrisDB = null;
	//使用默认的路径和缓存大小构造函数
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BDBFrontier(String homeDirectory) throws DatabaseException,
	FileNotFoundException{
		super(homeDirectory);
		EntryBinding keyBinding =new SerialBinding
				(javaCatalog,String.class);
		EntryBinding valueBinding =new SerialBinding(javaCatalog,
				CrawlUrl.class);
		pendingUrisDB = new StoredMap(database,keyBinding, valueBinding,
				true);
	}
	
	
	
	//获得下一条记录
	public CrawlUrl getNext() throws Exception {
		CrawlUrl result = null;
		if(!pendingUrisDB.isEmpty()){
			Set entrys = pendingUrisDB.entrySet();
			System.out.println(entrys);
			Entry <String,CrawlUrl> entry = (Entry<String,CrawlUrl>) pendingUrisDB.entrySet().iterator().next();
			result = entry.getValue();
			delete(entry.getKey());
		}
		return result;
	}
	
	
	//存入 URL
	public boolean putUrl(CrawlUrl url){
		put(url.getOriUrl(),url);
		return true;
	}
	
	
	// 存入数据库的方法
	@SuppressWarnings("unchecked")
	protected void put(Object key,Object value) {
		pendingUrisDB.put(key, value);
	}
	//取出
	protected Object get(Object key){
		return pendingUrisDB.get(key);
	}
	//删除
	protected Object delete(Object key){
		return pendingUrisDB.remove(key);
	}
	
	// 根据 URL 计算键值，可以使用各种压缩算法，包括 MD5 等压缩算法
	@SuppressWarnings("unused")
	private String caculateUrl(String url) {
		return url;
	}
	// 测试函数
	public static void main(String[] strs) {
		try {
			BDBFrontier bBDBFrontier = new BDBFrontier("c:\\bdb");
			CrawlUrl url = new CrawlUrl();
			url.setOriUrl("http://www.163.com");
			bBDBFrontier.putUrl(url);
			System.out.println(((CrawlUrl)bBDBFrontier.getNext()).getOriUrl());
			bBDBFrontier.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		}
	}

}
