package com.showu.baogu.api.film;

import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-8-19.
 */
@RequestType(type= HttpMethod.POST)
public class UpdateCouponStatus extends AbstractParam{
    private final String api="/api/film/update_coupon_status.api" ;
    private String uorderid  ;//订单ID
    private String ucashcouponid   ;//面值
    private String userid  ;//用户ID
    private String deviceidentifyid  ;//设备ID
    private final int devicetype=1  ;//设备类型 0=ios 1=android
    public void setUorderid(String uorderid) {
        this.uorderid = uorderid;
    }

    public void setUcashcouponid(String ucashcouponid) {
        this.ucashcouponid = ucashcouponid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    @Override
    public String getApi() {
        return api;
    }
}
