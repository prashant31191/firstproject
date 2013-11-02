package com.qinnuan.engine.bean;

public class Point extends BaseBean {
	private Double geoLat;
	private Double geoLng;

	public Point() {
		// TODO Auto-generated constructor stub
	}
	public Point(double lat,double lng){
		this.geoLat=lat ;
		this.geoLng=lng ;
	}
	public Double getGeoLat() {
		return geoLat;
	}

	public void setGeoLat(Double geoLat) {
		this.geoLat = geoLat;
	}

	public Double getGeoLng() {
		return geoLng;
	}

	public void setGeoLng(Double geoLng) {
		this.geoLng = geoLng;
	}

	@Override
	public String toString() {
		return "Point lat=>["+geoLat+"], Point lng=>["+geoLng+"]";
	}

}
