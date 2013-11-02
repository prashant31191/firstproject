package com.qinnuan.engine.bean;

import com.qinnuan.common.josn.EAJson;

/**
 * Created by Administrator on 13-7-17.
 */
public class Like extends BaseBean {

    @EAJson private String userid;//用户ID
    @EAJson private String nickname;//用户名称
    @EAJson private String likedynamicid;//赞ID
    @EAJson private String profileimg;//用户头像


    public Like() { }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLikedynamicid() {
        return likedynamicid;
    }

    public void setLikedynamicid(String likedynamicid) {
        this.likedynamicid = likedynamicid;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

}
