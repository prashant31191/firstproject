package com.showu.baogu.api;

import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-7-22.
 */
@RequestType(type = HttpMethod.POST)
public class UpdateUserQuit extends AbstractParam {
    private final String api="/api/user/update_user_quit.api";
    private String userid  ;//用户ID(用户登录时传)
    private String deviceidentifyid  ;//设备ID(跟userid同传)
    private String devicetype  ;//设备类型 0=ios 1=android(跟userid同传)

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
