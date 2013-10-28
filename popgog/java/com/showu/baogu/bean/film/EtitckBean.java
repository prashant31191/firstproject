package com.showu.baogu.bean.film;

/**
 * Created by showu on 13-8-8.
 */
public class EtitckBean {
    private String cinemaeticketid;//g影院电子券ID
    private int tickettype;//0=2D 1=3D
    private float price;//g价格
    private int maxbuynum;//g最大购买数
    private String validdate;//g有效期(文字)
    private EcinemaBean parentCinema ;

    public EcinemaBean getParentCinema() {
        return parentCinema;
    }

    public void setParentCinema(EcinemaBean parentCinema) {
        this.parentCinema = parentCinema;
    }

    public String getCinemaeticketid() {
        return cinemaeticketid;
    }

    public void setCinemaeticketid(String cinemaeticketid) {
        this.cinemaeticketid = cinemaeticketid;
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

    public String getValiddate() {
        return validdate;
    }

    public void setValiddate(String validdate) {
        this.validdate = validdate;
    }
}
