package com.showu.baogu.api.film;


import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

@RequestType(type = HttpMethod.GET)
public class GetTogetherseefilmUserlist extends AbstractParam {

	private final String api="/api/film/get_togetherseefilm_userlist.api" ;

	@Override
	public String getApi() {
		return api;
	}

    private String logincityid;             //城市ID
    private String deviceidentifyid;        //设备ID
    private String devicetype;              //设备类型 0=ios 1=android
    private String userid;                 //用户ID
    private String pageindex;  //页索引 默认0

    public void setLogincityid(String logincityid) {
        this.logincityid = logincityid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setPageindex(String pageindex) {
        this.pageindex = pageindex;
    }
}
