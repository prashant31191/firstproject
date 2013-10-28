package com.showu.baogu.bean.film;

import java.util.List;

/**
 * Created by showu on 13-8-9.
 */
public class TickitOrderBean {
    private String uorderid;  //订单ID
    private float totalcashremain;  //余额
    private String ordertime;  //下单时间

    public List<CrashBean> getUcashcoupon() {
        return ucashcoupon;
    }

    public void setUcashcoupon(List<CrashBean> ucashcoupon) {
        this.ucashcoupon = ucashcoupon;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public float getTotalcashremain() {
        return totalcashremain;
    }

    public void setTotalcashremain(float totalcashremain) {
        this.totalcashremain = totalcashremain;
    }

    public String getUorderid() {
        return uorderid;
    }

    public void setUorderid(String uorderid) {
        this.uorderid = uorderid;
    }

    private List<CrashBean> ucashcoupon ;
}
