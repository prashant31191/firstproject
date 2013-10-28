package com.showu.baogu.api.film;

import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-8-14.
 */
@RequestType(type = HttpMethod.GET)
public class GetActivityDetail extends AbstractParam {
    private final String api = "/api/activity/get_activity_detail.api";

    @Override
    public String getApi() {
        return api;
    }
    private String userid  ;//用户ID 登录传
    private String deviceidentifyid  ;//设备ID 同上
    private final int devicetype=1  ;//设备类型 0=ios 1=android 同上
    private String officialactivityid  ;//活动ID

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setOfficialactivityid(String officialactivityid) {
        this.officialactivityid = officialactivityid;
    }
}
