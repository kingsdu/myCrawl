package com.myCrawl.three;

import java.net.*;
import java.io.*;

public class IP {
	public static void main(String[] args) throws IOException {
		String hostname = "www.sina.com";
//		BufferedReader input = new BufferedReader(new InputStreamReader(
//				System.in));
//		System.out.print("\n");
//		System.out.print("Host name: ");
//		hostname = input.readLine();
		try {
			InetAddress ipaddress = InetAddress.getByName(hostname);
			System.out.println("IP address: " + ipaddress.getHostAddress());
		} catch (UnknownHostException e) {
			System.out.println("Could not find IP address for: " + hostname);
		}
	}
}
