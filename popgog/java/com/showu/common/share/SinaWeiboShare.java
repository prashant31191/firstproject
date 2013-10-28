package com.showu.common.share;

import android.app.Activity;
import android.content.Context;

import com.showu.baogu.application.Const;
import com.showu.common.util.LogUtil;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.sso.SsoHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

public class SinaWeiboShare {
	private Weibo mWeibo;
	// private static final String CONSUMER_KEY = "4121627817";//
	// 替换为开发者的appkey，例如"1646212860";
	private static final String CONSUMER_KEY = "208280952";// 替换为开发者的appkey，例如"1646212860";
	private static final String REDIRECT_URL = "http://weibo.com/popgog.Authorize.com";
	public Oauth2AccessToken accessToken;
	private SsoHandler mSsoHandler;
	private static SinaWeiboShare instance;

	public static SinaWeiboShare getInstance() {
		if (instance == null) {
			instance = new SinaWeiboShare();
		}
		return instance;
	}

	private SinaWeiboShare() {
		mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);

	}

	public Oauth2AccessToken getAccessToken() {
		return accessToken;
	}

	public SsoHandler getmSsoHandler() {
		return mSsoHandler;
	}

	public void setmSsoHandler(SsoHandler mSsoHandler) {
		this.mSsoHandler = mSsoHandler;
	}

	public void setAccessToken(Oauth2AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	public void authorize(Activity activity, SinaListener sinaAuthListener) {
		mSsoHandler = new SsoHandler(activity, mWeibo);
		mSsoHandler.authorize(sinaAuthListener);
	}

	public boolean isSessionValid() {
		if (accessToken == null) {
			return false;
		} else {
			return accessToken.isSessionValid();
		}
	}

	public void shareSina(String content, SinaListener sinaAuthListener) {
		if (isSessionValid()) {
			StatusesAPI statusApi = new StatusesAPI(accessToken);
			statusApi.update(content, Const.point.getGeoLat() + "",
					Const.point.getGeoLng() + "", sinaAuthListener);
		} else {
			sinaAuthListener.failed();
		}

	}

    public  void createFriend(long uid,String screenName ,SinaListener sinaListener){
        FriendshipsAPI friendshipsAPI=new FriendshipsAPI(accessToken);
        friendshipsAPI.create(uid,screenName,sinaListener);
    }

	public void shareSinaWithImge(String content, String file,
			SinaListener sinaAuthListener) {
        LogUtil.d(getClass(),file);
		if (isSessionValid()) {
			StatusesAPI statusApi = new StatusesAPI(accessToken);
			statusApi.upload(content, file, Const.point.getGeoLat() + "",
					Const.point.getGeoLng() + "", sinaAuthListener);
		} else {
			sinaAuthListener.failed();
		}
	}

    public void shareSinaWithImgeURL(String content, String url,
                                  SinaListener sinaAuthListener) throws UnsupportedEncodingException {
        LogUtil.d(getClass(),url);
        if (isSessionValid()) {
            StatusesAPI statusApi = new StatusesAPI(accessToken);
            statusApi.uploadUrlText(URLEncoder.encode(content,"utf-8"), url, Const.point.getGeoLat() + "",
                    Const.point.getGeoLng() + "", sinaAuthListener);
        } else {
            sinaAuthListener.failed();
        }
    }


	public void cleanToken(Context context) {
		if (accessToken != null) {
            this.accessToken=null ;
		}
	}

    public void getUserInfo(long uid, SinaListener listener){
        UsersAPI usersAPI = new UsersAPI(accessToken) ;
        usersAPI.show(uid,listener);
    }

}
