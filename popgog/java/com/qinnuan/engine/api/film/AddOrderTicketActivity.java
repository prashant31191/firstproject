package com.qinnuan.engine.api.film;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

/**
 * Created by showu on 13-8-14.
 */
@RequestType(type = HttpMethod.POST)
public class AddOrderTicketActivity extends AbstractParam {
    private final String api="/api/activity/add_order_ticket_activity.api" ;
    @Override
    public String getApi() {
        return api ;
    }
    private String userid  ;//用户ID 登录传
    private String deviceidentifyid  ;//设备ID 同上
    private final int devicetype=1  ;//设备类型 0=ios 1=android 同上
    private String officialactivityid  ;//活动ID
    private String acteticcinemaid  ;//活动影院电子券ID
    private int ticketcount  ;//数量
    private float totalprice  ;//总价
    private String phone  ;//手机号码

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setOfficialactivityid(String officialactivityid) {
        this.officialactivityid = officialactivityid;
    }

    public void setActeticcinemaid(String acteticcinemaid) {
        this.acteticcinemaid = acteticcinemaid;
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
}
