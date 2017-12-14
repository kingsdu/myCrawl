package com.myCrawl.UDPTCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer01 {

	
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(10001);
			
			Socket s = ss.accept();//阻塞式方法，接受客户端输入数据
			
			String ip = s.getInetAddress().getHostAddress();
			
			System.out.println(ip+"......conntected");
			
			InputStream in = s.getInputStream();
			
			byte[] buf = new byte[1024];
			
			int len = in.read(buf);
			
			System.out.println(new String(buf,0,len));
			
			OutputStream out = s.getOutputStream();
			
			out.write("hello you too client".getBytes());
			
			s.close();
			
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
