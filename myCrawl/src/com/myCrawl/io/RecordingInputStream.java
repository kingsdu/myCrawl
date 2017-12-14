package com.myCrawl.io;

import java.io.IOException;
import java.io.InputStream;



public class RecordingInputStream extends InputStream {
	protected InputStream wrappedStream;
	protected RecordingOutputStream recordingOutputStream;

	/**
	 * Create a new RecordingInputStream with the specified parameters.
	 * 
	 * @param bufferSize
	 * @param backingFilename
	 * @param maxSize
	 */
	public RecordingInputStream(int bufferSize, String backingFilename, int maxSize) {
		recordingOutputStream = new RecordingOutputStream(bufferSize, backingFilename, maxSize);
	}
	
	
	public void open(InputStream wrappedStream) throws IOException {
		this.wrappedStream = wrappedStream;
		recordingOutputStream.open(new NullOutputStream()); 
	}


	
	public void markResponseBodyStart() {
		recordingOutputStream.markResponseBodyStart();
	}
	
	public long getResponseContentLength() {
		return recordingOutputStream.getResponseContentLength();
	}
	
	
	public void closeRecorder() throws IOException {
		recordingOutputStream.closeRecorder();
	}
	
	
	public CharSequence getCharSequence() {
		return recordingOutputStream.getCharSequence();
	}
	
	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
