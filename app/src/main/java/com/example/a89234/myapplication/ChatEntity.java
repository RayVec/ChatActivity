package com.example.a89234.myapplication;

public class ChatEntity {
	private int avatar;
	private String content;
	private String time;
	private int type;//0左，1中，2右
	
	public ChatEntity(String content,String time,int type){
		this.content = content;
		this.time = time;
		this.type=type;
	}
	
	public int getAvatar() {
		return avatar;
	}
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
