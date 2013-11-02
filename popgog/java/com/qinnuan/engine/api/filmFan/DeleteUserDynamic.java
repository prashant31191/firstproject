package com.qinnuan.engine.api.filmFan;

import com.qinnuan.engine.api.AbstractParam;
import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;

@RequestType(type = HttpMethod.POST)
public class DeleteUserDynamic extends AbstractParam {

    private final String api="/api/filmfans/delete_user_dynamic.api";
    @Override
    public String getApi() {
        return api;
    }

    private String userid;  //用户ID
    private String deviceidentifyid; //设备ID
    private String devicetype;  //设备类型 0=ios 1=android
    private String cinemadynamicid;  //动态ID

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setCinemadynamicid(String cinemadynamicid) {
        this.cinemadynamicid = cinemadynamicid;
    }

}
