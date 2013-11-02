package com.qinnuan.engine.bean.film;

import com.qinnuan.common.josn.EAJson;

import java.util.ArrayList;
import java.util.List;

public class ProvinceBean {
	public List<CityBean> city = new ArrayList<CityBean>() ;
	@EAJson
	public String provinceid;
	@EAJson
	public String provincename;

	public List<CityBean> getCityList() {
		return city;
	}

	public String getProvinceid() {
		return provinceid;
	}

	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}

	public String getProvincename() {
		return provincename;
	}

	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}

}
