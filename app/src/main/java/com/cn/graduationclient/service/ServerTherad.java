package com.cn.graduationclient.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ServerTherad implements Runnable {
	private Socket s = null;
	private BufferedReader buRead = null;
	StringBuffer stb=new StringBuffer();
	public ServerTherad(Socket s) throws IOException {
		this.s = s;
		this.buRead = new BufferedReader(new InputStreamReader(
				this.s.getInputStream(), "utf-8"));
	}

	@Override
	public void run() {
		String connet=null;
		try {
			while ((connet=readFromClient())!=null) {

				for (Socket ss:SimpleServer.socketList) {
					OutputStream out=ss.getOutputStream();
					out.write((connet+"\n").getBytes("utf-8"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private String readFromClient(){
		try {
			return buRead.readLine();
		} catch (Exception e) {
			
			SimpleServer.socketList.remove(s);
		}
		return null;
	}

}
