package com.qinnuan.engine.api.film;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.engine.api.AbstractParam;

/**
 * Created by showu on 13-8-19.
 */
@RequestType(type = HttpMethod.GET)
public class GetCityList extends AbstractParam {
    private final String api="/api/global/get_citylist.api";

    @Override
    public String getApi() {
        return api;
    }
}
