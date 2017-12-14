package com.myCrawl.UDPTCP;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient03 {

	/*
	 * 文件上传
	 * */
	
	public static void main(String[] args){	
		try {
			Socket s = new Socket("192.168.0.109",10001);
			
			BufferedReader bufr = new BufferedReader(new FileReader("E:/test/output_res.txt"));
			
			PrintWriter out = new PrintWriter(s.getOutputStream(),true);
			
			String line = null;
			
			while((line = bufr.readLine())!=null){
				out.println(line);
			}
			
			s.shutdownOutput();//结束标记
			
			BufferedReader bufrINT = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			String str =  bufrINT.readLine();
			
			System.out.println(str);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
}
