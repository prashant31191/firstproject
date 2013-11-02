package com.qinnuan.engine.api;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;

/**
 * Created by showu on 13-6-14.
 */
@RequestType(type = HttpMethod.GET)
public class GetGlobalparameters extends AbstractParam {
    //测试环境
    private final String api="http://183.136.221.2:1990/api/global/get_globalparameters.api";
    //正式环境
    //private final String api="http://json3.popgog.com/api/global/get_globalparameters.api";
    @Override
    public String getApi() {
        return api;
    }

}
