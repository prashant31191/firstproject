package com.showu.common.util;

import android.content.Context;
import android.text.SpannableStringBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JingHuiLiu on 13-7-6.
 */
public class TextUtil {

    public static Boolean isEmpty(String text) {
        Boolean isEmpty = true;
        if(text == null) {
            isEmpty = true;
        } else if(text != null) {
            if(!text.trim().isEmpty()) {
                isEmpty = false;
            }
        }
        return isEmpty;
    }

    public static SpannableStringBuilder getProcessText(String content,
                                                        Context mContext) {
        // SpannableStringBuilder builder =
        // ParseMsgUtil.faceConvetToHtml(utfResult,
        // mContext);
        if(!TextUtil.isEmpty(content)) {
            SpannableStringBuilder builder = ParseMsgUtil.convetToHtml(content,
                mContext);
            return builder;
        } else {
            return null;
        }
    }

   public static String getDecodStr(Context context, String content) {
       String result = "";
       SpannableStringBuilder ssb = getProcessText(content, context);
       if(ssb != null) {
            result = ssb.toString();
       }
       return result;
   }

    public static String getUrlEncode(String content) {
        try {
           return  URLEncoder.encode(content, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDistance(String dis) {
        if(isEmpty(dis)){
            return "" ;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        float distanceF = (float) (Double.parseDouble(dis) / 1000);
        return df.format(distanceF);
    }


    public static String getOffline(int minute) {
        int liveHour = minute / 60;
        String result = "3天前";
        if (minute < 60) {
            result = minute + "分钟前";
        } else {
            if (liveHour < 24) {
                result = liveHour + "小时前";
            } else if (liveHour >= 24 && liveHour < 48) {
                result = "1天前";
            } else if ((liveHour >= 48 && liveHour < 72)) {
                result = "2天前";
            } else {
                result = "3天前";
            }
        }
        return result;
    }

    public static boolean isMobilePhone(String mobiles){
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

}
