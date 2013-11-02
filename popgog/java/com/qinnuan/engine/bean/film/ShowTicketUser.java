package com.qinnuan.engine.bean.film;

import java.io.Serializable;

import com.qinnuan.common.josn.EAJson;

public class ShowTicketUser implements Serializable {
	
	@EAJson private String userid;          //用户ID
	@EAJson private String profileimg;      //头像
	@EAJson private String uorderid;        //订单ID
    @EAJson private String togetherseefilmid;  //一起看电影ID


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

    public String getUorderid() {
        return uorderid;
    }

    public void setUorderid(String uorderid) {
        this.uorderid = uorderid;
    }

    public String getTogetherseefilmid() {
        return togetherseefilmid;
    }

    public void setTogetherseefilmid(String togetherseefilmid) {
        this.togetherseefilmid = togetherseefilmid;
    }

}
