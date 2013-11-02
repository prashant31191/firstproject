package com.qinnuan.engine.bean;

import com.qinnuan.engine.application.Const;
import com.qinnuan.common.josn.EAJson;

/**
 * Created by Administrator on 13-7-16.
 */
public class OfficialUser extends BaseBean {

    @EAJson
    private String userid;
    @EAJson
    private String lat;
    @EAJson
    private String lng;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void cacheOfficialUser() {
        Const.officalUser.userid = getUserid();
        Const.officalUser.lat = getLat();
        Const.officalUser.lng = getLng();
    }

}
