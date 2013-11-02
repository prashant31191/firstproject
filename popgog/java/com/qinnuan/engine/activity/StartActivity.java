package com.qinnuan.engine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qinnuan.engine.api.BlacklistSelect;
import com.qinnuan.engine.api.GetCityBylatlng;
import com.qinnuan.engine.api.GetGlobalparameters;
import com.qinnuan.engine.api.user.AddUserApply;
import com.qinnuan.engine.api.user.GetUserApply;
import com.qinnuan.engine.bean.City;
import com.qinnuan.engine.bean.FanBlack;
import com.qinnuan.engine.bean.OfficialUser;
import com.qinnuan.engine.bean.Officialinfo;
import com.qinnuan.engine.xmpp.provider.FanBlacklistManager;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.Location;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.util.TextUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.Point;
import com.qinnuan.engine.bean.UrlBean;
import com.qinnuan.engine.xmpp.ServiceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class StartActivity extends BaseActivity {

    private GetGlobalparameters globalParam = new GetGlobalparameters();
    private GetCityBylatlng cityParam = new GetCityBylatlng();
    private BlacklistSelect blackParam = new BlacklistSelect();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        httpBlackList();
        requestGloable(globalParam);
        startOpenfire();
    }


    private AddUserApply addUserParam = new AddUserApply();
    private GetUserApply getUserParam = new GetUserApply();
    private void showBetaPage() {
        initBetaViews();
    }

    private void parseGetUserApply(String json) {
        try {
            JSONObject jObj = new JSONObject(json);
            int status = jObj.getInt("status");
            if(status == Const.HTTP_OK) {
                //申请通过
                new Location(this, locationCallBack).getLocation();
            } else if(status == 10){
                initBetaSendSuccess();
            } else {
                showBetaPage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseAddUserApply(String json) {
        try {
            JSONObject jObj = new JSONObject(json);
            int status = jObj.getInt("status");
            if(status == Const.HTTP_OK) {
                //发送申请成功
                initBetaSendSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private EditText betaPhone;
    private Button betaSend;
    private String phonetext;


    private View betaSendLayout;
    private View betaSendSuccessLayout;

    void findBetaViews() {
        setContentView(R.layout.activity_beta);
        betaSendLayout = findViewById(R.id.activity_beta_sendlayout);
        betaSendSuccessLayout = findViewById(R.id.activity_beta_successlayout);
        betaPhone = (EditText)findViewById(R.id.activity_beta_phone);
        betaSend = (Button)findViewById(R.id.activity_beta_send);
        findViewById(R.id.activity_beta_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
                finish();
            }
        });
    }

    private void initBetaViews() {
        findBetaViews();
        betaSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonetext = betaPhone.getText().toString();
                if(TextUtil.isEmpty(phonetext)) {
                    GUIUtil.toast(StartActivity.this, "手机号码不能为空");
                } else {
                    String p = phonetext.trim();
                    if(TextUtil.isMobilePhone(p)) {
                        httpAddUserApply();
                    } else {
                        GUIUtil.toast(StartActivity.this, "手机号码格式不正确！");
                    }
                }
            }
        });
    }

    private void initBetaSendSuccess() {
        findBetaViews();
        findViewById(R.id.activity_beta_logout).setVisibility(View.VISIBLE);
        betaSendLayout.setVisibility(View.GONE);
        betaSendSuccessLayout.setVisibility(View.VISIBLE);
    }

    private void httpAddUserApply() {
        addUserParam.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        addUserParam.setDevicetype(Const.DEVICE_TYPE);
        addUserParam.setPhone(phonetext);
        request(addUserParam);
    }

    private void httpGetUserApply() {
        getUserParam.setDevicetype(Const.DEVICE_TYPE);
        getUserParam.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        request(getUserParam);
    }

    private void httpBlackList() {
        if(Const.user == null) return;
        blackParam.setDevicetype(Const.DEVICE_TYPE);
        blackParam.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        blackParam.setUserid(Const.user.getUserid());
        request(blackParam);
    }

    private void parseFanBlacks(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            if (Const.HTTP_OK == jobj.getInt("status")) {
                JSONObject jdata = jobj.getJSONObject("data");
                List<FanBlack> fanblacks = JSONTool.getJsonToListBean(FanBlack.class,
                        jdata.getJSONArray("users"));
                if(!fanblacks.isEmpty()) {
                    FanBlacklistManager.getInstance(this).deleteFanBlacklist();
                    FanBlacklistManager.getInstance(this).insertFanBlacklist(fanblacks);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        if(url.contains(globalParam.getApi())) {
            parseGlabal(conent);
        } else if(url.contains(cityParam.getApi())) {
            parseCityBean(conent);
        } else if(url.contains(addUserParam.getApi())) {
            parseAddUserApply(conent);
        } else if(url.contains(getUserParam.getApi())) {
            parseGetUserApply(conent);
        } else if(url.contains(blackParam.getApi())) {
            parseFanBlacks(conent);
        }
    }

    @Override
    protected void requestFailed() {
        super.requestFailed();
        //startActivity(new Intent(this, MainActivity.class));
        //finish();
    }

    private void parseGlabal(String content) {
        LogUtil.e(getClass(), "content=>" + content);
        try {
            JSONObject jobj = new JSONObject(content);
            if(Const.HTTP_OK == jobj.getInt("status")) {
                JSONObject jdata = jobj.getJSONObject("data");
                JSONObject jUrlBean = jdata.getJSONObject("baseinfo");
                Const.urlBean = JSONTool.jsonToBean(UrlBean.class, jUrlBean);
                Const.urlBean.cacheUrlToMyPref(this, jUrlBean.toString());

                OfficialUser officialUser = JSONTool.jsonToBean(
                        OfficialUser.class, jdata.getJSONObject("offical_user"));
                Officialinfo officialinfo = JSONTool.jsonToBean(
                        Officialinfo.class, jdata.getJSONObject("officialinfo"));

                officialUser.cacheOfficialUser();
                officialinfo.cacheOfficialinfo();

                //Const.urlBean.setAndroidregswitch(0);
                if(Const.urlBean.getAndroidregswitch() == 1) {
                    httpGetUserApply();
                } else {
                    new Location(this, locationCallBack).getLocation();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Location.LocationCallBack locationCallBack =
            new Location.LocationCallBack() {
        @Override
        public void locationSuccess(Point point) {
            httpGetCity(point);
        }

        @Override
        public void locaitonFailed() {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }
    };

    private void httpGetCity(Point point) {
        cityParam.setLatitude(point.getGeoLat().toString());
        cityParam.setLongitude(point.getGeoLng().toString());
        request(cityParam);
    }

    private void parseCityBean(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            Const.city = JSONTool.jsonToBean(City.class, jobj.getJSONObject("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

            startActivity(new Intent(this, MainActivity.class));
            finish();


    }

    private void startOpenfire() {
        if(!TextUtil.isEmpty(basePref.getUser())) {
            ServiceManager serviceManager = new ServiceManager(this);
            serviceManager.startService();
        }
    }

}