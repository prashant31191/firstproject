package com.qinnuan.common.share;

import java.io.IOException;

import android.os.Bundle;

import com.qinnuan.common.util.LogUtil;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

public abstract class SinaListener implements WeiboAuthListener,
		RequestListener {

	public SinaListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCancel() {
		failed();
	}

	@Override
	public void onComplete(Bundle values) {
        LogUtil.e(getClass(), "values=>" + values.toString());
		String token = values.getString("access_token");
		String expires_in = values.getString("expires_in");
		String uid = values.getString("uid");
		String userName = values.getString("userName");
        LogUtil.e(getClass(), "userName sina=>"+userName);
		for (String key : values.keySet()) {
			LogUtil.i(getClass(), key + "||" + values.getString(key));
		}
		Oauth2AccessToken accessToken = new Oauth2AccessToken(token, expires_in);
		SinaWeiboShare.getInstance().setAccessToken(accessToken);
		authSuccess(accessToken,uid, userName);
	}

	@Override
	public void onError(WeiboDialogError arg0) {
		// TODO Auto-generated method stub
		arg0.printStackTrace() ;
		failed();
	}

	@Override
	public void onWeiboException(WeiboException arg0) {
		// TODO Auto-generated method stub
		arg0.printStackTrace() ;
		failed();
	}

	public abstract void authSuccess(Oauth2AccessToken accessToken,String uid, String userName);

	public abstract void failed();

	public abstract void requestSucessSina(String response);

	@Override
	public void onComplete(String response) {

		requestSucessSina(response);
	}

	@Override
	public void onError(WeiboException arg0) {
		arg0.printStackTrace() ;
		failed();
	}

	@Override
	public void onIOException(IOException arg0) {
		arg0.printStackTrace() ;
		failed();
	}

}
