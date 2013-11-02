package com.qinnuan.engine.bean.film;

import java.util.List;

/**
 * Created by showu on 13-8-12.
 */
public class SectionBean {
    private String sectionid  ;//区域ID
    private String sectionname  ;//区域名称
    private List<Seat> seat ;

    public String getSectionid() {
        return sectionid;
    }

    public void setSectionid(String sectionid) {
        this.sectionid = sectionid;
    }

    public String getSectionname() {
        return sectionname;
    }

    public void setSectionname(String sectionname) {
        this.sectionname = sectionname;
    }

    public List<Seat> getSeat() {
        return seat;
    }

    public void setSeat(List<Seat> seat) {
        this.seat = seat;
    }
}
