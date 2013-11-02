package com.qinnuan.engine.api.filmFan;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

/**
 * Created by showu on 13-6-14.
 */
@RequestType(type = HttpMethod.GET)
public class GetUserDynamicDetail extends AbstractParam {

    private final String api="/api/filmfans/get_user_dynamic_detail.api";
    @Override
    public String getApi() {
        return api;
    }

    private String userid;  //用户ID
    private String deviceidentifyid; //设备ID
    private String devicetype;  //设备类型 0=ios 1=android
    private String cinemadynamicid;  //动态ID
    private String cdynamiccommentid;  //评论ID 分页用

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

    public void setCdynamiccommentid(String cdynamiccommentid) {
        this.cdynamiccommentid = cdynamiccommentid;
    }

}
