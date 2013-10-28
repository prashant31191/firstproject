package com.showu.baogu.bean;

import com.showu.common.josn.EAJson;

/**
 * Created by Administrator on 13-7-19.
 */
public class Openfire extends  BaseBean {

    @EAJson private String openfireserver;      //openfire服务地址
    @EAJson private int openfireport;           //openfire端口
    @EAJson private String openfirepassword;    //openfire明文密码

    public Openfire() {

    }

    public Openfire(String openfireserver, int openfireport, String openfirepassword) {
        this.openfireserver = openfireserver;
        this.openfireport = openfireport;
        this.openfirepassword = openfirepassword;
    }

    public Openfire(User user) {
        this.openfireserver = user.getOpenfireserver();
        this.openfireport = user.getOpenfireport();
        this.openfirepassword = user.getOpenfirepassword();
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

    @Override
    public String toString() {
        return "address:"+openfireserver+" port"+openfireport ;
    }
}
