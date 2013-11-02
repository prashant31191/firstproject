package com.qinnuan.engine.api.film;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.engine.api.AbstractParam;
import com.qinnuan.common.http.RequestType;

/**
 * Created by showu on 13-8-13.
 */
@RequestType(type= HttpMethod.POST)
public class DeleteGroupUser extends AbstractParam {
    private final String api="/api/film/delete_group_user.api";
    @Override
    public String getApi() {
        return api;
    }
    private String userid  ;//用户ID
    private String deviceidentifyid  ;//设备ID
    private final int devicetype=1  ;//设备类型 0=ios 1=android
    private String groupid  ;//群ID

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
}
