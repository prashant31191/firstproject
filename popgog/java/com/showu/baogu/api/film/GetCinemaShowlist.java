package com.showu.baogu.api.film;

import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-7-25.
 */
@RequestType(type = HttpMethod.GET)
public class GetCinemaShowlist extends AbstractParam {
    final private String api = "/api/filmfans/get_cinema_showlist.api";

    @Override
    public String getApi() {
        return api;
    }

    private String filmid;//影片ID
    private String longitude;//经度
    private String latitude;//纬度
    private String userid;//用户ID
    private String logincityid;  //登录城市ID
    private String deviceidentifyid;//设备ID
    private int devicetype = 1;//设备类型 0=ios 1=android

    public void setFilmid(String filmid) {
        this.filmid = filmid;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(int devicetype) {
        this.devicetype = devicetype;
    }

    public void setLogincityid(String logincityid) {
        this.logincityid = logincityid;
    }
}
