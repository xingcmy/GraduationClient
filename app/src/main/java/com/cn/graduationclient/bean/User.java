package com.cn.graduationclient.bean;

import android.graphics.Bitmap;

import java.io.Serializable;


public class User implements Serializable {
	private long id;		
	private String ip;
	private String port;
	private String name;
	private String img;
	private Bitmap bitmap;
	private int flag;		
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public User() {
	}
	public User(String ip, String port, String name, String img, int flag) {
		super();
		this.ip = ip;
		this.port = port;
		this.name = name;
		this.img = img;
		this.flag = flag;
	}
	public User(int id, String ip, String port, String name, String img) {
		super();
		this.id = id;
		this.ip = ip;
		this.port = port;
		this.name = name;
		this.img = img;
	}
	public User(String ip, String port, String name, Bitmap bitmap, int flag) {
		super();
		this.ip = ip;
		this.port = port;
		this.name = name;
		this.bitmap = bitmap;
		this.flag = flag;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
}
