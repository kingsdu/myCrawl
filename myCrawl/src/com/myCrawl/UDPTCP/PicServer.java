package com.myCrawl.UDPTCP;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PicServer {

	/*
	 * 上传一张图片至服务端
	 * *
	 */

	public static void main(String args[]){		
		try {
			ServerSocket ss = new ServerSocket(10002);

			while(true){
				Socket s = ss.accept();
				
				new Thread(new PicThread(s)).start();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/*String ip = s.getInetAddress().getHostAddress();

System.out.println(ip+"......conntected");

InputStream in = s.getInputStream();

FileOutputStream fos = new FileOutputStream("H:\\Pictures\\sa1.jpg");

byte []buf = new byte[1024];

int len = 0;

while((len = in.read(buf))!=-1){
	fos.write(buf,0,len);
}		
OutputStream out = s.getOutputStream();			
out.write("上传成功".getBytes());*/
