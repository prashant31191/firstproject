package com.qinnuan.engine.bean;


import com.qinnuan.common.josn.EAJson;

public class VersionBean {
	@EAJson
	private int androidversionnum ;
	@EAJson
	private String androidversion ;
	@EAJson
	private String androiddownloadurl ;
	@EAJson
	private String androiddescription ;
	public int getAndroidversionnum() {
		return androidversionnum;
	}
	public void setAndroidversionnum(int androidversionnum) {
		this.androidversionnum = androidversionnum;
	}
	public String getAndroidversion() {
		return androidversion;
	}
	public void setAndroidversion(String androidversion) {
		this.androidversion = androidversion;
	}
	public String getAndroiddownloadurl() {
		return androiddownloadurl;
	}
	public void setAndroiddownloadurl(String androiddownloadurl) {
		this.androiddownloadurl = androiddownloadurl;
	}

    public String getAndroiddescription() {
        return androiddescription;
    }

    public void setAndroiddescription(String androiddescription) {
        this.androiddescription = androiddescription;
    }
}
