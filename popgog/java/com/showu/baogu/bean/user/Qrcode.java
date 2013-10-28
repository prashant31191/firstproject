package com.showu.baogu.bean.user;

import com.showu.baogu.bean.BaseBean;
import com.showu.common.josn.EAJson;

/**
 * Created by showu on 13-7-25.
 */
public class Qrcode extends BaseBean{

    @EAJson private String pconfirmationid;    //取票号
    @EAJson private String qrcodeurl;//    二维码
    @EAJson private String expireddate;    //过期时间

    public String getPconfirmationid() {
        return pconfirmationid;
    }

    public void setPconfirmationid(String pconfirmationid) {
        this.pconfirmationid = pconfirmationid;
    }

    public String getQrcodeurl() {
        return qrcodeurl;
    }

    public void setQrcodeurl(String qrcodeurl) {
        this.qrcodeurl = qrcodeurl;
    }

    public String getExpireddate() {
        return expireddate;
    }

    public void setExpireddate(String expireddate) {
        this.expireddate = expireddate;
    }
}
