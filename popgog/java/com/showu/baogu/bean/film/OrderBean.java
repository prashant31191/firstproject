package com.showu.baogu.bean.film;

import com.showu.common.josn.EAJson;

import java.util.List;

/**
 * Created by showu on 13-7-29.
 */
public class OrderBean {
    @EAJson
    private String uorderid;  //订单ID
    @EAJson
    private String totalcashremain;  //余额
    @EAJson
    private String ordertime;  //下单时间

    private List<CrashBean> crashBeanList;

    public List<CrashBean> getCrashBeanList() {
        return crashBeanList;
    }

    public void setCrashBeanList(List<CrashBean> crashBeanList) {
        this.crashBeanList = crashBeanList;
    }

    public String getUorderid() {
        return uorderid;
    }

    public String getTotalcashremain() {
        return totalcashremain;
    }

    public String getOrdertime() {
        return ordertime;
    }
}
