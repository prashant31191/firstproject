package com.showu.baogu.api;

import com.showu.baogu.application.Const;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-7-22.
 */
@RequestType(type = HttpMethod.POST)
public class BindingPhoneCreate extends AbstractParam {
    private final String api="/api/user/binding_phone_create.api";
    private String phone  ;//手机号码
    private String userid  ;//用户ID(用户登录时传)
    private String deviceidentifyid  ;//设备ID(跟userid同传)
    private String devicetype  ;//设备类型 0=ios 1=android(跟userid同传)

    public void setPhone(String phone) {
        this.phone = phone;
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

    @Override
    public String getApi() {
        return api;
    }
}
