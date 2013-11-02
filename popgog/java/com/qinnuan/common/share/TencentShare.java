package com.qinnuan.common.share;

import android.app.Activity;
import android.content.Context;

import com.tencent.tauth.Tencent;

public class TencentShare {
    private static final String APP_ID = "100326015";
    private static final String SCOPE = "get_user_info,get_simple_userinfo,get_user_profile,get_app_friends,"
            + "add_share,add_topic,list_album,upload_pic,add_album,set_user_face,get_vip_info,get_vip_rich_info,get_intimate_friends_weibo,match_nick_tips_weibo";

    private static TencentShare instance;
    private Tencent mTencent;

    public static TencentShare getInstance(Context context) {
        if (instance == null) {
            instance = new TencentShare(context);
        }
        return instance;
    }

    private TencentShare(Context context) {
        mTencent = Tencent.createInstance(APP_ID, context);
    }

    public Tencent getmTencent() {
        return mTencent;
    }

    public void login(Activity activity, TencentListener listener) {
//        if (!mTencent.isSessionValid()) {
//            mTencent.login(activity, SCOPE, listener);
//        }
        mTencent.login(activity, SCOPE, listener);
    }

    public void addShare(String content, TencentListener listener) {
        if (ready()) {
//			Bundle parmas = new Bundle();
//			parmas.putString("title", "爆谷电影");// 必须。feeds的标题，最长36个中文字，超出部分会被截断。
//			parmas.putString("url",
//					" http://www.popgog.com" + "#" + System.currentTimeMillis());// 必须。分享所在网页资源的链接，点击后跳转至第三方网页，
//			parmas.putString("comment", content);// 用户评论内容，也叫发表分享时的分享理由。禁止使用系统生产的语句进行代替。最长40个中文字，超出部分会被截断。
//			parmas.putString("summary", "爆谷电影");// 所分享的网页资源的摘要内容，或者是网页的概要描述。
//			parmas.putString("images", Const.official_qr_url);// 所分享的网页资源的代表性图片链接"，请以http://开头，长度限制255字符。多张图片以竖线（|）分隔，目前只有第一张图片有效，图片规格100*100为佳。
//			parmas.putString("type", "4");// 分享内容的类型。
//			mTencent.requestAsync(Constants.GRAPH_ADD_SHARE, parmas,
//					Constants.HTTP_POST, listener, null);
        }
    }

    public boolean ready() {
        return mTencent.isSessionValid() && mTencent.getOpenId() != null;
    }

}