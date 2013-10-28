package com.showu.baogu.bean.film;

import com.showu.common.josn.EAJson;

import java.io.Serializable;

public class CityBean implements Serializable{
	@EAJson
	private String cityid;
	@EAJson
	private String cityname;
	@EAJson
	private String ishot;

	public CityBean() {
		
	}
	
	public CityBean(String id,String name){
		this.cityid=id ;
		this.cityname=name ;
	}
	public String getLocalid() {
		return cityid;
	}

	public void setLocalid(String localid) {
		this.cityid= localid;
	}

	public String getLocalname() {
		return cityname;
	}

	public void setLocalname(String localname) {
		this.cityname= localname;
	}

	public String getIshot() {
		return ishot;
	}

	public void setIshot(String ishot) {
		this.ishot = ishot;
	}

}
