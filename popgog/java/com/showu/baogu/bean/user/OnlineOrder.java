package com.showu.baogu.bean.user;

import com.showu.baogu.bean.BaseBean;
import com.showu.common.josn.EAJson;

/**
 * Created by showu on 13-7-25.
 */
public class OnlineOrder extends BaseBean{

    @EAJson private String uorderid;    //订单ID
    @EAJson private String createdate;    //下单时间
    @EAJson private String cinemaname;    //影院名称
    @EAJson private String filmname;    //影片名称
    @EAJson private String statusremark;    //订单状态描述
    @EAJson private String showdate;    //场次时间
    @EAJson private String planguage;    //影片语言
    @EAJson private String pdimenstional;    //影片维数
    @EAJson private String seatlist;    //座位
    @EAJson private String phone;    //手机号码
    @EAJson private String totalprice;    //总价
    @EAJson private String pconfirmationid;    //取票号
    @EAJson private String qrcodeurl;//    二维码
    @EAJson private String showimgurl;//   座位图
    @EAJson private String hallname;//  影厅
    @EAJson
    private String frontcover;  //封面

    public String getFrontcover() {
        return frontcover;
    }

    public String getHallname() {
        return hallname;
    }

    public void setHallname(String hallname) {
        this.hallname = hallname;
    }

    public void setFrontcover(String frontcover) {
        this.frontcover = frontcover;
    }

    public String getShowimgurl() {
        return showimgurl;
    }

    public void setShowimgurl(String showimgurl) {
        this.showimgurl = showimgurl;
    }

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

    public String getFilmname() {
        return filmname;
    }

    public void setFilmname(String filmname) {
        this.filmname = filmname;
    }

    public String getStatusremark() {
        return statusremark;
    }

    public void setStatusremark(String statusremark) {
        this.statusremark = statusremark;
    }

    public String getShowdate() {
        return showdate;
    }

    public void setShowdate(String showdate) {
        this.showdate = showdate;
    }

    public String getPlanguage() {
        return planguage;
    }

    public void setPlanguage(String planguage) {
        this.planguage = planguage;
    }

    public String getPdimenstional() {
        return pdimenstional;
    }

    public void setPdimenstional(String pdimenstional) {
        this.pdimenstional = pdimenstional;
    }

    public String getSeatlist() {
        return seatlist;
    }

    public void setSeatlist(String seatlist) {
        this.seatlist = seatlist;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getPconfirmationid() {
        return pconfirmationid;
    }

    public void setPconfirmationid(String pconfirmationid) {
        this.pconfirmationid = pconfirmationid;
    }

    public String getQrcodeurl() {
        return qrcodeurl;
    }

    public void setQrcodeurl(String qrcodeurl) {
        this.qrcodeurl = qrcodeurl;
    }

}
