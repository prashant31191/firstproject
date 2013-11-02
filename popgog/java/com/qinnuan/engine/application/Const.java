package com.qinnuan.engine.application;

import com.qinnuan.engine.bean.City;
import com.qinnuan.engine.bean.OfficialUser;
import com.qinnuan.engine.bean.Officialinfo;
import com.qinnuan.engine.bean.Point;
import com.qinnuan.engine.bean.UrlBean;
import com.qinnuan.engine.bean.User;

/**
 * Created by Administrator on 13-7-9.
 */
public class Const {

//    public static String localId ="L_909CCA20B6D448C99460776774FC9C57";      //本地id
    public static User user ;

    public final static String IMAGE_URI = "iamge_uri";
    public final static String BITMAP = "bitmap";
    public final static String CROP_IMAGE_URI = "crop_image_uri";

    public static final String APP_ID = "100326015";
    public static final String SCOPE = "all";
    public static final String JUSTAUTH = "justauth";

    public final static Integer HTTP_OK = 1;
    public static final String DEVICE_TYPE = "1"; //设备类型 0=ios 1=android

    /** 全局变量 */
    //public static String json_api_url = "http://183.136.221.2:1990";
    //     public static String json_api_url;
    //public static String upload_api_url;
    //public static String geo_offset_api_url;
    //public static String app_plugin_url;
    //public static String connection_manager_server;
    //public static String alipay_app_url;
    //public static String alipay_wap_url;
    //public static String cupay_url;
    //public static String iphoneversion;
    //public static String androidversion;

    public static OfficialUser officalUser = new OfficialUser();
    public static Officialinfo officialinfo = new Officialinfo();

    public static Point point = new Point(23.092611, 113.326286);
    public static City city = new City("L_909CCA20B6D448C99460776774FC9C57", "广州");
    public static UrlBean urlBean;

}
