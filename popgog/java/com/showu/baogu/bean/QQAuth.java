package com.showu.baogu.bean;

import com.showu.common.josn.EAJson;

/**
 * Created by Administrator on 13-7-19.
 */
public class QQAuth extends BaseBean {
    @EAJson private int ret;                //0,
    @EAJson private String pay_token;       //910FE10A6E158090D08662A1138D2F43;
    @EAJson private String pf;              //openmobile_android;
    @EAJson private String expires_in;      //7776000;
    @EAJson private String openid;          //1FC1DFBF0688463C039467C04DC4A8D5;
    @EAJson private String pfkey;           //c46024480f173e518efe3494a65a1be1;
    @EAJson private String msg;             //sucess;
    @EAJson private String access_token;    //3A0A3CC95277DB3E5A739261FBB56984"

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getPay_token() {
        return pay_token;
    }

    public void setPay_token(String pay_token) {
        this.pay_token = pay_token;
    }

    public String getPf() {
        return pf;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPfkey() {
        return pfkey;
    }

    public void setPfkey(String pfkey) {
        this.pfkey = pfkey;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

}
