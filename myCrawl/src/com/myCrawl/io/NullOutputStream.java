package com.myCrawl.io;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream{
	
	public NullOutputStream(){
	}

	public void write(int b) throws IOException {
	}

	public void write(byte[] b){	
	}
	
	public void write(byte[] b, int off, int len){
	}
}
