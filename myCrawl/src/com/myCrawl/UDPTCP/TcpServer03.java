package com.myCrawl.UDPTCP;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer03 {

	public static void main(String[] args){		
		try {
			ServerSocket ss = new ServerSocket(10001);
			
			Socket s = ss.accept();//阻塞式方法，接受客户端输入数据
			
			String ip = s.getInetAddress().getHostAddress();

			System.out.println(ip+"......conntected");
			
			BufferedReader buFIN = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			PrintWriter out = new PrintWriter(new FileWriter("E:/test/111.txt"),true);
			
			String line = null;
			
			while((line = buFIN.readLine())!=null){
				out.println(line);
			}	
			
			PrintWriter pw = new PrintWriter(s.getOutputStream(),true);		
			pw.println("上传成功");
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
}
