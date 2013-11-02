package com.qinnuan.engine.api;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;

/**
 * Created by showu on 13-7-20.
 */
@RequestType(type = HttpMethod.GET)
public class GetUserOrderOnline extends AbstractParam {

    private final String api="/api/user/get_user_order_online.api" ;

    @Override
    public String getApi() {
        return api;
    }

    private String userid; // --我的用户ID
    private String deviceidentifyid;  //--设备ID
    private String devicetype;  //--设备类型
    private String pageindex;  //页索引 默认0

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setPageindex(String pageindex) {
        this.pageindex = pageindex;
    }

}
