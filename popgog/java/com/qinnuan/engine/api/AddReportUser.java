package com.qinnuan.engine.api;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.application.Const;

@RequestType(type = HttpMethod.POST)
public class AddReportUser extends AbstractParam {

    private final String api="/api/filmfans/add_report_user.api";
    @Override
    public String getApi() {
        return api;
    }

    private String userid  ;//用户ID
    private String useridto  ;//用户ID
    private String deviceidentifyid  ;//设备ID
    private String devicetype= Const.DEVICE_TYPE;//设备类型 0=ios 1=android
    private String reftype  ;//举报类型 0=色情内容1=反动政治2=广告欺诈3=谩骂骚扰4=伪造他人言论5=招摇起哄 6=个人资料不当7=盗用他人资料
    private String uploadfile  ;//举报文件

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUseridto(String useridto) {
        this.useridto = useridto;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setReftype(String reftype) {
        this.reftype = reftype;
    }

    public void setUploadfile(String uploadfile) {
        this.uploadfile = uploadfile;
    }
}
