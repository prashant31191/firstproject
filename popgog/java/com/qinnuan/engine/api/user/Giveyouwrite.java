package com.qinnuan.engine.api.user;

import com.qinnuan.engine.api.AbstractParam;
import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;

/**
 * Created by showu on 13-8-28.
 */
@RequestType(type = HttpMethod.POST)
public class Giveyouwrite extends AbstractParam {
    private final String api="/api/filmfans/giveyouwrite.api";
    private String userid  ;//用户ID
    private String useridto  ;//需要解除的用户id
    private String deviceidentifyid  ;//设备ID
    private final int devicetype=1  ;//设备类型 0=ios 1=android

    @Override
    public String getApi() {
        return api;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUseridto(String useridto) {
        this.useridto = useridto;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }
}
