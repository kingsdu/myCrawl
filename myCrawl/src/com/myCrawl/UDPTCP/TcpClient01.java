package com.myCrawl.UDPTCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient01 {

	/*
	 * 演示TCP的传输的客户端和服务端的互访
	 * 
	 * 
	 * 客户端：
	 * 1 建立socket服务。指定要连接的主机端口
	 * 2 获取socket流中的输出流。将数据写到该流中。通过网络发个服务端
	 * 3 获取socket中的输入流，将服务端反馈的数据获取到，并打印
	 * 4 关闭客户端资源
	 * */
	
	/**
	 * @Description: TODO
	 * @return:
	 * @date: 2017-9-26  
	 */
	public static void main(String[] args) {
		try {
			Socket s = new Socket("192.168.0.109",10001);
			
			OutputStream out = s.getOutputStream();
			
			out.write("hello server".getBytes());
			
			InputStream in = s.getInputStream();
			
			byte[] buf = new byte[1024];
			
			int len = in.read(buf);//阻塞式方法，接受服务端返回数据
			
			System.out.println(new String(buf,0,len));
			
			s.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
