package com.qinnuan.engine.api.filmFan;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

@RequestType(type = HttpMethod.POST)
public class DeleteFriend extends AbstractParam {

    private final String api="/api/filmfans/delete_friend.api";
    @Override
    public String getApi() {
        return api;
    }

    private String userid;  //用户ID
    private String deviceidentifyid;  //设备ID
    private String devicetype;  //设备类型 0=ios 1=android
    private String useridto; // 用户ID

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setUseridto(String useridto) {
        this.useridto = useridto;
    }

}
