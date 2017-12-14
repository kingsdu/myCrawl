package com.myCrawl.utils;

import org.apache.log4j.Logger;

public class LogUtils {

	private static Logger logger = Logger.getLogger(LogUtils.class);  
	/**
	 * @Description: TODO
	 * @param:
	 * @return:
	 * @date: 2017-11-1  
	 */
	public static void main(String[] args) {
		double a = 0.0;
		double b = 1.0;
		double c = a/b;
        // 记录info级别的信息  
        logger.info(c);  
	}
	
	

}
