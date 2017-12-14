package com.myCrawl.UDPTCP;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PicThread implements Runnable{

	private Socket s;
	PicThread(Socket s){
		this.s = s;
	}
	@Override
	public void run() {
		int count = 1;
		String ip = s.getInetAddress().getHostAddress();
		try {	
			InputStream in = s.getInputStream();

			File file = new File(ip+"("+(count)+")"+".jpg");
			
			while(file.exists()){
				file = new File(ip+"("+(count++)+")"+".jpg");
			}
			
			FileOutputStream fos = new FileOutputStream(file);

			byte []buf = new byte[1024];

			int len = 0;

			while((len = in.read(buf))!=-1){
				fos.write(buf,0,len);
			}		
			OutputStream out = s.getOutputStream();			
			out.write("上传成功".getBytes());
		} catch (Exception e) {
			throw new RuntimeException(ip+"....error");
		}
	}	
}
