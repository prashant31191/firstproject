package com.qinnuan.engine.api.film;

import com.qinnuan.engine.api.AbstractParam;
import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;

/**
 * Created by showu on 13-8-20.
 */
@RequestType(type = HttpMethod.GET)
public class GetOrderStatus extends AbstractParam {
    private final String api="/api/film/get_order_status.api" ;
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
