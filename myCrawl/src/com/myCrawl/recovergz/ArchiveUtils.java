package com.myCrawl.recovergz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ArchiveUtils {

	
    /**
     * Log-style date stamp in the format yyyy-MM-dd'T'HH:mm:ss'Z'
     * UTC time zone is assumed.
     */
    private static final ThreadLocal<SimpleDateFormat>
        TIMESTAMP14ISO8601Z = threadLocalDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /** milliseconds in an hour */ 
    private static final int HOUR_IN_MS = 60 * 60 * 1000;
    /** milliseconds in a day */
    private static final int DAY_IN_MS = 24 * HOUR_IN_MS;
    
    private static ThreadLocal<SimpleDateFormat> threadLocalDateFormat(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<SimpleDateFormat>() {
            protected SimpleDateFormat initialValue() {
                SimpleDateFormat df = new SimpleDateFormat(pattern);
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                return df;
            }
        };
        return tl;
    }
    
    
    
    /**
     * Utility function for creating log timestamps, in
     * W3C/ISO8601 format, assuming UTC. Use current time. 
     * 
     * Format is yyyy-MM-dd'T'HH:mm:ss'Z'
     * 
     * @return the date stamp
     */
    public static String getLog14Date(){
        return TIMESTAMP14ISO8601Z.get().format(new Date());
    }
}
