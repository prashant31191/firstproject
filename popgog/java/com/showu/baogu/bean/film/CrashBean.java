package com.showu.baogu.bean.film;

import com.showu.common.josn.EAJson;

/**
 * Created by showu on 13-7-29.
 */
public class CrashBean {


    @EAJson
    private int cashamount ;
    @EAJson
    private String ucashcouponid ;

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


}
