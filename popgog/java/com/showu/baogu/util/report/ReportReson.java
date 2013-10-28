package com.showu.baogu.util.report;

public enum ReportReson {
	USERINFO(0), // 用户资料
	ACTIVITYCOMMENT(1), // 活动点评
	FILMCOMMENT(2), // 影片点评
	CINEMACOMMENT(3);// 影院点评
	private int value;

	private ReportReson(int reson) {
		this.value = reson;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return value + "";
	}
}
