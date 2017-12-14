package com.myCrawl.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import com.myCrawl.io.RecordingInputStream;
import com.myCrawl.io.RecordingOutputStream;

public class HttpRecorder {
	private static final int DEFAULT_OUTPUT_BUFFER_SIZE = 4096;
	private static final int DEFAULT_INPUT_BUFFER_SIZE = 65536;
	protected RecordingInputStream ris;
	protected RecordingOutputStream ros;
	
	/**
	 * @param tempDir
	 * @param backingFilenamePrefix
	 * 
	 */
	public HttpRecorder(File tempDir, String backingFilenamePrefix) {
//		super();
		tempDir.mkdirs();
		String tempDirPath = tempDir.getPath()+File.separatorChar;
		ris = new RecordingInputStream(DEFAULT_INPUT_BUFFER_SIZE,tempDirPath+backingFilenamePrefix+".ris",2^20);
		ros = new RecordingOutputStream(DEFAULT_OUTPUT_BUFFER_SIZE,tempDirPath+backingFilenamePrefix+".ros",2^12);
	}

	/**
	 * Wrap the provided stream with the internal RecordingInputStream
	 * 
	 * @param is
	 * @return An inputstream.
	 * @throws IOException
	 */
	public InputStream inputWrap(InputStream is) throws IOException {
		ris.open(is);
		return ris;
	}

	/**
	 * Wrap the provided stream with the internal RecordingOutputStream
	 * @param os
	 * @return An output stream.
	 * @throws IOException
	 */
	public OutputStream outputWrap(OutputStream os) throws IOException {
		ros.open(os);
		return ros;
	}

	/**
	 * Close all streams.
	 */
	public void close() {
		try {
			ris.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			DevUtils.logger.log(Level.SEVERE,"close() ris"+DevUtils.extraInfo(),e);

		}
		try {
			ros.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			DevUtils.logger.log(Level.SEVERE,"close() ros"+DevUtils.extraInfo(),e);
		}
	}

	/**
	 * Return the internal RecordingInputStream
	 * @return A RIS.
	 */
	public RecordingInputStream getRecordedInput() {
		return ris;
	}

	/**
	 * Mark the point where the HTTP headers end. 
	 * 
	 */
	public void markResponseBodyStart() {
		ris.markResponseBodyStart();
	}

	public long getResponseContentLength() {
		return ris.getResponseContentLength();
	}

	/**
	 * 
	 */
	public void closeRecorders() {
		try {
			ris.closeRecorder();
			ros.closeRecorder();
		} catch (IOException e) {
			DevUtils.warnHandle(e,"convert to runtime exception?");
		}
	}
	
}
