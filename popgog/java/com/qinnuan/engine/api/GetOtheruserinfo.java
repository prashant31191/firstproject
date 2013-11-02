package com.qinnuan.engine.api;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;

/**
 * Created by showu on 13-7-20.
 */
@RequestType(type = HttpMethod.GET)
public class GetOtheruserinfo extends AbstractParam {

    private final String api="/api/filmfans/get_otheruserinfo.api" ;

    @Override
    public String getApi() {
        return api;
    }

    private String userid; // --我的用户ID
    private String otheruserid; // --其他用户ID
    private String deviceidentifyid;  //--设备ID
    private String devicetype;  //--设备类型
    private String longitude;  //--经度
    private String latitude ; //--纬度

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setOtheruserid(String otheruserid) {
        this.otheruserid = otheruserid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

}
