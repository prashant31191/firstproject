package com.showu.baogu.api.filmFan;

import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-6-14.
 */
@RequestType(type = HttpMethod.POST)
public class AddUserDynamic extends AbstractParam {

    private final String api="/api/filmfans/add_user_dynamic.api";
    @Override
    public String getApi() {
        return api;
    }

    private String userid;  //用户ID
    private String deviceidentifyid; //设备ID
    private String devicetype;  //设备类型 0=ios 1=android
    private String content;  //内容
    private String imagelist;  //图片集 不能超过4张


    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImagelist(String imagelist) {
        this.imagelist = imagelist;
    }

}
