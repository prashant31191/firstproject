package com.qinnuan.engine.api.filmFan;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

@RequestType(type = HttpMethod.GET)
public class GetUserDynamicOther extends AbstractParam {

    private final String api="/api/filmfans/get_user_dynamic_other.api";
    @Override
    public String getApi() {
        return api;
    }

    private String userid;  //用户ID
    private String deviceidentifyid;  //设备ID
    private String devicetype;  //设备类型 0=ios 1=android
    private String cinemadynamicid;  //动态ID 点击更多传
    private String useridto;  //其他用户ID

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

    public String getCinemadynamicid() {
        return cinemadynamicid;
    }

    public void setCinemadynamicid(String cinemadynamicid) {
        this.cinemadynamicid = cinemadynamicid;
    }

    public void setUseridto(String useridto) {
        this.useridto = useridto;
    }

}
