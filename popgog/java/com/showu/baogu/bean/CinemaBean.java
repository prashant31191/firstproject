package com.showu.baogu.bean;

import com.showu.common.josn.EAJson;

/**
 * Created by showu on 13-7-25.
 */
public class CinemaBean {
    @EAJson private String cinemaid;  //影院ID
    @EAJson private String cinemaname;  //影片名称
    @EAJson private String longitude;  //经度
    @EAJson private String latitude;  //纬度
    @EAJson private String areaname;  //地区名称
    @EAJson private String address;  //地址
    @EAJson private int distance;  //距离(米)
    @EAJson private String cinemaphone;  //地址

    public String getCinemaphone() {
        return cinemaphone;
    }

    public void setCinemaphone(String cinemaphone) {
        this.cinemaphone = cinemaphone;
    }

    public int getIsoften() {
        return isoften;
    }

    public void setIsoften(int isoften) {
        this.isoften = isoften;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCinemaname() {
        return cinemaname;
    }

    public void setCinemaname(String cinemaname) {
        this.cinemaname = cinemaname;
    }

    public String getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(String cinemaid) {
        this.cinemaid = cinemaid;
    }
    @EAJson
    private  int isoften;  //是否常去 0=否 1=是

}
