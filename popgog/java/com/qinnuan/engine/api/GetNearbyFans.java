package com.qinnuan.engine.api;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;

@RequestType(type = HttpMethod.GET)
public class GetNearbyFans extends AbstractParam {

    private final String api="/api/filmfans/get_nearby_fans.api";
    @Override
    public String getApi() {
        return api;
    }

    private String userid;              //用户ID
    private String deviceidentifyid;    //设备ID
    private String devicetype;          //设备类型
    private String longitude;           //经度
    private String latitude;            //纬度
    private String pageindex;           //当前页索引
    private String sex;                 //性别 0=未知 1=男 2=女

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

    public String getPageindex() {
        return pageindex;
    }

    public void setPageindex(String pageindex) {
        this.pageindex = pageindex;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

}
