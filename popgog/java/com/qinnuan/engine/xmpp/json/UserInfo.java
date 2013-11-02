package com.qinnuan.engine.xmpp.json;

import com.google.gson.Gson;
import com.qinnuan.common.josn.EAJson;

/**
 * Created by showu on 13-7-16.
 */
public class UserInfo {
    @EAJson
    private String  nickname ;
    @EAJson
    private String headImage ;
//    @EAJson
//    private String distance;
    @EAJson
    private String userid ;
    @EAJson
    private String groupname;
    @EAJson
    private String filmimgurl;
    @EAJson
    private int membercount;


    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getFilmimgurl() {
        return filmimgurl;
    }

    public void setFilmimgurl(String filmimgurl) {
        this.filmimgurl = filmimgurl;
    }

    public int getMembercount() {
        return membercount;
    }

    public void setMembercount(int membercount) {
        this.membercount = membercount;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }



//    public String getDistance() {
//        return distance;
//    }

//    public void setDistance(String distance) {
//        this.distance = distance;
//    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this) ;
    }
}
