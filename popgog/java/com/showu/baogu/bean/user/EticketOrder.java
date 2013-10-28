package com.showu.baogu.bean.user;

import com.showu.baogu.bean.BaseBean;
import com.showu.common.josn.EAJson;

import java.util.List;

/**
 * Created by showu on 13-7-25.
 */
public class EticketOrder extends BaseBean {

    @EAJson private String uorderid;    //订单ID
    @EAJson private String createdate;    //下单时间
    @EAJson private String cinemaname;    //影院名称
    @EAJson private String statusremark;    //订单状态描述
    @EAJson private String phone;    //手机号码
    @EAJson private int totalprice;    //总价
    @EAJson private int ticketcount; //  数量
    @EAJson private int tickettype; //类型 0=2D 1=3D

    public List<Qrcode> getQrcodes() {
        return qrcodes;
    }

    public void setQrcodes(List<Qrcode> qrcodes) {
        this.qrcodes = qrcodes;
    }

    private List<Qrcode> qrcodes;

    public String getUorderid() {
        return uorderid;
    }

    public void setUorderid(String uorderid) {
        this.uorderid = uorderid;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getCinemaname() {
        return cinemaname;
    }

    public void setCinemaname(String cinemaname) {
        this.cinemaname = cinemaname;
    }

    public String getStatusremark() {
        return statusremark;
    }

    public void setStatusremark(String statusremark) {
        this.statusremark = statusremark;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public int getTicketcount() {
        return ticketcount;
    }

    public void setTicketcount(int ticketcount) {
        this.ticketcount = ticketcount;
    }

    public int getTickettype() {
        return tickettype;
    }

    public void setTickettype(int tickettype) {
        this.tickettype = tickettype;
    }
}
