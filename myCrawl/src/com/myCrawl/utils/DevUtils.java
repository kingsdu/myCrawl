package com.myCrawl.utils;

import java.io.PrintWriter;
import java.io.StringWriter;


public class DevUtils {

	public static void warnHandle(Throwable ex, String note) {
		StringWriter sw = new StringWriter();
		sw.write(note);
		sw.write("\n");
		ex.printStackTrace(new PrintWriter(sw));
	}
	
	public static String extraInfo() {
		Thread current = Thread.currentThread();
		return "";
	}
}
