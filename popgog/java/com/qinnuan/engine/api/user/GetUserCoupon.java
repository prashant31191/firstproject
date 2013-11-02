package com.qinnuan.engine.api.user;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

/**
 * Created by showu on 13-6-14.
 */
@RequestType(type = HttpMethod.GET)
public class GetUserCoupon extends AbstractParam {

    private final String api="/api/user/get_user_coupon.api";
    @Override
    public String getApi() {
        return api;
    }

    private String userid;  //用户ID
    private String deviceidentifyid; //设备ID
    private String devicetype;  //设备类型 0=ios 1=android
    private String pageindex;          // 页索引 默认0

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setPageindex(String pageindex) {
        this.pageindex = pageindex;
    }

}
