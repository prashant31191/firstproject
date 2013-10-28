package com.showu.baogu.api.film;

import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-8-12.
 */
@RequestType(type = HttpMethod.GET)
public class GetTogetherseefilmSeatlist extends AbstractParam {
    private final String api="/api/film/get_togetherseefilm_seatlist.api" ;
    private String groupid  ;//群ID
    private String userid  ;//用户ID
    private String deviceidentifyid  ;//设备ID
    private final int devicetype=1  ;//设备类型 0=ios 1=android

    @Override
    public String getApi() {
        return api;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }
}
