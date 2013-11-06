package com.qinnuan.engine.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.qinnuan.engine.bean.QQAuth;
import com.qinnuan.engine.bean.UrlBean;
import com.qinnuan.common.util.LogUtil;

/**
 * Created by JingHuiLiu on 13-7-9.
 */
public class MyPref {

    private static MyPref instance;
    private SharedPreferences mSharedPref = null;
    private final String APP_NAME = "COM.qinnuan.engine.BAOGU_POP";
    private final String DISPLAY_WIDTH = "DISPLAY_WIDTH";
    private final String DISPLAY_HEIGHT = "DISPLAY_HEIGHT";

    private final String JSON_API_URL = "JSON_API_URL";
    private final String UPLOAD_API_URL = "UPLOAD_API_URL";
    private final String PHONE_NO = "PHONE_NUMBER";
    private final String PASSWORD = "PASSWORD";
    private final String USER_ID = "USER_ID";
    private final String OPENFIRE_SERVIER= "OPENFIRE_SERVER";
    private final String OPENFIRE_PORT= "OPENFIRE_PORT";
    private final String OPENFIRE_PASSWORD= "OPENFIRE_PASSWORD";
    private final String QQ_AUTH_JSON = "QQ_AUTH_JSON";
    private final String USER_JSON = "USER_JSON";

    private final String SNS_QQ_ID = "SNS_QQ_ID";
    private final String SNS_SINA_ID = "SNS_SINA_ID";
    private static final String IS_SOUND_OPEN = "IS_SOUND_OPEN";
    private static final String IS_SHARE_OPEN = "IS_SHARE_OPEN";
    private static final String IS_AUTO_UPDATE = "IS_AUTO_UPADATE";
    private static final String URL_BEAN = "URL_BEAN";

    private MyPref(Context context) {
        mSharedPref = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
    }

    public static MyPref getInstance(Context context) {
        instance = new MyPref(context);
        return instance;
    }

    public void  saveUserId(String userId){
        Editor editor = mSharedPref.edit();
        editor.putString(USER_ID, userId);
        editor.commit();
    }
    public  String getUserId(){
        return mSharedPref.getString(USER_ID, null);
    }

    public void saveOpenfireServer(String address){
        Editor editor = mSharedPref.edit();
        editor.putString(OPENFIRE_SERVIER, address);
        editor.commit();
    }
    public  String getOpenfirSever(){
        return mSharedPref.getString(OPENFIRE_SERVIER, null);
    }
    public void saveOpenfirePass(String password){
        Editor editor = mSharedPref.edit();
        editor.putString(OPENFIRE_PASSWORD, password);
        editor.commit();

    }
    public  String getOpenfirPass(){
        return mSharedPref.getString(OPENFIRE_PASSWORD, null);
    }
    public void saveOpenfirePort(int port){
        Editor editor = mSharedPref.edit();
        editor.putInt(OPENFIRE_PORT, port);
        editor.commit();

    }
    public  int getOpenfirPort(){
        return mSharedPref.getInt(OPENFIRE_PORT, 5222);
    }
    public void setUrlBean(String urlBeanJson) {
        Editor editor = mSharedPref.edit();
        editor.putString(URL_BEAN, urlBeanJson);
        editor.commit();
    }

    public UrlBean getUrlBean() {
        String urlBeanJson = mSharedPref.getString(URL_BEAN, "");
        UrlBean bean = new Gson().fromJson(urlBeanJson, UrlBean.class);
        if(bean == null)
            bean = new UrlBean();
        return bean;
    }

    public void setDisplayWidth(int width) {
        Editor editor = mSharedPref.edit();
        editor.putInt(DISPLAY_WIDTH, width);
        editor.commit();
    }

    public int getDisplayWidth() {
        return mSharedPref.getInt(DISPLAY_WIDTH, 480);
    }

    public void setDisplayHeight(int height) {
        Editor editor = mSharedPref.edit();
        editor.putInt(DISPLAY_HEIGHT, height);
        editor.commit();
    }

    public int getDisplayHeight() {
        return mSharedPref.getInt(DISPLAY_HEIGHT, 800);
    }

    public void setPhoneNo(String phone) {
        Editor editor = mSharedPref.edit();
        editor.putString(PHONE_NO, phone);
        editor.commit();
    }

    public String getPhoneNo() {
        return mSharedPref.getString(PHONE_NO, "");
    }

    public void setPsw(String psw) {
        Editor editor = mSharedPref.edit();
        editor.putString(PASSWORD, psw);
        editor.commit();
    }

    public String getPsw() {
        return mSharedPref.getString(PASSWORD, "");
    }

    public void setUser(String userinfo) {
        Editor editor = mSharedPref.edit();
        editor.putString(USER_JSON, userinfo);
        editor.commit();
    }

    public String getUser() {
        String userJson = mSharedPref.getString(USER_JSON, null);
        return userJson;
    }

    public void setQQAuthBean(QQAuth bean) {
        Editor editor = mSharedPref.edit();
        String qqAuthJson = new Gson().toJson(bean);
        editor.putString(QQ_AUTH_JSON, qqAuthJson);
        editor.commit();
        LogUtil.e(getClass(), "qqAuthJson=>" + qqAuthJson);
    }

    public QQAuth getQQAuthBean() {
        String qqAuthJson = mSharedPref.getString(QQ_AUTH_JSON, "");
        QQAuth bean = new Gson().fromJson(qqAuthJson, QQAuth.class);
        if(bean == null)
            bean = new QQAuth();
        return bean;
    }

    /**type  --第三方类型 0=新浪微博 1=QQ */
    public void setSnsId(String snsid, String type) {
        Editor editor = mSharedPref.edit();
        if(type.equals("0")) {
            editor.putString(SNS_SINA_ID, snsid);
        } else if(type.equals("1")) {
            editor.putString(SNS_QQ_ID, snsid);
        }
        editor.commit();
    }

    /**type  --第三方类型 0=新浪微博 1=QQ */
    public String getSnsId(String type) {
        String id = "";
        if(type.equals("0")) {
            id = mSharedPref.getString(SNS_SINA_ID, "");
        } else if(type.equals("1")) {
            id = mSharedPref.getString(SNS_QQ_ID, "");
        }
        return id;
    }
    public void saveSoundOpen(boolean value) {
        Editor editor = mSharedPref.edit();// 获取编辑器
        editor.putBoolean(IS_SOUND_OPEN, value);
        editor.commit();
    }

    public boolean isSoundOpen() {
        return mSharedPref.getBoolean(IS_SOUND_OPEN, true);
    }

    public void saveShakeOpen(boolean value) {
        Editor editor = mSharedPref.edit();// 获取编辑器
        editor.putBoolean(IS_SHARE_OPEN, value);
        editor.commit();
    }

    public boolean isShakeOpen() {
        return mSharedPref.getBoolean(IS_SHARE_OPEN, true);
    }
    public void saveAutoUpdate(boolean value) {
        Editor editor = mSharedPref.edit();// 获取编辑器
        editor.putBoolean(IS_AUTO_UPDATE, value);
        editor.commit();
    }

    public boolean isAutoUpdate() {
        return mSharedPref.getBoolean(IS_AUTO_UPDATE, true);
    }

    public  void setFilm(String api,String json){
        Editor editor = mSharedPref.edit();// 获取编辑器
        editor.putString(api, json);
        editor.commit();
    }

    public String getFilm(String api){
        return mSharedPref.getString(api,null) ;
    }



}
