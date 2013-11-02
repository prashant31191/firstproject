package com.qinnuan.engine.bean;

import com.qinnuan.common.josn.EAJson;

/**
 * Created by Administrator on 13-7-17.
 */
public class Cash extends BaseBean {

    @EAJson private int cashamount;         //值
    @EAJson private int updatetype;         //变更类型 0=增1=减
    @EAJson private String description;     //描述
    @EAJson private String createdate;      //记录时间

    public Cash() { }

    public int getCashamount() {
        return cashamount;
    }

    public void setCashamount(int cashamount) {
        this.cashamount = cashamount;
    }

    public int getUpdatetype() {
        return updatetype;
    }

    public void setUpdatetype(int updatetype) {
        this.updatetype = updatetype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

}
