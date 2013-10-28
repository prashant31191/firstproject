package com.showu.baogu.api.film;

import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-8-17.
 */
@RequestType(type= HttpMethod.POST)
public class DeleteOrderOnline extends AbstractParam {
    private final String api="/api/film/delete_order_online.api" ;
    @Override
    public String getApi() {
        return api;
    }
    private String userid  ;//用户ID
    private String deviceidentifyid  ;//设备ID
    private final int devicetype=1  ;//设备类型 0=ios 1=android
    private String uorderid  ;//订单ID

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setUorderid(String uorderid) {
        this.uorderid = uorderid;
    }
}
