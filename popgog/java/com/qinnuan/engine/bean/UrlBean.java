package com.qinnuan.engine.bean;

import android.content.Context;

import com.qinnuan.common.josn.EAJson;
import com.qinnuan.engine.application.MyPref;

/**
 * Created by Administrator on 13-7-16.
 */
public class UrlBean extends BaseBean {

    @EAJson
    private String json_api_url;
    @EAJson private String upload_api_url;
    @EAJson private String geo_offset_api_url;
    @EAJson private String app_plugin_url;
    @EAJson private String connection_manager_server;
    @EAJson private String alipay_app_url;
    @EAJson private String alipay_wap_url;
    @EAJson private String cupay_url;
    @EAJson private String iphoneversion;
    @EAJson private String androidversion;
    @EAJson private int androidregswitch; //注册申请开关 0=关 1=开

    public String getJson_api_url() {
        return json_api_url;
    }

    public void setJson_api_url(String json_api_url) {
        this.json_api_url = json_api_url;
    }

    public String getUpload_api_url() {
        return upload_api_url;
    }

    public void setUpload_api_url(String upload_api_url) {
        this.upload_api_url = upload_api_url;
    }

    public String getGeo_offset_api_url() {
        return geo_offset_api_url;
    }

    public void setGeo_offset_api_url(String geo_offset_api_url) {
        this.geo_offset_api_url = geo_offset_api_url;
    }

    public String getApp_plugin_url() {
        return app_plugin_url;
    }

    public void setApp_plugin_url(String app_plugin_url) {
        this.app_plugin_url = app_plugin_url;
    }

    public String getConnection_manager_server() {
        return connection_manager_server;
    }

    public void setConnection_manager_server(String connection_manager_server) {
        this.connection_manager_server = connection_manager_server;
    }

    public String getAlipay_app_url() {
        return alipay_app_url;
    }

    public void setAlipay_app_url(String alipay_app_url) {
        this.alipay_app_url = alipay_app_url;
    }

    public String getAlipay_wap_url() {
        return alipay_wap_url;
    }

    public void setAlipay_wap_url(String alipay_wap_url) {
        this.alipay_wap_url = alipay_wap_url;
    }

    public String getCupay_url() {
        return cupay_url;
    }

    public void setCupay_url(String cupay_url) {
        this.cupay_url = cupay_url;
    }

    public String getIphoneversion() {
        return iphoneversion;
    }

    public void setIphoneversion(String iphoneversion) {
        this.iphoneversion = iphoneversion;
    }

    public String getAndroidversion() {
        return androidversion;
    }

    public void setAndroidversion(String androidversion) {
        this.androidversion = androidversion;
    }

    public int getAndroidregswitch() {
        return androidregswitch;
    }

    public void setAndroidregswitch(int androidregswitch) {
        this.androidregswitch = androidregswitch;
    }

    //    public void cacheUrl() {
//        Const.json_api_url = getJson_api_url();
//        Const.upload_api_url = getUpload_api_url();
//        Const.geo_offset_api_url = getGeo_offset_api_url();
//        Const.app_plugin_url = getApp_plugin_url();
//        Const.connection_manager_server = getConnection_manager_server();
//        Const.alipay_app_url = getAlipay_app_url();
//        Const.alipay_wap_url = getAlipay_wap_url();
//        Const.cupay_url = getCupay_url();
//        Const.iphoneversion = getIphoneversion();
//        Const.androidversion = getAndroidversion();
//    }

    public void cacheUrlToMyPref(Context c, String urlBeanJson) {
        MyPref appPref = MyPref.getInstance(c);
        appPref.setUrlBean(urlBeanJson);
    }
}
