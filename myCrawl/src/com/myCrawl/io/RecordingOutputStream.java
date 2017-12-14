package com.myCrawl.io;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RecordingOutputStream extends OutputStream{

	protected long size = 0;
	protected int maxSize;
	protected String backingFilename;
	protected BufferedOutputStream diskStream;
	protected FileOutputStream fileStream;
	protected OutputStream wrappedStream;
	protected byte[] buffer;
	protected long position;
	protected long responseBodyStart; // when recording HTTP, where the content-body starts
	
	
	/**
	 * Create a new RecordingPutputStream with the specified parameters.
	 * 根据参数创建一个输入流
	 * 
	 * @param bufferSize
	 * @param backingFilename
	 * @param maxSize
	 */
	public RecordingOutputStream(int bufferSize, String backingFilename, int maxSize) {
		buffer = new byte[bufferSize];
		this.backingFilename = backingFilename;
		this.maxSize = maxSize;
	}
	
	
	public void open(OutputStream wrappedStream) throws IOException {
		this.wrappedStream = wrappedStream;
		this.position = 0;
		this.size=0;
		// locksize=false;
		diskStream=null; 
		lateOpen();
 
	}
	
	
	private void lateOpen() throws FileNotFoundException {
		if(diskStream==null) {
			fileStream = new FileOutputStream(backingFilename);
			diskStream = new BufferedOutputStream(fileStream,4096);
		}
	}

	
	
	public long getResponseContentLength() {
		return size-responseBodyStart;
	}

	
	public void markResponseBodyStart() {
		responseBodyStart = position;
	}

	
	public void closeRecorder() throws IOException {
		// diskStream.flush(); // redundant, close includes flush
		if (diskStream != null) {
			diskStream.close();
			diskStream=null;
		}
//		if(locksize) {
//			System.out.println("gotcha");
//		}
		if(size==0) size = position;
	}

	
	
	public CharSequence getCharSequence() {
		try {
			return new ReplayCharSequence(buffer,size,responseBodyStart,backingFilename);
		} catch (IOException e) {
			// TODO convert to runtime exception?
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void write(int b) throws IOException {
		// TODO Auto-generated method stub
		
	}
	

	
}
