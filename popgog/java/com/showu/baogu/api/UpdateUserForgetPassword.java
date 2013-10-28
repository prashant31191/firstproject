package com.showu.baogu.api;

import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

@RequestType(type = HttpMethod.POST)
public class UpdateUserForgetPassword extends AbstractParam {

    private final String api="/api/user/update_user_forget_password.api";
    @Override
    public String getApi() {
        return api;
    }

    private String uverifyid; //验证ID
    private String newpassword;  //新密码
    private String userid;    //用户ID(用户登录时传)
    private String deviceidentifyid;    //设备ID(跟userid同传)
    private String devicetype;    //设备类型 0=ios 1=android(跟userid同传)

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDeviceidentifyid() {
        return deviceidentifyid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getUverifyid() {
        return uverifyid;
    }

    public void setUverifyid(String uverifyid) {
        this.uverifyid = uverifyid;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }
}
