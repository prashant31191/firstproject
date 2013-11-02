package com.qinnuan.engine.api.film;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

/**
 * Created by showu on 13-8-9.
 */
@RequestType(type = HttpMethod.POST)
public class AddOrderTicket extends AbstractParam {
    private final String api="/api/film/add_order_ticket.api";
    @Override
    public String getApi() {
        return api;
    }
    private String cinemaeticketid  ;//影院电子券ID
    private int ticketcount  ;//票数
    private  float totalprice  ;//总价
    private String phone  ;//手机
    private String userid  ;//用户ID
    private String deviceidentifyid  ;//设备ID

    public void setCinemaeticketid(String cinemaeticketid) {
        this.cinemaeticketid = cinemaeticketid;
    }

    public void setTicketcount(int ticketcount) {
        this.ticketcount = ticketcount;
    }

    public void setTotalprice(float totalprice) {
        this.totalprice = totalprice;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    private final int devicetype =1 ;//设备类型 0=ios 1=android
}
