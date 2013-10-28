package com.showu.baogu.api;

import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-6-14.
 */
@RequestType(type = HttpMethod.POST)
public class RegisterUserPhone extends AbstractParam {

    private final String api="/api/user/register_user_phone.api";
    @Override
    public String getApi() {
        return api;
    }

    private String uverifyid;   //验证ID
    private String profileimg;  //头像
    private String nickname;    //昵称
    private String password;    //密码
    private String sex;         //性别 1=男 2=女 0=未知
    private String longitude;   //经度
    private String latitude;    //纬度
    private String logincityid; //城市ID
    private String deviceidentifyid;//设备ID
    private String devicetype;  //设备类型 0=ios 1=android
    private String devicetoken; //token  当devicetype = 0时传

    public String getUverifyid() {
        return uverifyid;
    }

    public void setUverifyid(String uverifyid) {
        this.uverifyid = uverifyid;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLogincityid() {
        return logincityid;
    }

    public void setLogincityid(String logincityid) {
        this.logincityid = logincityid;
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

    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }

}
