package com.showu.baogu.bean;

import com.showu.common.josn.EAJson;

import java.util.List;

/**
 * Created by Administrator on 13-7-16.
 */
public class DynamicMsg extends BaseBean {

    @EAJson private String profileimg; //用户头像
    @EAJson private int msgcount;//新消息数量

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

    public int getMsgcount() {
        return msgcount;
    }

    public void setMsgcount(int msgcount) {
        this.msgcount = msgcount;
    }

}
