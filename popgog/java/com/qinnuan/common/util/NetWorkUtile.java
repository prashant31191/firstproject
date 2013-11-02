package com.qinnuan.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

public class NetWorkUtile {

	// 无网络
	public final static int NONE = 0;
	// Wi-Fi
	public final static int WIFI = 1;
	// 3G,GPRS
	public final static int MOBILE = 2;

	/**
	 * 获取当前网络状态
	 */
	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 手机网络判断
		State state = connManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return MOBILE;
		}
		// Wifi网络判断
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return WIFI;
		}
		return NONE;
	}

    public static boolean isHasNetwork(Context c) {
        if (getNetworkState(c) == NONE) {
            return false;
        } else {
            return true;
        }
    }

}
