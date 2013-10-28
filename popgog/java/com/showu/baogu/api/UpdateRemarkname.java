package com.showu.baogu.api;

import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

@RequestType(type = HttpMethod.POST)
public class UpdateRemarkname extends AbstractParam {

    private final String api="/api/filmfans/update_remarkname.api";
    @Override
    public String getApi() {
        return api;
    }

    private String userid;  //用户ID
    private String deviceidentifyid;  //设备ID
    private String devicetype;  //设备类型 0=ios 1=android
    private String remarkname;//备注名
    private String useridto; // 用户ID

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setRemarkname(String remarkname) {
        this.remarkname = remarkname;
    }

    public void setUseridto(String useridto) {
        this.useridto = useridto;
    }

}
