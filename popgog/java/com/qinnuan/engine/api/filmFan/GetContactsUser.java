package com.qinnuan.engine.api.filmFan;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

@RequestType(type = HttpMethod.POST)
public class GetContactsUser extends AbstractParam {

    private final String api="/api/filmfans/get_contacts_user.api";
    @Override
    public String getApi() {
        return api;
    }

    private String userid;            //用户ID
    private String deviceidentifyid;  //设备ID
    private String devicetype;        //设备类型 0=ios 1=android
    private String pageindex;       //页索引
    private String phonenumbers;   //手机号，多个用|隔开
    private String phonenames;     //手机联系人，多个用|隔开

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setPageindex(String pageindex) {
        this.pageindex = pageindex;
    }

    public void setPhonenumbers(String phonenumbers) {
        this.phonenumbers = phonenumbers;
    }

    public void setPhonenames(String phonenames) {
        this.phonenames = phonenames;
    }
}
