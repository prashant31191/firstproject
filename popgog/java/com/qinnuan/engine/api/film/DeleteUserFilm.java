package com.qinnuan.engine.api.film;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

/**
 * Created by showu on 13-7-25.
 */
@RequestType(type= HttpMethod.POST)
public class DeleteUserFilm extends AbstractParam {
    private final String api="/api/filmfans/delete_user_film.api" ;
    @Override
    public String getApi() {
        return api;
    }
    private String ufilmid  ;//打算去看ID
    private String userid  ;//用户ID
    private String deviceidentifyid  ;//设备ID

    public void setDevicetype(int devicetype) {
        this.devicetype = devicetype;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUfilmid(String ufilmid) {
        this.ufilmid = ufilmid;
    }

    private int devicetype = 1   ;//设备类型 0=ios 1=android

}
