package com.cn.graduationclient.service;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class SimpleServer {
	public static ArrayList<Socket> socketList=new ArrayList<Socket>();
	
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		ServerSocket ss=new ServerSocket(8008);
		while (true) {
			Socket s=ss.accept();
			socketList.add(s);
			new Thread(new ServerTherad(s)).start();
		}
	}

}
