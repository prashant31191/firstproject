package com.showu.baogu.bean;

import com.showu.common.josn.EAJson;

public class Coupon extends BaseBean {

    @EAJson private String ucashcouponid;    //代金券ID
    @EAJson private int cashamount;    //面值
    @EAJson private int status;    //状态0=未使用1=使用中2=已使用3已过期
    @EAJson private String expireddate;    //过期时间

    public String getUcashcouponid() {
        return ucashcouponid;
    }

    public void setUcashcouponid(String ucashcouponid) {
        this.ucashcouponid = ucashcouponid;
    }

    public int getCashamount() {
        return cashamount;
    }

    public void setCashamount(int cashamount) {
        this.cashamount = cashamount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getExpireddate() {
        return expireddate;
    }

    public void setExpireddate(String expireddate) {
        this.expireddate = expireddate;
    }
}
