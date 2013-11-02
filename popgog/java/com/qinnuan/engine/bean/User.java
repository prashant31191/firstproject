package com.qinnuan.engine.bean;

import com.qinnuan.common.josn.EAJson;

import java.util.List;

/**
 * Created by Administrator on 13-7-16.
 */
public class User extends BaseBean {

    @EAJson
    private String userid;
    @EAJson private String popgogid;
    @EAJson private String nickname;
    @EAJson private String profileimg;
    @EAJson private String signature;
    @EAJson private int sex;//性别 1=男 2=女 0=未知
    @EAJson private String phone;
    @EAJson private int isphoneverify; //手机是否验证 0=否 1=是(高亮显示)
    @EAJson private float totalcashremain;
    @EAJson private String openfireserver;
    @EAJson private int openfireport;
    @EAJson private String openfirepassword;
    @EAJson private int usertype;
    @EAJson private int isshow;             //是否在附近出现 0=否 1=是
    @EAJson private String dynamicbackimage;  //动态背景图
    @EAJson private int ispassword;         //是否需要设置密码0=不，1=需要

    private List<SNSBean> snsList ;

    public List<SNSBean> getSnsList() {
        return snsList;
    }

    public void setSnsList(List<SNSBean> snsList) {
        this.snsList = snsList;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIsphoneverify() {
        return isphoneverify;
    }

    public void setIsphoneverify(int isphoneverify) {
        this.isphoneverify = isphoneverify;
    }

    public float getTotalcashremain() {
        return totalcashremain;
    }

    public void setTotalcashremain(float totalcashremain) {
        this.totalcashremain = totalcashremain;
    }

    public String getOpenfireserver() {
        return openfireserver;
    }

    public void setOpenfireserver(String openfireserver) {
        this.openfireserver = openfireserver;
    }

    public int getOpenfireport() {
        return openfireport;
    }

    public void setOpenfireport(int openfireport) {
        this.openfireport = openfireport;
    }

    public String getOpenfirepassword() {
        return openfirepassword;
    }

    public void setOpenfirepassword(String openfirepassword) {
        this.openfirepassword = openfirepassword;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public int getIsshow() {
        return isshow;
    }

    public void setIsshow(int isshow) {
        this.isshow = isshow;
    }

    public String getDynamicbackimage() {
        return dynamicbackimage;
    }

    public void setDynamicbackimage(String dynamicbackimage) {
        this.dynamicbackimage = dynamicbackimage;
    }

    public int getIspassword() {
        return ispassword;
    }

    public void setIspassword(int ispassword) {
        this.ispassword = ispassword;
    }
}
