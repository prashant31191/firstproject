package com.qinnuan.engine.bean.film;

/**
 * Created by showu on 13-8-14.
 */
public class ActivityEticktBean {
    private String acteticcinemaid;//活动影院电子券ID
    private int tickettype;//0=2D 1=3D
    private float price;//价格
    private float oldprice;//价格
    private int maxbuynum;//最大购买数
    private int amount;//可购票数
    private String cinemaname;//影院名称
    private String notes;//购票须知
    private String validdate;//有效期

    public String getActeticcinemaid() {
        return acteticcinemaid;
    }

    public float getOldprice() {
        return oldprice;
    }

    public void setOldprice(float oldprice) {
        this.oldprice = oldprice;
    }

    public void setActeticcinemaid(String acteticcinemaid) {
        this.acteticcinemaid = acteticcinemaid;
    }

    public int getTickettype() {
        return tickettype;
    }

    public void setTickettype(int tickettype) {
        this.tickettype = tickettype;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getMaxbuynum() {
        return maxbuynum;
    }

    public void setMaxbuynum(int maxbuynum) {
        this.maxbuynum = maxbuynum;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCinemaname() {
        return cinemaname;
    }

    public void setCinemaname(String cinemaname) {
        this.cinemaname = cinemaname;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getValiddate() {
        return validdate;
    }

    public void setValiddate(String validdate) {
        this.validdate = validdate;
    }
}
