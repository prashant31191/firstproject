package com.showu.baogu.api.user;

import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-7-20.
 */
@RequestType(type = HttpMethod.POST)
public class AddFriend extends AbstractParam {

    private final String api="/api/filmfans/add_friend.api" ;

    @Override
    public String getApi() {
        return api;
    }

    private String userid; // --我的用户ID
    private String deviceidentifyid;  //--设备ID
    private String devicetype;  //--设备类型
    private String useridto;  //用户ID

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setUseridto(String useridto) {
        this.useridto = useridto;
    }

}
