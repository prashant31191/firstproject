package com.qinnuan.engine.api.film;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

/**
 * Created by showu on 13-8-9.
 */
@RequestType(type = HttpMethod.GET)
public class GetCinemaEticket extends AbstractParam {
    private final String api="/api/film/get_cinema_eticket.api" ;
    @Override
    public String getApi() {
        return api;
    }
    private String userid  ;//g用户ID
    private String deviceidentifyid  ;//g设备ID
    private final int devicetype=1  ;//g设备类型 0=ios 1=android
    private String useridto  ;//g商户用户ID
    private String logincityid  ;//g城市ID

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDeviceidentifyid() {
        return deviceidentifyid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public int getDevicetype() {
        return devicetype;
    }

    public String getUseridto() {
        return useridto;
    }

    public void setUseridto(String useridto) {
        this.useridto = useridto;
    }

    public String getLogincityid() {
        return logincityid;
    }

    public void setLogincityid(String logincityid) {
        this.logincityid = logincityid;
    }
}
