package com.qinnuan.engine.bean;

import com.qinnuan.common.josn.EAJson;

/**
 * Created by Administrator on 13-7-16.
 */
public class Sns extends BaseBean {

    @EAJson
    private int snstype;                //类型 0=新浪微博 1=QQ
    @EAJson private String userid;              //用户ID
    @EAJson private String snsuserid;           //第三方ID
    @EAJson private String accesstoken;         //访问token sina
    @EAJson private String accesstokensecret;   //token密钥 sina
    @EAJson private String openid;              //腾讯微博ID
    @EAJson private String openkey;             //腾讯微博ID对应Key X
    @EAJson private int isverified;             //是否认证 sina
    @EAJson private String expireddate;         //过期时间
    @EAJson private String snsid;               //SNSID sina
    @EAJson private String snsname;             //第三方名称 sina

    public String getSnsname() {
        return snsname;
    }

    public void setSnsname(String snsname) {
        this.snsname = snsname;
    }

    public String getSnsid() {
        return snsid;
    }

    public void setSnsid(String snsid) {
        this.snsid = snsid;
    }

    public void setIsverified(int isverified) {
        this.isverified = isverified;
    }

    public void setSnstype(int snstype) {
        this.snstype = snstype;
    }

    public Integer getSnstype() {
        return snstype;
    }

    public void setSnstype(Integer snstype) {
        this.snstype = snstype;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSnsuserid() {
        return snsuserid;
    }

    public void setSnsuserid(String snsuserid) {
        this.snsuserid = snsuserid;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getAccesstokensecret() {
        return accesstokensecret;
    }

    public void setAccesstokensecret(String accesstokensecret) {
        this.accesstokensecret = accesstokensecret;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOpenkey() {
        return openkey;
    }

    public void setOpenkey(String openkey) {
        this.openkey = openkey;
    }

    public Integer getIsverified() {
        return isverified;
    }

    public void setIsverified(Integer isverified) {
        this.isverified = isverified;
    }

    public String getExpireddate() {
        return expireddate;
    }

    public void setExpireddate(String expireddate) {
        this.expireddate = expireddate;
    }


}
