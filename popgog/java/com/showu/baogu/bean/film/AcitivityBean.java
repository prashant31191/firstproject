package com.showu.baogu.bean.film;

import java.util.List;

/**
 * Created by showu on 13-8-14.
 */
public class AcitivityBean {
    private String officialactivityid  ;//活动ID
    private String activitytype  ;//活动类型0=无1=使用联系信息2=使用电子券3=使用订座票
    private String title  ;//标题
    private String description  ;//描述
    private String poster  ;//海报
    private String startdate  ;//开始时间
    private String enddate  ;//结束时间
    private int status  ;//状态0=未发布1=已发布，进行中2=已结束3=终止
    private String address  ;//地址
    private List<ActivityEticktBean> ticket ;

    public String getOfficialactivityid() {
        return officialactivityid;
    }

    public void setOfficialactivityid(String officialactivityid) {
        this.officialactivityid = officialactivityid;
    }

    public String getActivitytype() {
        return activitytype;
    }

    public void setActivitytype(String activitytype) {
        this.activitytype = activitytype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ActivityEticktBean> getTicket() {
        return ticket;
    }

    public void setTicket(List<ActivityEticktBean> ticket) {
        this.ticket = ticket;
    }
}
