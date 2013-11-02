package com.qinnuan.engine.api.user;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

/**
 * Created by showu on 13-6-14.
 */
@RequestType(type = HttpMethod.POST)
public class AddUserCashremain extends AbstractParam {

    private final String api="/api/user/add_user_cashremain.api";
    @Override
    public String getApi() {
        return api;
    }

    private String userid;  //用户ID
    private String deviceidentifyid; //设备ID
    private String devicetype;  //设备类型 0=ios 1=android
    private String cashremain;  //充值数

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setCashremain(String cashremain) {
        this.cashremain = cashremain;
    }

}
