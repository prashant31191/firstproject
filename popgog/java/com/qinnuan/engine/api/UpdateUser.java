package com.qinnuan.engine.api;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;

@RequestType(type = HttpMethod.POST)
public class UpdateUser extends AbstractParam {

    private final String api="/api/user/update_user.api";
    @Override
    public String getApi() {
        return api;
    }

    private String userid;  //用户ID
    private String deviceidentifyid;  //设备ID
    private String devicetype;  //设备类型 0=ios 1=android
    private String nickname;  //名称
    private String profileimg;  //头像
    private String signature;  //签名

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}
