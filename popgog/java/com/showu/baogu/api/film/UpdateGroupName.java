package com.showu.baogu.api.film;

import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-8-12.
 */
@RequestType(type = HttpMethod.POST)
public class UpdateGroupName extends AbstractParam {
    private final String api="/api/film/update_group_name.api";
    @Override
    public String getApi() {
        return api;
    }
    private String userid  ;//用户ID
    private String deviceidentifyid  ;//设备ID
    private final int devicetype=1  ;//设备类型 0=ios 1=android
    private String groupid  ;//群ID
    private String groupname  ;//群名称

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
}
