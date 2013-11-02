package com.qinnuan.engine.api;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;

/**
 * Created by showu on 13-6-19.
 */
@RequestType(type = HttpMethod.POST)
public class UpdateUserPasswordPhone extends AbstractParam {

    private String api="/api/user/update_user_password_phone.api" ;
    private String userid ; //用户ID
    private String newpassword ; //新密码
    private String deviceidentifyid ;
    private String devicetype;

    @Override
    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

}
