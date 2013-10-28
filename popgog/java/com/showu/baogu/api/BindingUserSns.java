package com.showu.baogu.api;

import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-7-23.
 */
@RequestType(type= HttpMethod.POST)
public class BindingUserSns extends AbstractParam {
    private final String api="/api/user/binding_user_sns.api";
    private String snsuserid  ;//第三方ID
    private String snstype  ;//第三方类型 0=新浪微博 1=QQ
    private String userid  ;//用户ID
    private String deviceidentifyid  ;//设备ID
    private int devicetype =1 ;//设备类型 0=ios 1=android
    private String snsname  ;//第三方名称
    private String accesstoken  ;//访问token
    private String accesstokensecret  ;//token密钥
    private String openid  ;//腾讯开放ID
    private String openkey  ;//对应开放ID的key
    private String isverified  ;//是否认证 0=否 1=是
    private String expireddate  ;//过期时间

    public void setSnsuserid(String snsuserid) {
        this.snsuserid = snsuserid;
    }

    public void setSnstype(String snstype) {
        this.snstype = snstype;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(int devicetype) {
        this.devicetype = devicetype;
    }

    public void setSnsname(String snsname) {
        this.snsname = snsname;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public void setAccesstokensecret(String accesstokensecret) {
        this.accesstokensecret = accesstokensecret;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setOpenkey(String openkey) {
        this.openkey = openkey;
    }

    public void setIsverified(String isverified) {
        this.isverified = isverified;
    }

    public void setExpireddate(String expireddate) {
        this.expireddate = expireddate;
    }

    @Override
    public String getApi() {
        return api;
    }
}
