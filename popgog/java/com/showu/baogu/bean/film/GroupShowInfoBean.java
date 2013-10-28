package com.showu.baogu.bean.film;

import java.util.List;

/**
 * Created by showu on 13-8-12.
 */
public class GroupShowInfoBean {
    private String showinfoid  ;//场次ID
    private String showdate  ;//场次日期
    private String showtime  ;//场次时间
    private String cinemaname  ;//影院名称
    private String filmname  ;//影片名称
    private String hallname  ;//影厅名称
    private String frontcover  ;//封面
    private String pdimenstional  ;//维数
    private String planguage  ;//语言
    private GroupBean groups ;
    private List<SectionBean> section;

    public GroupBean getGroups() {
        return groups;
    }

    public void setGroups(GroupBean groups) {
        this.groups = groups;
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
        return showtime;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
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

    public String getHallname() {
        return hallname;
    }

    public void setHallname(String hallname) {
        this.hallname = hallname;
    }

    public String getFrontcover() {
        return frontcover;
    }

    public void setFrontcover(String frontcover) {
        this.frontcover = frontcover;
    }

    public String getPdimenstional() {
        return pdimenstional;
    }

    public void setPdimenstional(String pdimenstional) {
        this.pdimenstional = pdimenstional;
    }

    public String getPlanguage() {
        return planguage;
    }

    public void setPlanguage(String planguage) {
        this.planguage = planguage;
    }

    public List<SectionBean> getSection() {
        return section;
    }

    public void setSection(List<SectionBean> section) {
        this.section = section;
    }
}
