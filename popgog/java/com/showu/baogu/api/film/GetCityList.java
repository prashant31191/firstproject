package com.showu.baogu.api.film;

import com.showu.baogu.api.AbstractParam;
import com.showu.common.http.HttpMethod;
import com.showu.common.http.RequestType;

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
