package com.qinnuan.engine.bean;

import com.qinnuan.common.josn.EAJson;

public class UpomBean {
	@EAJson
	private String merchantname;
	@EAJson
	private String merchantid;
	@EAJson
	private String merchantorderid;
	@EAJson
	private String merchantordertime ;
	@EAJson
	private String merchantorderamt ;
	@EAJson
	private String sign;

	public String getMerchantname() {
		return merchantname;
	}

	public void setMerchantname(String merchantname) {
		this.merchantname = merchantname;
	}

	public String getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}

	public String getMerchantorderid() {
		return merchantorderid;
	}

	public void setMerchantorderid(String merchantorderid) {
		this.merchantorderid = merchantorderid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getMerchantordertime() {
		return merchantordertime;
	}

	public void setMerchantordertime(String merchantordertime) {
		this.merchantordertime = merchantordertime;
	}

	public String getMerchantorderamt() {
		return merchantorderamt;
	}

	public void setMerchantorderamt(String merchantorderamt) {
		this.merchantorderamt = merchantorderamt;
	}

}
