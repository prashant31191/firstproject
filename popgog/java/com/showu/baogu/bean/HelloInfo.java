package com.showu.baogu.bean;


public class HelloInfo extends MessageListBean {
	private int userCount=0 ;
	private int notReadCount=0 ;
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public int getNotReadCount() {
		return notReadCount;
	}
	public void setNotReadCount(int notReadCount) {
		this.notReadCount = notReadCount;
	}
}
