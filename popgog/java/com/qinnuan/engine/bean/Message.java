package com.qinnuan.engine.bean;

import com.qinnuan.common.josn.EAJson;

/**
 * Created by Administrator on 13-7-17.
 */
public class Message extends BaseBean {

    @EAJson
    private String nickname;                //用户名称
    @EAJson private String content;                 //动态内容
    @EAJson private String createdate;              //消息时间
    @EAJson private String profileimg;               //用户头像
    @EAJson private String cdynamicmessageid;//消息ID
    @EAJson private int msgtype;//消息类型 0=赞 1=评论
    @EAJson private String cinemadynamicid;//动态ID
    @EAJson private int dynamictype;//动态类型 0=一起看电影1=打算去看(喜欢影片)2=自己发表
    @EAJson private String refid;//引用ID
    @EAJson private String userid;//用户ID
    @EAJson private int islike; //是否赞 0=否 1=是
    @EAJson private String dynamicimage;  //--动态图片

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

    public String getCdynamicmessageid() {
        return cdynamicmessageid;
    }

    public void setCdynamicmessageid(String cdynamicmessageid) {
        this.cdynamicmessageid = cdynamicmessageid;
    }

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public String getCinemadynamicid() {
        return cinemadynamicid;
    }

    public void setCinemadynamicid(String cinemadynamicid) {
        this.cinemadynamicid = cinemadynamicid;
    }

    public int getDynamictype() {
        return dynamictype;
    }

    public void setDynamictype(int dynamictype) {
        this.dynamictype = dynamictype;
    }

    public String getRefid() {
        return refid;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getIslike() {
        return islike;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }

    public String getDynamicimage() {
        return dynamicimage;
    }

    public void setDynamicimage(String dynamicimage) {
        this.dynamicimage = dynamicimage;
    }

}
