package com.qinnuan.engine.api;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;

/**
 * Created by showu on 13-7-22.
 */
@RequestType(type = HttpMethod.POST)
public class ForgetPasswordCreateParam extends AbstractParam {
    private final String api="/api/user/forget_password_create.api";
    private String phone  ;//手机号码
    private String userid  ;//用户ID(用户登录时传)
    private String deviceidentifyid  ;//设备ID(跟userid同传)
    private int devicetype=1  ;//设备类型 0=ios 1=android(跟userid同传)

    public void setPhone(String phone) {
        this.phone = phone;
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

    @Override
    public String getApi() {
        return api;
    }
}
