package com.qinnuan.engine.api;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;

/**
 * Created by showu on 13-8-26.
 */
@RequestType(type = HttpMethod.GET)
public class GetVersioninfo extends AbstractParam {
    private final String api="/api/global/get_versioninfo.api";
    @Override
    public String getApi() {
        return api;
    }

}
