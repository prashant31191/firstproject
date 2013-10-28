package com.showu.baogu.api.film;

import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-8-12.
 */
@RequestType(type = HttpMethod.POST)
public class UpdateGroupNotice extends AbstractParam {
    private final String api="/api/film/update_group_notice.api";
    @Override
    public String getApi() {
        return api;
    }
    private String userid  ;//用户ID
    private String deviceidentifyid  ;//设备ID
    private final int devicetype=1  ;//设备类型 0=ios 1=android
    private String groupid  ;//群ID
    private int isnotice  ;//是否通知 0=否 1=是

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public void setIsnotice(int isnotice) {
        this.isnotice = isnotice;
    }
}
