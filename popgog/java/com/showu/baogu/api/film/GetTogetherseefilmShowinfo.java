package com.showu.baogu.api.film;

import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-7-31.
 */
@RequestType(type = HttpMethod.GET)
public class GetTogetherseefilmShowinfo extends AbstractParam {
    private final String api="/api/film/get_togetherseefilm_showinfo.api" ;
    @Override
    public String getApi() {
        return api ;
    }
    private String uorderid  ;//订单ID
    private String userid  ;//用户ID
    private String deviceidentifyid  ;//设备ID
    private int devicetype=1  ;//设备类型 0=ios 1=android

    public void setUorderid(String uorderid) {
        this.uorderid = uorderid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }
}
