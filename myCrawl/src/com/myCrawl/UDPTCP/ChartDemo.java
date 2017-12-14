package com.myCrawl.UDPTCP;

import java.net.DatagramSocket;
import java.net.SocketException;


public class ChartDemo{


	//UDP模式发送数据
	public static void main(String args[]){	
		DatagramSocket sendSocket = null;
		DatagramSocket receiveSocket = null;
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(12002);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		new Thread(new Socket_send(sendSocket)).start();
		new Thread(new Socket_receive(receiveSocket)).start();
	}



}
