package com.qinnuan.engine.api;

import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.RequestType;

/**
 * Created by showu on 13-6-14.
 */
@RequestType(type = HttpMethod.POST)
public class RegisterPhoneVerify extends AbstractParam {

    private final String api="/api/user/register_phone_verify.api";
    @Override
    public String getApi() {
        return api;
    }

    private String phone;
    private String verifycode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerifycode() {
        return verifycode;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
    }

}
