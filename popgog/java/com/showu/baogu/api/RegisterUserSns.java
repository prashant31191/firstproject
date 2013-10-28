package com.showu.baogu.api;

import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-6-14.
 */
@RequestType(type = HttpMethod.POST)
public class RegisterUserSns extends AbstractParam {

    private final String api="/api/user/register_user_sns.api";
    @Override
    public String getApi() {
        return api;
    }

    private String snsuserid;               //第三方ID
    private String snstype;                 //第三方类型 0=新浪微博 1=QQ
    private String profileimg;              //头像
    private String nickname;                //昵称
    private String sex;                     //性别 1=男 2=女 0=未知
    private String longitude;               //经度
    private String latitude;                //纬度
    private String logincityid;             //当前城市ID
    private String deviceidentifyid;        //设备ID
    private String devicetype="1";              //设备类型 0=ios 1=android
    private String devicetoken;             //token  当devicetype = 0时传
    private String openid;                  //腾讯开放ID qq
    private String accesstokensecret;       //token密钥 X
    private String openkey;                 //对应开放ID的key X
    private String snsname;                 //第三方名称 sina
    private String accesstoken;             //访问token sina
    private String isverified;              //是否认证 0=否 1=是 sina
    private String expireddate;             //过期时间 qq sina

    public String getSnsuserid() {
        return snsuserid;
    }

    public void setSnsuserid(String snsuserid) {
        this.snsuserid = snsuserid;
    }

    public String getSnstype() {
        return snstype;
    }

    public void setSnstype(String snstype) {
        this.snstype = snstype;
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

    public String getSnsname() {
        return snsname;
    }

    public void setSnsname(String snsname) {
        this.snsname = snsname;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getAccesstokensecret() {
        return accesstokensecret;
    }

    public void setAccesstokensecret(String accesstokensecret) {
        this.accesstokensecret = accesstokensecret;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOpenkey() {
        return openkey;
    }

    public void setOpenkey(String openkey) {
        this.openkey = openkey;
    }

    public String getIsverified() {
        return isverified;
    }

    public void setIsverified(String isverified) {
        this.isverified = isverified;
    }

    public String getExpireddate() {
        return expireddate;
    }

    public void setExpireddate(String expireddate) {
        this.expireddate = expireddate;
    }

}
