package com.qinnuan.engine.api;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;

/**
 * Created by showu on 13-7-23.
 */
@RequestType(type= HttpMethod.POST)
public class CancelUserSns extends AbstractParam {
    private final String api="/api/user/cancel_user_sns.api";

    private String snstype  ;//第三方类型 0=新浪微博 1=QQ
    private String userid  ;//用户ID
    private String deviceidentifyid  ;//设备ID
    private String devicetype;//设备类型 0=ios 1=android

    @Override
    public String getApi() {
        return api;
    }

    public void setSnstype(String snstype) {
        this.snstype = snstype;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }
}
