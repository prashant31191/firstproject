package com.qinnuan.engine.listener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import com.qinnuan.common.util.LogUtil;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public abstract class TencentListener implements IUiListener, IRequestListener {

	@Override
	public void onComplete(JSONObject arg0, Object arg1) {
		// TODO Auto-generated method stub
		shareSuccess(arg0, arg1);
	}

	@Override
	public void onConnectTimeoutException(ConnectTimeoutException arg0,
			Object arg1) {
		// TODO Auto-generated method stub
		arg0.printStackTrace() ;
		failed(1);
	}

	@Override
	public void onHttpStatusException(HttpStatusException arg0, Object arg1) {
		// TODO Auto-generated method stub
		arg0.printStackTrace() ;
		failed(1);
	}

	@Override
	public void onIOException(IOException arg0, Object arg1) {
		// TODO Auto-generated method stub
		arg0.printStackTrace() ;
		failed(1);
	}

	@Override
	public void onJSONException(JSONException arg0, Object arg1) {
		// TODO Auto-generated method stub
		failed(1);
	}

	@Override
	public void onMalformedURLException(MalformedURLException arg0, Object arg1) {
		// TODO Auto-generated method stub
		arg0.printStackTrace() ;
		failed(1);
	}

	@Override
	public void onNetworkUnavailableException(NetworkUnavailableException arg0,
			Object arg1) {
		// TODO Auto-generated method stub
		arg0.printStackTrace() ;
		failed(1);
	}

	@Override
	public void onSocketTimeoutException(SocketTimeoutException arg0,
			Object arg1) {
		// TODO failed(1) ;Auto-generated method stub
		arg0.printStackTrace() ;
		failed(1);
	}

	@Override
	public void onUnknowException(Exception arg0, Object arg1) {
		// TODO Auto-generated method stub
		arg0.printStackTrace() ;
		failed(1);
	}

	@Override
	public void onCancel() {
		failed(0);
	}

	@Override
	public void onComplete(JSONObject arg0) {
		// TODO Auto-generated method stub
		loginSuccess(arg0);
	}

	@Override
	public void onError(UiError arg0) {
		LogUtil.e(getClass(), arg0.errorMessage) ;
		failed(0);
	}

	public abstract void loginSuccess(JSONObject json);

	public abstract void shareSuccess(JSONObject json, Object object);

	public abstract void failed(int type);
}
