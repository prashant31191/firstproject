package com.qinnuan.engine.bean;

import com.qinnuan.common.josn.EAJson;

/**
 * Created by Administrator on 13-7-17.
 */
public class Comment extends BaseBean {

    @EAJson private String userid;                  //用户ID
    @EAJson private String nickname;                //用户名称
    @EAJson private String content;                 //内容
    @EAJson private String cdynamiccommentid;       //评论ID
    @EAJson private String createdate;              //评论时间
    @EAJson private String refcdynamiccommentid;    //评论ID
    @EAJson private String rootcdynamiccommentid;    //评论ID
    @EAJson private String profileimg;               //用户头像
    @EAJson private String useridto;                //被评论用户id
    @EAJson private String nicknameto;              //被评论用户名称


    public Comment() { }

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCdynamiccommentid() {
        return cdynamiccommentid;
    }

    public void setCdynamiccommentid(String cdynamiccommentid) {
        this.cdynamiccommentid = cdynamiccommentid;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getRefcdynamiccommentid() {
        return refcdynamiccommentid;
    }

    public void setRefcdynamiccommentid(String refcdynamiccommentid) {
        this.refcdynamiccommentid = refcdynamiccommentid;
    }

    public String getRootcdynamiccommentid() {
        return rootcdynamiccommentid;
    }

    public void setRootcdynamiccommentid(String rootcdynamiccommentid) {
        this.rootcdynamiccommentid = rootcdynamiccommentid;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

    public String getUseridto() {
        return useridto;
    }

    public void setUseridto(String useridto) {
        this.useridto = useridto;
    }

    public String getNicknameto() {
        return nicknameto;
    }

    public void setNicknameto(String nicknameto) {
        this.nicknameto = nicknameto;
    }
}
