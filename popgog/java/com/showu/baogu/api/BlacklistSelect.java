package com.showu.baogu.api;

import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-6-14.
 */
@RequestType(type = HttpMethod.GET)
public class BlacklistSelect extends AbstractParam {

    private final String api="/api/filmfans/blacklist_select.api";
    @Override
    public String getApi() {
        return api;
    }

    private String deviceidentifyid;
    private String devicetype;
    private String userid;  //用户ID

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
