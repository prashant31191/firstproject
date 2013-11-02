package com.qinnuan.engine.util.report;

public enum ReportType {
	SEXINFO(0), // 色情信息
	REACTIONARY_POLITICAL(1), // 反动政治
	ADS_CHEAT(2), // 广告欺诈
	SCOLD(3), // 谩骂骚扰
	CHEAT_MESSAGE(4), // 伪造他人言论
	RUMORBOOING(5), // 造摇起哄
	USERINFO(6), // 个人资料不当
	USER_OTHRE_INFO(7);// 盗用他人资料/
	private int value;

	private ReportType(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return value + "";
	}
}
