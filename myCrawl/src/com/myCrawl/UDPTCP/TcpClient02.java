package com.myCrawl.UDPTCP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient02 {

	public static void main(String[] args){
		try {
			Socket s = new Socket("192.168.0.109",10001);
			
			BufferedReader br =
					new BufferedReader(new InputStreamReader(System.in));
			
			BufferedWriter bfwOUT = 
					new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));//字节流转字符流
			
			BufferedReader brwIN =
					new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			String line = null;
			
			while((line = br.readLine())!=null){		
				if("over".equals(line)){
					break;
				}
				
				bfwOUT.write(line);
				bfwOUT.newLine();
				bfwOUT.flush();
				
				//返回服务端返回的大写数据
				String str = brwIN.readLine();
				System.out.println(str);
			}
			br.close();
			s.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
