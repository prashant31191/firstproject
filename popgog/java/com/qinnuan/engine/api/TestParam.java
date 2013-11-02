package com.qinnuan.engine.api;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;

/**
 * Created by showu on 13-7-17.
 */
@RequestType(type = HttpMethod.POST)
public class TestParam extends AbstractParam {
    @Override
    public String getApi() {
        return null;
    }
}
