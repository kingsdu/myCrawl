package com.myCrawl.UDPTCP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Socket_receive implements Runnable{

	private DatagramSocket ds;

	public Socket_receive(DatagramSocket ds){
		this.ds = ds;
	}
	

	@Override
	public void run() {
		while(true){
			try {
				//2 确定数据，并将其封装为数据包
				byte[] buf = new byte[1024];
				DatagramPacket dp = new DatagramPacket(buf,buf.length);
				//3 发送数据
				ds.receive(dp);
				System.out.println("addressName:  "+dp.getAddress().getHostAddress());
				String data = new String(dp.getData(),0,dp.getLength());
				System.out.println("data:  "+data);
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
//	//UDP模式发送数据
//	public static void main(String args[]){	
//		UDPPartern_receive_sysIn();
//	}



	public static void UDPPartern_receive(){
		try {
			//1 创建UDP服务.通过DatagramSocket对象
			DatagramSocket ds = new DatagramSocket(1234);
			//2 确定数据，并将其封装为数据包
			byte[] buf = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buf,buf.length);
			//3 发送数据
			ds.receive(dp);
			System.out.println("addressName"+dp.getAddress().getHostAddress());
			String data = new String(dp.getData(),0,dp.getLength());
			System.out.println("data"+data);
			//4 关闭资源
			ds.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * @Description: 接受键盘输入数据
	 * @return:
	 * @date: 2017-9-24  
	 */
	public static void UDPPartern_receive_sysIn(){
		while(true){
			try {
				//1 创建UDP服务.通过DatagramSocket对象
				DatagramSocket ds = new DatagramSocket(1234);
				//2 确定数据，并将其封装为数据包
				byte[] buf = new byte[1024];
				DatagramPacket dp = new DatagramPacket(buf,buf.length);
				//3 发送数据
				ds.receive(dp);
				System.out.println("addressName"+dp.getAddress().getHostAddress());
				String data = new String(dp.getData(),0,dp.getLength());
				System.out.println("data"+data);
				//4 关闭资源
				ds.close();
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



}
