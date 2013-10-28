package com.showu.baogu.api.filmFan;

import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

@RequestType(type = HttpMethod.POST)
public class DeleteUserLikedynamic extends AbstractParam {

    private final String api = "/api/filmfans/delete_user_likedynamic.api";

    @Override
    public String getApi() {
        return api;
    }

    private String userid;  //用户ID
    private String deviceidentifyid;  //设备ID
    private String devicetype;  //设备类型 0=ios 1=android
    private String likecdynamicid;//赞ID


    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setLikecdynamicid(String likecdynamicid) {
        this.likecdynamicid = likecdynamicid;
    }

}
