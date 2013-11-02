package com.qinnuan.common.util;

import android.content.Context;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.Point;

public class Location implements AMapLocationListener {

	private final long mLocationUpdateMinTime = 10;
	private final float mLocationUpdateMinDistance = 10;
	private int timeScend = 0;
	private LocationCallBack locationCallBack;
	private Handler mHandler = new Handler();
	private TimeClick time = new TimeClick();
	private boolean locationSuccessFull = false;
	private LocationManagerProxy mAMapLocManager = null;
	private Context context;

	public interface LocationCallBack {
		public void locationSuccess(Point point);
		public void locaitonFailed();
	}

	public Location(Context context, LocationCallBack callBack) {
		this.locationCallBack = callBack;
		this.context = context;
		locationSuccessFull = false;
		timeScend = 0;
	}

	private boolean enableMyLocation() {
		LogUtil.e(getClass(), "enableMyLocation");
		boolean result = false;
		if (mAMapLocManager.isProviderEnabled(
                LocationProviderProxy.AMapNetwork)) {
			mAMapLocManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork,
                    mLocationUpdateMinTime,
					mLocationUpdateMinDistance, this);
			result = true;
		}
		mHandler.post(time);
		return result;
	}

	public void getLocation() {
		mAMapLocManager = LocationManagerProxy.getInstance(context);
		enableMyLocation();
	}

	int i = 0;

	class TimeClick implements Runnable {
		@Override
		public void run() {
			if (!locationSuccessFull) {
				if (timeScend < 15) {
					mHandler.postDelayed(time, 5000);
					timeScend += 5;
				} else {
					locationSuccessFull = true;
					timeScend = 0;
					locationCallBack.locaitonFailed();
				}
			}
		}
	}

	@Override
	public void onLocationChanged(android.location.Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			Point point = new Point();
			Const.point = point;
			point.setGeoLat(geoLat);
			point.setGeoLng(geoLng);
			LogUtil.e(LocationListener.class, point);
			if (locationCallBack != null) {
				locationCallBack.locationSuccess(point);
				mAMapLocManager.removeUpdates(this);
				locationSuccessFull = true;
				mHandler.removeCallbacks(time);
			}
		}
	}
}
