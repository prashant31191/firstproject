package com.showu.baogu.api;

import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-6-19.
 */
@RequestType(type = HttpMethod.POST)
public class UpdateUserPassword extends AbstractParam {
    private String api="/api/user/update_user_password.api" ;
    private String userid ; //用户ID
    private String oldpassword;  //原密码
    private String newpassword ; //新密码
    private String deviceidentifyid ;
    private int devicetype=1 ;

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    @Override
    public String getApi() {
        return api;
    }
}
