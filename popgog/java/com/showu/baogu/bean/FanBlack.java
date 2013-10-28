package com.showu.baogu.bean;

import com.showu.common.josn.EAJson;

import java.util.List;

/**
 * Created by Administrator on 13-7-16.
 */
public class FanBlack extends BaseBean {

    @EAJson private String userid;      //黑名单用户ID
    private String mineid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMineid() {
        return mineid;
    }

    public void setMineid(String mineid) {
        this.mineid = mineid;
    }
//    public String getBlackid() {
//        return blackid;
//    }
//
//    public void setBlackid(String blackid) {
//        this.blackid = blackid;
//    }
}
