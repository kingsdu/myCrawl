package com.myCrawl.three.ip;

public class IPTest {

	public static void main(String[] args) {
		// ָ���������ݿ���ļ����������ļ���
		IPSeeker ip = new IPSeeker("qqwry_2.dat", "E:\\test\\crawlBookData");
		// ����IP 58.20.43.13
		System.out.println(ip.getIPLocation("47.52.93.148").getCountry() + ":"
				+ ip.getIPLocation("47.52.93.148").getArea());
	}
}
