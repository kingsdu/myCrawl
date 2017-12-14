package com.myCrawl.UDPTCP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer02 {


	/*
	 * 建立一个文本转换服务器
	 * 客户端给服务端发送文本，服务端将文本转换成大写在返回客户端，当输入over结束
	 * 
	 * 源：键盘输入
	 * 目的：网络设备，网络输出流
	 * */

	public static void main(String[] args){
		try {
			ServerSocket ss = new ServerSocket(10001);

			Socket s = ss.accept();//阻塞式方法，接受客户端输入数据

			String ip = s.getInetAddress().getHostAddress();

			System.out.println(ip+"......conntected");

			BufferedReader brwIN =
					new BufferedReader(new InputStreamReader(s.getInputStream()));

			BufferedWriter bfwOUT = 
					new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));//字节流转字符流

			String line = null;
			//从客户端读取，在将其转换为大写字符
			while((line = brwIN.readLine())!=null){	
				bfwOUT.write(line.toUpperCase());
				bfwOUT.newLine();
				bfwOUT.flush();
				
				System.out.println(line);
			}
			
			s.close();
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
