package com.qinnuan.engine.api.film;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

/**
 * Created by showu on 13-7-25.
 */
@RequestType(type = HttpMethod.GET)
public class GetFilmShowlist extends AbstractParam {
    private final String api="/api/filmfans/get_film_showlist.api" ;

    @Override
    public String getApi() {
        return api;
    }
    private String logincityid  ;//城市ID
    private String userid  ;//用户ID 登录传
    private String deviceidentifyid  ;//设备ID 同上
    private int devicetype =1 ;//设备类型 0=ios 1=android 同上

    public void setLogincityid(String logincityid) {
        this.logincityid = logincityid;
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
