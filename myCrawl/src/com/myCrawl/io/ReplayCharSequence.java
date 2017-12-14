package com.myCrawl.io;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ReplayCharSequence implements CharSequence{
	protected byte[] prefixBuffer;
	protected long size;
	protected long responseBodyStart; // where the response body starts, if marked
	
	protected byte[] wraparoundBuffer;
	protected int wrapOrigin; // index in underlying bytestream where wraparound buffer starts
	protected int wrapOffset; // index in wraparoundBuffer that corresponds to wrapOrigin

	protected String backingFilename;
	protected RandomAccessFile raFile;
	
	/**
	 * @param buffer
	 * @param size
	 * @param responseBodyStart
	 * @param backingFilename
	 * @throws IOException
	 */
	public ReplayCharSequence(byte[] buffer, long size, long responseBodyStart, String backingFilename) throws IOException {
		this(buffer,size,backingFilename);
		this.responseBodyStart = responseBodyStart;
	}
	
	
	/**
	 * @param buffer
	 * @param size
	 * @param backingFilename
	 * @throws IOException
	 */
	public ReplayCharSequence(byte[] buffer, long size, String backingFilename) throws IOException {
		this.prefixBuffer = buffer;
		this.size = size;
		if (size>buffer.length) {
			this.backingFilename = backingFilename;
			raFile = new RandomAccessFile(backingFilename,"r");
			wraparoundBuffer = new byte[buffer.length];
			wrapOrigin = prefixBuffer.length;
			wrapOffset = 0;
			loadBuffer();
		}
	}
	
	
	
	private void loadBuffer() {
		long len = -1;
		try {
			len=raFile.length();
			raFile.seek(wrapOrigin-prefixBuffer.length);
			raFile.readFully(wraparoundBuffer,0,(int)Math.min(wraparoundBuffer.length, size-wrapOrigin ));
		} catch (IOException e) {
			// TODO convert this to a runtime error?
//			DevUtils.logger.log(
//				Level.SEVERE,
//				"raFile.seek("+(wrapOrigin-prefixBuffer.length)+")\n"+
//				"raFile.readFully(wraparoundBuffer,0,"+((int)Math.min(wraparoundBuffer.length, size-wrapOrigin ))+")\n"+
//				"raFile.length()"+len+"\n"+
//				DevUtils.extraInfo(),
//				e);
		}
	}


	@Override
	public int length() {
		return (int) size;
	}


	@Override
	public char charAt(int index) {
		if(index < prefixBuffer.length) {
			return (char) ((int)prefixBuffer[index]&0xFF); // mask to unsigned
		}
		if(index >= wrapOrigin && index-wrapOrigin < wraparoundBuffer.length) {
			return (char) ((int)wraparoundBuffer[(index-wrapOrigin+wrapOffset) % wraparoundBuffer.length]&0xFF); // mask to unsigned
		}
		return faultCharAt(index);
	}

	
	private char faultCharAt(int index) {
		if(index>=wrapOrigin+wraparoundBuffer.length) {
			// moving forward 
			while (index>=wrapOrigin+wraparoundBuffer.length){
				// TODO optimize this 
				advanceBuffer();
			}
			return charAt(index);
		} else { 
			// moving backward 
			recenterBuffer(index);
			return charAt(index);
		}
	}
	
	private void recenterBuffer(int index) {
		System.out.println("recentering around "+index+" in "+ backingFilename);
		wrapOrigin = index - (wraparoundBuffer.length/2);
		if(wrapOrigin<prefixBuffer.length) {
			wrapOrigin = prefixBuffer.length;
		}
		wrapOffset = 0;
		loadBuffer();
	}
	
	
	private void advanceBuffer() {
		try {
			wraparoundBuffer[wrapOffset] = (byte)raFile.read();
			wrapOffset++;
			wrapOffset %= wraparoundBuffer.length;
			wrapOrigin++;
		} catch (IOException e) {
			// TODO convert this to a runtime error?
//			DevUtils.logger.log(Level.SEVERE,"advanceBuffer()"+DevUtils.extraInfo(),e);
		}
		
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return new CharSubSequence(this,start,end);
	}


	
}
