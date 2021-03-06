package com.qinnuan.engine.api.film;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.engine.api.AbstractParam;
import com.qinnuan.common.http.RequestType;

/**
 * Created by showu on 13-7-26.
 */
@RequestType(type = HttpMethod.GET)
public class GetShowinfo extends AbstractParam{
    private final  String api="/api/film/get_showinfo.api" ;
    @Override
    public String getApi() {
        return api;
    }
    private String filmid  ;//影片ID
    private String cinemaid  ;//影院ID
    private String userid  ;//用户ID
    private String deviceidentifyid  ;//设备ID
    private int devicetype=1  ;//设备类型 0=ios 1=android

    public void setFilmid(String filmid) {
        this.filmid = filmid;
    }

    public void setCinemaid(String cinemaid) {
        this.cinemaid = cinemaid;
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
}
