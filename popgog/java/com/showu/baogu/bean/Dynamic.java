package com.showu.baogu.bean;

import com.showu.common.josn.EAJson;

import java.util.List;

public class Dynamic extends BaseBean {

    @EAJson private String cinemadynamicid;     //动态ID
    @EAJson private String userid;              //用户ID
    @EAJson private int dynamictype;            //动态类型 0=一起看电影1=打算去看(喜欢影片)2=自己发表
    @EAJson private String content;             // = 2 动态内容
    @EAJson private String imagelist;           //动态图片
    @EAJson private String togetherseefilmid;   // =0 一起看电影ID
    @EAJson private String uorderid;            // =0 订单ID
    @EAJson private String filmname;            // =1 影片名称
    @EAJson private String frontcover;          // =1 封面
    @EAJson private String trailer;             // =1 预告片
    @EAJson private String likecdynamicid;      //赞ID
    @EAJson private String createdate;          //动态发布时间
    @EAJson private String nickname;            //用户名称
    @EAJson private String profileimg;          //用户头像
    @EAJson private String showimgurl;//      一起看电影图
    private List<Like> likes;                       //赞
    private List<Comment> comments;                 //评论

    public String getCinemadynamicid() {
        return cinemadynamicid;
    }

    public void setCinemadynamicid(String cinemadynamicid) {
        this.cinemadynamicid = cinemadynamicid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getDynamictype() {
        return dynamictype;
    }

    public void setDynamictype(int dynamictype) {
        this.dynamictype = dynamictype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagelist() {
        return imagelist;
    }

    public void setImagelist(String imagelist) {
        this.imagelist = imagelist;
    }

    public String getTogetherseefilmid() {
        return togetherseefilmid;
    }

    public void setTogetherseefilmid(String togetherseefilmid) {
        this.togetherseefilmid = togetherseefilmid;
    }

    public String getUorderid() {
        return uorderid;
    }

    public void setUorderid(String uorderid) {
        this.uorderid = uorderid;
    }

    public String getFilmname() {
        return filmname;
    }

    public void setFilmname(String filmname) {
        this.filmname = filmname;
    }

    public String getFrontcover() {
        return frontcover;
    }

    public void setFrontcover(String frontcover) {
        this.frontcover = frontcover;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getLikecdynamicid() {
        return likecdynamicid;
    }

    public void setLikecdynamicid(String likecdynamicid) {
        this.likecdynamicid = likecdynamicid;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
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

    public String getShowimgurl() {
        return showimgurl;
    }

    public void setShowimgurl(String showimgurl) {
        this.showimgurl = showimgurl;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
