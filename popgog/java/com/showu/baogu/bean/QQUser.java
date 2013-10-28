package com.showu.baogu.bean;

import com.showu.common.josn.EAJson;

/**
 * Created by Administrator on 13-7-19.
 */
public class QQUser extends BaseBean {

    @EAJson private String is_yellow_year_vip;      //":"0"
    @EAJson private int ret;                        //:0,
    @EAJson private String figureurl_qq_1;          //":"http:\/\/q.qlogo.cn\/qqapp\/100326015\/1FC1DFBF0688463C039467C04DC4A8D5\/40",
    @EAJson private String figureurl_qq_2;          //":"http:\/\/q.qlogo.cn\/qqapp\/100326015\/1FC1DFBF0688463C039467C04DC4A8D5\/100",
    @EAJson private String nickname;                //":"景辉",
    @EAJson private String yellow_vip_level;        //":"0",
    @EAJson private String msg;                     //":"",
    @EAJson private String figureurl_1;             //":"http:\/\/qzapp.qlogo.cn\/qzapp\/100326015\/1FC1DFBF0688463C039467C04DC4A8D5\/50",
    @EAJson private String vip;                     //":"0",
    @EAJson private String level;                   //":"0",
    @EAJson private String figureurl_2;             //":"http:\/\/qzapp.qlogo.cn\/qzapp\/100326015\/1FC1DFBF0688463C039467C04DC4A8D5\/100",
    @EAJson private String is_yellow_vip;           //":"0",
    @EAJson private String gender;                  //":"男",
    @EAJson private String figureurl;               //":"http:\/\/qzapp.qlogo.cn\/qzapp\/100326015\/1FC1DFBF0688463C039467C04DC4A8D5\/30"

    public String getIs_yellow_year_vip() {
        return is_yellow_year_vip;
    }

    public void setIs_yellow_year_vip(String is_yellow_year_vip) {
        this.is_yellow_year_vip = is_yellow_year_vip;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getFigureurl_qq_1() {
        return figureurl_qq_1;
    }

    public void setFigureurl_qq_1(String figureurl_qq_1) {
        this.figureurl_qq_1 = figureurl_qq_1;
    }

    public String getFigureurl_qq_2() {
        return figureurl_qq_2;
    }

    public void setFigureurl_qq_2(String figureurl_qq_2) {
        this.figureurl_qq_2 = figureurl_qq_2;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getYellow_vip_level() {
        return yellow_vip_level;
    }

    public void setYellow_vip_level(String yellow_vip_level) {
        this.yellow_vip_level = yellow_vip_level;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFigureurl_1() {
        return figureurl_1;
    }

    public void setFigureurl_1(String figureurl_1) {
        this.figureurl_1 = figureurl_1;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFigureurl_2() {
        return figureurl_2;
    }

    public void setFigureurl_2(String figureurl_2) {
        this.figureurl_2 = figureurl_2;
    }

    public String getIs_yellow_vip() {
        return is_yellow_vip;
    }

    public void setIs_yellow_vip(String is_yellow_vip) {
        this.is_yellow_vip = is_yellow_vip;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFigureurl() {
        return figureurl;
    }

    public void setFigureurl(String figureurl) {
        this.figureurl = figureurl;
    }

}
