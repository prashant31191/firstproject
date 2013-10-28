package com.showu.baogu.api.filmFan;

import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

@RequestType(type = HttpMethod.GET)
public class GetUserUnreadMessage extends AbstractParam {

    private final String api="/api/filmfans/get_user_unread_message.api";
    @Override
    public String getApi() {
        return api;
    }

    private String userid;  //用户ID
    private String deviceidentifyid;  //设备ID
    private String devicetype;  //设备类型 0=ios 1=android
    private String pagesize;  //加载数量
    private String cdynamicmessageid;  //消息ID 点击更多传

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public void setCdynamicmessageid(String cdynamicmessageid) {
        this.cdynamicmessageid = cdynamicmessageid;
    }
}
