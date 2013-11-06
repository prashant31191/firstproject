package com.qinnuan.engine.activity.user;

import android.content.Intent;
import android.os.Bundle;

import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.api.BindingPhoneCreate;
import com.qinnuan.engine.api.BindingPhoneVerify;
import com.qinnuan.engine.api.UpdateUserForgetPassword;
import com.qinnuan.engine.fragment.user.ForgetVerifyCodeFragment;
import com.qinnuan.engine.fragment.user.ForgetVerifyPhoneFragment;
import com.qinnuan.engine.fragment.user.ForgetVerifyPswFragment;
import com.qinnuan.engine.listener.IForgetPswNewPswListener;
import com.qinnuan.engine.listener.IForgetPswPhoneListener;
import com.qinnuan.engine.listener.IForgetPswVerifycodeListener;
import com.qinnuan.engine.xmpp.ServiceManager;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.application.Const;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class BindPhoneActivity extends BaseActivity {

    private BindPhoneActivity instance = this;
    private ForgetVerifyPhoneFragment phoneF;
    private ForgetVerifyCodeFragment verifyF;
    private ForgetVerifyPswFragment pswF;

    private BindingPhoneCreate phoneParam = new BindingPhoneCreate();
    private BindingPhoneVerify verifycodeParam = new BindingPhoneVerify();
    private UpdateUserForgetPassword pswParam = new UpdateUserForgetPassword();

    private String uverifyid;
//    private String verifycode;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gotoPhonePage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //gotoPhonePage();
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        if(url.contains(phoneParam.getApi())) {
            parsePhoneCreate(conent);
        } else if(url.contains(verifycodeParam.getApi())) {
            parsePhoneVerify(conent);
        } else if(url.contains(pswParam.getApi())) {
            //parsePswSuccess(conent);
        }
    }

    @Override
    protected void requestFailed() {
        super.requestFailed();
    }

    private void parsePhoneVerify(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            if(Const.HTTP_OK == jobj.getInt("status")) {
                GUIUtil.toast(instance, "验证成功,你已经成功绑定手机");
                Const.user.setPhone(phone);
                Const.user.setIsphoneverify(1);
                Const.user.setIspassword(1);
                finish();
            } else {
                GUIUtil.toast(instance, "验证失败!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param json {"status":1,"verifycode":724546}
     */
    private void parsePhoneCreate(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            if(Const.HTTP_OK == jobj.getInt("status")) {
                GUIUtil.toast(instance, "验证码正在发送到你的手机上,请注意查收！");
//                verifycode = jobj.getString("verifycode");
                gotoVerifyPage();
            } else {
                GUIUtil.toast(instance, R.string.regist_phone_getAuthNo_fail);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void parsePswSuccess(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            if(Const.HTTP_OK == jobj.getInt("status")) {
                gotoUserHomePage();
                startOpenfire();
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startOpenfire() {
        if(!TextUtil.isEmpty(basePref.getUser())) {
            ServiceManager serviceManager = new ServiceManager(this);
            serviceManager.startService();
        }
    }


    private IForgetPswPhoneListener phoneL = new IForgetPswPhoneListener() {
        @Override
        public void sendPhone(String phone) {
            instance.phone = phone;
            phoneParam.setPhone(phone);
            phoneParam.setDevicetype("1");
            phoneParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
            if(Const.user != null) {
                phoneParam.setUserid(Const.user.getUserid());
            }
            request(phoneParam);
        }
    };

    private IForgetPswVerifycodeListener verifyL = new IForgetPswVerifycodeListener() {
        @Override
        public void sendVerifyCode(String code) {
            verifycodeParam.setVerifycode(code);
            if(Const.user != null) {
                verifycodeParam.setUserid(Const.user.getUserid());
            }
            verifycodeParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
            verifycodeParam.setDevicetype(Const.DEVICE_TYPE);
            verifycodeParam.setPhone(instance.phone);
            request(verifycodeParam);
        }
    };

    private IForgetPswNewPswListener pswL = new IForgetPswNewPswListener() {
        @Override
        public void updatePsw(String psw) {
            pswParam.setDevicetype(Const.DEVICE_TYPE);
            pswParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
            if(Const.user != null) {
                pswParam.setUserid(Const.user.getUserid());
            }
            pswParam.setNewpassword(psw);
            pswParam.setUverifyid(uverifyid);
            request(pswParam);
        }
    };

    private void gotoVerifyPage() {
        verifyF = new ForgetVerifyCodeFragment(verifyL);
        replaceFragment(verifyF, true);
    }

    private void gotoPhonePage() {
        phoneF = new ForgetVerifyPhoneFragment(phoneL);
        replaceFragment(phoneF, false);
    }

    private void gotoPswPage() {
        pswF = new ForgetVerifyPswFragment(pswL);
        replaceFragment(pswF, true);
    }

    private void gotoUserHomePage() {
        //startActivity(new Intent(instance, UserActivity.class));
        finish();
    }


}
