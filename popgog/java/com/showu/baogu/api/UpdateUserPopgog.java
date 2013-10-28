package com.showu.baogu.api;

import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

@RequestType(type = HttpMethod.POST)
public class UpdateUserPopgog extends AbstractParam {

    private final String api="/api/user/update_user_popgog.api";
    @Override
    public String getApi() {
        return api;
    }

    private String userid;  //用户ID
    private String deviceidentifyid;  //设备ID
    private String devicetype;  //设备类型 0=ios 1=android
    private String popgogid;  //爆谷号

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDeviceidentifyid() {
        return deviceidentifyid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getPopgogid() {
        return popgogid;
    }

    public void setPopgogid(String popgogid) {
        this.popgogid = popgogid;
    }

}
