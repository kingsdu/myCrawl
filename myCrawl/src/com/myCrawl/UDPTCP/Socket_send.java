package com.myCrawl.UDPTCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Socket_send implements Runnable{

	private DatagramSocket ds;
	
	
	public Socket_send(DatagramSocket ds){
		this.ds = ds;
	}
	

	@Override
	public void run() {
		try {
			//2 确定数据，并将其封装为数据包
			BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			
			String line = null;
			
			while((line = bf.readLine())!=null){
				if("886".equals(line)){
					break;
				}
				byte[] buf = line.getBytes();
				
				DatagramPacket dp = new DatagramPacket(buf, buf.length,InetAddress.getByName(InetAddress.getLocalHost().getHostAddress()),12002);
				
				ds.send(dp);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	
//	//UDP模式发送数据
//	public static void main(String args[]){	
//		UDPPartern_send_sysIn();
//	}
	
	
	public static void UDPPartern_send(){
		try {
			//1 创建UDP服务.通过DatagramSocket对象
			DatagramSocket ds = new DatagramSocket();
			//2 确定数据，并将其封装为数据包
			byte[] buf = "udp ge men lai le".getBytes();
			DatagramPacket dp = new DatagramPacket(buf,buf.length,InetAddress.getByName(InetAddress.getLocalHost().getHostAddress()),1234);
			//3 发送数据
			ds.send(dp);
			//4 关闭资源
			ds.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	/**
	 * @Description: 获取本机IP地址
	 * @return:
	 * @date: 2017-9-23  
	 */
	public static void UDPPartern_IP(){
		try {
			InetAddress ip = InetAddress.getLocalHost();//得到本地IP地址
			System.out.println(ip.getAddress());
			System.out.println(ip.getHostAddress());//IP地址
			System.out.println(ip.getHostName());//主机名称
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * @Description: 发送键盘数据
	 * @return:
	 * @date: 2017-9-24  
	 */
	public static void UDPPartern_send_sysIn(){
		try {
			//1 创建UDP服务.通过DatagramSocket对象
			DatagramSocket ds = new DatagramSocket();
			//2 确定数据，并将其封装为数据包
			BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			
			String line = null;
			
			while((line = bf.readLine())!=null){
				if("886".equals(line)){
					break;
				}
				byte[] buf = line.getBytes();
				
				DatagramPacket dp = new DatagramPacket(buf, buf.length,InetAddress.getByName(InetAddress.getLocalHost().getHostAddress()),1234);
				
				ds.send(dp);
			}
			//4 关闭资源
			ds.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
