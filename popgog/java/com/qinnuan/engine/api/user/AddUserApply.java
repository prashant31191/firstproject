package com.qinnuan.engine.api.user;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

/**
 * Created by showu on 13-6-14.
 */
@RequestType(type = HttpMethod.POST)
public class AddUserApply extends AbstractParam {

    private final String api="/api/global/add_user_apply.api";
    @Override
    public String getApi() {
        return api;
    }

    private String deviceidentifyid; //设备ID
    private String devicetype;  //设备类型 0=ios 1=android
    private String phone;  //手机号码

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
