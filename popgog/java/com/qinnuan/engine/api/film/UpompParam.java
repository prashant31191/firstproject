package com.qinnuan.engine.api.film;


import com.qinnuan.engine.api.AbstractParam;

public class UpompParam extends AbstractParam{
	private final String api="" ;
    private String uorderid  ;//订单ID
    private String userid  ;//需要解除的用户id
    private String deviceidentifyid  ;//设备ID
    private int devicetype=1  ;//设备类型 0=ios 1=android

    public void setUorderid(String uorderid) {
        this.uorderid = uorderid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setDeviceidentifyid(String deviceidentifyid) {
        this.deviceidentifyid = deviceidentifyid;
    }

    public void setDevicetype(int devicetype) {
        this.devicetype = devicetype;
    }

    @Override
	public String getApi() {
		// TODO Auto-generated method stub
		return api;
	}

}
