package com.showu.baogu.api.film;

import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

/**
 * Created by showu on 13-7-29.
 */
@RequestType(type= HttpMethod.POST)
public class UpdateOrderconfirm extends AbstractParam {
    private final  String api="/api/film/update_orderconfirm.api" ;
    private String uorderid  ;//订单ID
    private int payment  ;//支付方式 0=余额支付1=支付宝(网页) 2=支付宝(客户端)3=银联

    public void setUorderid(String uorderid) {
        this.uorderid = uorderid;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    @Override
    public String getApi() {
        return api;
    }
}
