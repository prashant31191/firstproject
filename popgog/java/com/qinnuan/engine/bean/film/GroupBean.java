package com.qinnuan.engine.bean.film;

/**
 * Created by showu on 13-8-13.
 */
public class GroupBean {
    private String groupname  ;//群名称
    private int isnotice  ;//是否通知 0=否 1=是

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public int getIsnotice() {
        return isnotice;
    }

    public void setIsnotice(int isnotice) {
        this.isnotice = isnotice;
    }
}
