package com.qinnuan.engine.bean;

import com.qinnuan.common.josn.EAJson;

/**
 * Created by showu on 13-7-20.
 */
 public class OtherUserInfo {
    @EAJson
    private String userid;  //用户ID
    @EAJson
    private int usertype;  //用户类型 0=普通用户1=枪手用户2=官方/系统用户3=公共帐号
    @EAJson
    private String popgogid;  //爆谷号
    @EAJson
    private String nickname;  //昵称
    @EAJson
    private String remarkname;  //备注名
    @EAJson
    private String profileimg;  //头像
    @EAJson
    private String signature;  //签名
    @EAJson
    private int sex;  //性别 1=男 2=女 0=未知
    @EAJson
    private int distance;  //距离(米)
    @EAJson
    private int datediff;  //时间差(分钟)
    @EAJson
    private int isfriend;  //是否好友 0=否 1=是

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public String getPopgogid() {
        return popgogid;
    }

    public void setPopgogid(String popgogid) {
        this.popgogid = popgogid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRemarkname() {
        return remarkname;
    }

    public void setRemarkname(String remarkname) {
        this.remarkname = remarkname;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDatediff() {
        return datediff;
    }

    public void setDatediff(int datediff) {
        this.datediff = datediff;
    }

    public int getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(int isfriend) {
        this.isfriend = isfriend;
    }
}
