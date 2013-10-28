package com.showu.baogu.bean.film;

import com.showu.common.josn.EAJson;
import com.showu.common.util.TextUtil;

/**
 * Created by showu on 13-7-26.
 */
public class ShowInfoBean {
    @EAJson
    private String showinfoid;  //场次ID
    @EAJson
    private String showdate;  //日期
    @EAJson
    private String showtime;  //时间
    @EAJson
    private String language;  //语言
    @EAJson
    private String dimenstional;  //维数
    @EAJson
    private int issellticket;  //是否支持售票 0=否 1=是
    @EAJson
    private String price;  //价格
    @EAJson
    private int maxticketnum;  //最大值
    @EAJson
    private String hallname;  //影厅名称
    @EAJson
    private int isexpired;   //场次是否过期 0=否1=是
    @EAJson
    private String cinemaname  ;//影院名称
    @EAJson
    private String filmname  ;//影片名称
    @EAJson
    private String frontcover  ;//封面
    @EAJson
    private String planguage;
    @EAJson
    private String pdimenstional;


    public String getPlanguage() {
        return planguage;
    }

    public void setPlanguage(String planguage) {
        this.planguage = planguage;
    }

    public String getPdimenstional() {
        return pdimenstional;
    }

    public void setPdimenstional(String pdimenstional) {
        this.pdimenstional = pdimenstional;
    }

    public String getCinemaname() {
        return cinemaname;
    }

    public void setCinemaname(String cinemaname) {
        this.cinemaname = cinemaname;
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

    public int getIsexpired() {
        return isexpired;
    }

    public void setIsexpired(int isexpired) {
        this.isexpired = isexpired;
    }

    public String getShowinfoid() {
        return showinfoid;
    }

    public void setShowinfoid(String showinfoid) {
        this.showinfoid = showinfoid;
    }

    public String getShowdate() {
        return showdate;
    }

    public void setShowdate(String showdate) {
        this.showdate = showdate;
    }

    public String getShowtime() {
        if(TextUtil.isEmpty(showtime)) return "";
        return showtime;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDimenstional() {
        return dimenstional;
    }

    public void setDimenstional(String dimenstional) {
        this.dimenstional = dimenstional;
    }

    public int getIssellticket() {
        return issellticket;
    }

    public void setIssellticket(int issellticket) {
        this.issellticket = issellticket;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getMaxticketnum() {
        return maxticketnum;
    }

    public void setMaxticketnum(int maxticketnum) {
        this.maxticketnum = maxticketnum;
    }

    public String getHallname() {
        return hallname;
    }

    public void setHallname(String hallname) {
        this.hallname = hallname;
    }
}
