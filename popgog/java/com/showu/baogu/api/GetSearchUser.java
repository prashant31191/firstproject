package com.showu.baogu.api;

import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-7-20.
 */
@RequestType(type = HttpMethod.GET)
public class GetSearchUser extends AbstractParam {

    private final String api="/api/filmfans/get_search_user.api" ;

    @Override
    public String getApi() {
        return api;
    }

    private String userid; // --我的用户ID
    private String deviceidentifyid;  //--设备ID
    private String devicetype;  //--设备类型 0=ios 1=android
    private String longitude;  //--经度
    private String latitude ; //--纬度
    private String pageindex;  //页索引
    private String keyword; //关键字(爆谷号/名称)


    public void setUserid(String userid) {
        this.userid = userid;
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

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setPageindex(String pageindex) {
        this.pageindex = pageindex;
    }

}
