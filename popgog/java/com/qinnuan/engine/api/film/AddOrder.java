package com.qinnuan.engine.api.film;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

/**
 * Created by showu on 13-7-29.
 */
@RequestType(type= HttpMethod.POST)
public class AddOrder extends AbstractParam {
    private final String api="/api/film/add_order_online.api" ;
    @Override
    public String getApi() {
        return api;
    }
    private String showinfoid  ;//场次ID
    private String seatlist  ;//座位列表 1:01|1:02
    private int ticketcount  ;//票数
    private String totalprice  ;//总价
    private String phone  ;//手机
    private String userid  ;//用户ID
    private String deviceidentifyid  ;//设备ID
    private int devicetype =1 ;//设备类型 0=ios 1=android
    private String togetherseefilmid  ;//一起看电影ID(ordertype = 6 并且 参加别人发布的场次)
    private String showimgurl  ;//一起看电影座位图(ordertype = 6 并且 参加别人发布的场次)
    private  int isshow;//0=否 1=是
    public void setTogetherseefilmid(String togetherseefilmid) {
        this.togetherseefilmid = togetherseefilmid;
    }

    public int getIsshow() {
        return isshow;
    }

    public void setIsshow(int isshow) {
        this.isshow = isshow;
    }

    public void setShowimgurl(String showimgurl) {
        this.showimgurl = showimgurl;
    }

    private int ordertype=0 ;

    public int getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(int ordertype) {
        this.ordertype = ordertype;
    }

    public void setShowinfoid(String showinfoid) {
        this.showinfoid = showinfoid;
    }

    public void setSeatlist(String seatlist) {
        this.seatlist = seatlist;
    }

    public void setTicketcount(int ticketcount) {
        this.ticketcount = ticketcount;
    }

    public void setTotalprice(String totalprice) {
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

    public void setDevicetype(int devicetype) {
        this.devicetype = devicetype;
    }
}
