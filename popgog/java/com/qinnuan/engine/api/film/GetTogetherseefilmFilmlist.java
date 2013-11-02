package com.qinnuan.engine.api.film;


import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

@RequestType(type = HttpMethod.GET)
public class GetTogetherseefilmFilmlist extends AbstractParam {

	private final String api="/api/film/get_togetherseefilm_filmlist.api" ;

	@Override
	public String getApi() {
		return api;
	}

    private String logincityid;             //城市ID
    private String deviceidentifyid;        //设备ID
    private String devicetype;              //设备类型 0=ios 1=android
    private String userid;                 //用户ID

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

}
