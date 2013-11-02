package com.qinnuan.engine.api.film;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

/**
 * Created by showu on 13-7-26.
 */
@RequestType(type = HttpMethod.GET)
public class GetSeatlist extends AbstractParam {
    private final String  api="/api/film/get_seatlist.api" ;
    @Override
    public String getApi() {
        return api;
    }
    private String showinfoid  ;//场次ID
    private String userid  ;//用户ID
    private String deviceidentifyid  ;//设备ID
    private int devicetype=1  ;//设备类型 0=ios 1=android

    public void setShowinfoid(String showinfoid) {
        this.showinfoid = showinfoid;
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
