package com.showu.baogu.api.filmFan;

import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-6-14.
 */
@RequestType(type = HttpMethod.POST)
public class AddUserDynamiccomment extends AbstractParam {

    private final String api="/api/filmfans/add_user_dynamiccomment.api";
    @Override
    public String getApi() {
        return api;
    }

    private String userid;  //用户ID
    private String deviceidentifyid; //设备ID
    private String devicetype;  //设备类型 0=ios 1=android
    private String cinemadynamicid;  //动态ID
    private String content;  //内容
    private String refcdynamiccommentid;  //ref评论ID
    private String rootcdynamiccommentid;  //root评论ID

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setCinemadynamicid(String cinemadynamicid) {
        this.cinemadynamicid = cinemadynamicid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRefcdynamiccommentid(String refcdynamiccommentid) {
        this.refcdynamiccommentid = refcdynamiccommentid;
    }

    public void setRootcdynamiccommentid(String rootcdynamiccommentid) {
        this.rootcdynamiccommentid = rootcdynamiccommentid;
    }

}
