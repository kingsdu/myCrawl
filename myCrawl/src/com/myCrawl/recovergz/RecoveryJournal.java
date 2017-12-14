package com.myCrawl.recovergz;

import it.unimi.dsi.mg4j.util.MutableString;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Writer;


public class RecoveryJournal extends CrawlerJournal{

	public final static String F_ADD = "F+ ";
	public final static String F_EMIT = "Fe ";
	public final static String F_DISREGARD = "Fd ";
	public final static String F_RESCHEDULE = "Fr ";
	public final static String F_SUCCESS = "Fs ";
	public final static String F_FAILURE = "Ff ";

	
	 //  show recovery progress every this many lines
    private static final int PROGRESS_INTERVAL = 1000000;

    // once this many URIs are queued during recovery, allow 
    // crawl to begin, while enqueuing of other URIs from log
    // continues in background
    private static final long ENOUGH_TO_START_CRAWLING = 100000;
    
    /**
     * Create a new recovery journal at the given location
     * 
     * @param path Directory to make the recovery  journal in.
     * @param filename Name to use for recovery journal file.
     * @throws IOException
     */
    public RecoveryJournal(String path, String filename)
    throws IOException {
    	super(path,filename);
        timestamp_interval = 10000; // write timestamp lines occasionally
    }
    
    
    
    
    /**
     * Read a line from the given bufferedinputstream into the MutableString.
     * Return true if a line was read; false if EOF. 
     * 
     * @param is
     * @param read
     * @return True if we read a line.
     * @throws IOException
     */
    private static boolean readLine(BufferedInputStream is, MutableString read)
    throws IOException {
        read.length(0);
        int c = is.read();
        while((c!=-1)&&c!='\n'&&c!='\r') {
            read.append((char)c);
            c = is.read();
        }
        if(c==-1 && read.length()==0) {
            // EOF and none read; return false
            return false;
        }
        if(c=='\n') {
            // consume LF following CR, if present
            is.mark(1);
            if(is.read()!='\r') {
                is.reset();
            }
        }
        // a line (possibly blank) was read
        return true;
    }
    
    
    
    
    
    
    /**
     * Stream on which we record frontier events.
     */
    protected Writer out = null;
    
    /** line count */ 
    protected long lines = 0;
    /** number of lines between timestamps */ 
    protected int timestamp_interval = 0; // 0 means no timestamps

    
    /** suffix to recognize gzipped files */
    public static final String GZIP_SUFFIX = ".gz";
    
    /**
     * File we're writing journal to.
     * Keep a reference in case we want to rotate it off.
     */
    protected File gzipFile = null;
}
