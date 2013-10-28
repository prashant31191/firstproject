package com.showu.baogu.api;

import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

@RequestType(type = HttpMethod.POST)
public class ForgetPasswordVerify extends AbstractParam {

    private final String api="/api/user/forget_password_verify.api";
    @Override
    public String getApi() {
        return api;
    }

    private String phone;    //手机号码
    private String userid;    //用户ID(用户登录时传)
    private String deviceidentifyid;    //设备ID(跟userid同传)
    private String devicetype="1";    //设备类型 0=ios 1=android(跟userid同传)
    private String verifycode;      //验证码

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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

    public String getVerifycode() {
        return verifycode;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
    }

}
