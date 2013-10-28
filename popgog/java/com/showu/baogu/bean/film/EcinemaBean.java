package com.showu.baogu.bean.film;

import java.util.List;

/**
 * Created by showu on 13-8-8.
 */
public class EcinemaBean {
    private String cinemaid;//影院ID
    private String cinemaname;//影院名称
    private String areaname;//区域名称
    private String address;//地址
    private String discount;//折扣
    private float price;//价格
    private List<EtitckBean> ticket;

    public String getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(String cinemaid) {
        this.cinemaid = cinemaid;
    }

    public String getCinemaname() {
        return cinemaname;
    }

    public void setCinemaname(String cinemaname) {
        this.cinemaname = cinemaname;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<EtitckBean> getTicket() {
        return ticket;
    }

    public void setTicket(List<EtitckBean> ticket) {
        this.ticket = ticket;
    }
}
