package com.qinnuan.engine.bean;

import com.qinnuan.engine.application.Const;
import com.qinnuan.common.josn.EAJson;

/**
 * Created by Administrator on 13-7-16.
 */
public class Officialinfo extends BaseBean {

    @EAJson
    private String official_url;
    @EAJson
    private String official_qr_url;

    public String getOfficial_url() {
        return official_url;
    }

    public void setOfficial_url(String official_url) {
        this.official_url = official_url;
    }

    public String getOfficial_qr_url() {
        return official_qr_url;
    }

    public void setOfficial_qr_url(String official_qr_url) {
        this.official_qr_url = official_qr_url;
    }

    public void cacheOfficialinfo() {
        Const.officialinfo.official_url = getOfficial_url();
        Const.officialinfo.official_qr_url = getOfficial_qr_url();
    }

}
