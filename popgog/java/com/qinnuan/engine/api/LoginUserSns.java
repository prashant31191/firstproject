package com.qinnuan.engine.api;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;

/**
 * Created by showu on 13-6-14.
 */
@RequestType(type = HttpMethod.POST)
public class LoginUserSns extends AbstractParam {

    private final String api="/api/user/login_user_sns.api";
    @Override
    public String getApi() {
        return api;
    }

    private String snsuserid;  //第三方ID
    private String snstype;  //第三方类型 0=新浪微博 1=QQ
    private String longitude;
    private String latitude;
    private String logincityid;
    private String deviceidentifyid;
    private String devicetype="1";
    private String devicetoken;

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
