package com.qinnuan.engine.bean.film;

import com.qinnuan.common.josn.EAJson;

/**
 * Created by showu on 13-7-25.
 */
public class FilmBean {
    @EAJson
    private String filmid;  //影片ID
    @EAJson
    private String frontcover;  //封面
    @EAJson
    private String trailer;  //预告片
    @EAJson
    private String filmname;  //影片名称
    @EAJson
    private String showdate;  //上映日期
    @EAJson
    private String country;  //地区
    @EAJson
    private String typelist;  //影片类型
    @EAJson
    private String dimenstional;  //维数
    @EAJson
    private String duration;  //片长(分钟)
    @EAJson
    private String director;  //导演
    @EAJson
    private String actorlist;  //演员列表
    @EAJson
    private int totalwantnum;  //总打算去看数
    @EAJson
    private String iconimage;  //icon图片
    @EAJson
    private int showstatus;  //影片状态 =即将上映1=正在上映2=已下映
    @EAJson
    private String ufilmid;  //当前用户打算去看ID
    @EAJson
    private int cinemanum;    //上映影院数量
    @EAJson
    private String imagelist;  //影片剧照
    @EAJson
    private int issellticket;  //是否有购票影院

    private boolean isRequest=false;

    public boolean isRequest() {
        return isRequest;
    }

    public void setRequest(boolean request) {
        isRequest = request;
    }

    public String getImagelist() {
        return imagelist;
    }

    public void setImagelist(String imagelist) {
        this.imagelist = imagelist;
    }

    public int getIssellticket() {
        return issellticket;
    }

    public void setIssellticket(int issellticket) {
        this.issellticket = issellticket;
    }

    public void setFilmid(String filmid) {
        this.filmid = filmid;
    }

    public void setFrontcover(String frontcover) {
        this.frontcover = frontcover;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public void setFilmname(String filmname) {
        this.filmname = filmname;
    }

    public void setShowdate(String showdate) {
        this.showdate = showdate;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setTypelist(String typelist) {
        this.typelist = typelist;
    }

    public void setDimenstional(String dimenstional) {
        this.dimenstional = dimenstional;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setActorlist(String actorlist) {
        this.actorlist = actorlist;
    }

    public void setTotalwantnum(int totalwantnum) {
        this.totalwantnum = totalwantnum;
    }

    public void setIconimage(String iconimage) {
        this.iconimage = iconimage;
    }

    public void setShowstatus(int showstatus) {
        this.showstatus = showstatus;
    }

    public void setUfilmid(String ufilmid) {
        this.ufilmid = ufilmid;
    }

    public String getFilmid() {
        return filmid;
    }

    public String getFrontcover() {
        return frontcover;
    }

    public String getTrailer() {
        return trailer;
    }

    public String getFilmname() {
        return filmname;
    }

    public String getShowdate() {
        return showdate;
    }

    public String getCountry() {
        return country;
    }

    public String getTypelist() {
        return typelist;
    }

    public String getDimenstional() {
        return dimenstional;
    }

    public String getDuration() {
        return duration;
    }

    public String getDirector() {
        return director;
    }

    public String getActorlist() {
        return actorlist;
    }

    public int getTotalwantnum() {
        return totalwantnum;
    }

    public String getIconimage() {
        return iconimage;
    }

    public int getShowstatus() {
        return showstatus;
    }

    public String getUfilmid() {
        return ufilmid;
    }

    public int getCinemanum() {
        return cinemanum;
    }

    public void setCinemanum(int cinemanum) {
        this.cinemanum = cinemanum;
    }

}
