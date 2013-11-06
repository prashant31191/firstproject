package com.qinnuan.engine.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.activity.CouponActivity;
import com.qinnuan.engine.activity.MainActivity;
import com.qinnuan.engine.api.GetMyuserinfo;
import com.qinnuan.engine.api.LoginUserPhone;
import com.qinnuan.engine.bean.QQAuth;
import com.qinnuan.engine.fragment.user.LoginHomeFragment;
import com.qinnuan.engine.fragment.user.UserHomeFragment;
import com.qinnuan.engine.listener.ILoginListener;
import com.qinnuan.engine.xmpp.ServiceManager;
import com.qinnuan.common.share.SinaListener;
import com.qinnuan.common.share.SinaWeiboShare;
import com.qinnuan.common.share.TencentListener;
import com.qinnuan.common.share.TencentShare;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.api.LoginUserSns;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.User;
import com.qinnuan.engine.listener.IUserHomeListener;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.sso.SsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class UserActivity extends BaseActivity {

    private UserActivity instance = this;
    private LoginHomeFragment loginF;
    private UserHomeFragment userF;
    public static String EXT_NEED_RETURN="need_return" ;
    private LoginUserPhone userPhoneParam = new LoginUserPhone();
    private GetMyuserinfo getUserParam = new GetMyuserinfo();
    private LoginUserSns loginUserSns=new LoginUserSns();
    public static final int SNS_TYPE_QQ=1;
    public static final int SNS_TYPE_WEIBO=0;
    public int currentSNSType=-1 ;
    public String currentSnsId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_bg);
        getTitleWidget().getLeftL().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LogUtil.v(getClass(), "========onResume=====" + Const.user);

//        if (Const.user != null) {
//            if(userF != null) {
//                httpGetUser();
//            } else {
//                gotoUserHomePage();
//                httpGetUser();
//            }
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        LogUtil.v(getClass(),"========onResume====="+Const.user);

        if(Const.user != null) {

            if(userF != null) {
                LogUtil.e(getClass(), "userF != null");
               // needDialog = false;
                if(loginF != null) loginF.back();
                httpGetUser();
            } else {
                LogUtil.e(getClass(), "userF == null");
//                gotoUserHomePage();
                httpGetUser();
            }
            MainActivity.mHost.getTabWidget().setVisibility(View.VISIBLE);
        } else if(Const.user == null) {
            if(userF!=null) userF.back();
            loginF = new LoginHomeFragment(loginL);
            replaceFragment(loginF, false);
            MainActivity.mHost.getTabWidget().setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.e(getClass(), "===========onActivityResult=================");
        super.onActivityResult(requestCode, resultCode, data);
        TencentShare.getInstance(this).getmTencent().onActivityResult(requestCode, resultCode, data);
        SsoHandler ssHandler = SinaWeiboShare.getInstance().getmSsoHandler();
        if (ssHandler != null) {
            ssHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    protected void requestFailed() {
        super.requestFailed();
       // GUIUtil.toast(instance, R.string.regist_phone_getAuthNo_fail);
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        try {
            if (url.contains(userPhoneParam.getApi())) {
                parseUserPhone(conent);
            } else if (url.contains(getUserParam.getApi())) {
                parseGetUser(conent);
            } else if(url.contains(loginUserSns.getApi())){
                pasreLoginSns(conent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseUserPhone(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        int status= jsonObject.getInt("status") ;
        if(status==Const.HTTP_OK){
            basePref.setUser(json);
            User user = parseUserInfo(json);
//            getSupportFragmentManager().popBackStack();
            userF=new UserHomeFragment(userL,user);
            replaceFragment(userF,false);
            startOpenfire();
            MainActivity.mHost.getTabWidget().setVisibility(View.VISIBLE);
        } else {
            GUIUtil.toast(instance, "登录失败,请检查你的账户名或者密码是否正确!");
        }
    }

    private void parseGetUser(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        int status= jsonObject.getInt("status") ;
        if(status==Const.HTTP_OK){
            basePref.setUser(json);
            User user = parseUserInfo(json);
//            userF.initData(user);
            userF=new UserHomeFragment(userL,user) ;
            replaceFragment(userF,false);
            startOpenfire();
        }
    }

    private void pasreLoginSns(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        int status= jsonObject.getInt("status") ;
        if(status==Const.HTTP_OK){
            basePref.setUser(content);
            User user = parseUserInfo(content);
           getSupportFragmentManager().popBackStack();
            userF=new UserHomeFragment(userL,user) ;
            replaceFragment(userF,false);
            startOpenfire();
            MainActivity.mHost.getTabWidget().setVisibility(View.VISIBLE);
        }else{
            Intent intent = new Intent(this,OtherRegistActivity.class) ;
            intent.putExtra(OtherRegistActivity.EXT_SNS_TYPE,currentSNSType) ;
            intent.putExtra(OtherRegistActivity.EXT_SNS_ID,currentSnsId) ;
            startActivity(intent);
        }
    }

    private TencentShare tshare;
    private SinaWeiboShare sShare;
    private ILoginListener loginL = new ILoginListener() {

        @Override
        public void login(String phone, String psw) {
            httpPhoneLogin(phone, psw);
        }

        @Override
        public void loginByQQ() {
//            tshare = TencentShare.getInstance(instance);
            currentSNSType=SNS_TYPE_QQ;
            TencentShare.getInstance(getApplicationContext()).login(instance, tListener);
        }

        @Override
        public void loginByWeibo() {
            currentSNSType=SNS_TYPE_WEIBO;
            sShare = SinaWeiboShare.getInstance();
            sShare.authorize(instance, sSinaListener);
        }

        @Override
        public void forgetPass() {
            startActivity(new Intent(instance, ForgetPswActivity.class));
        }

        @Override
        public void regist() {
            gotoRegistActivity();
        }

        private TencentListener tListener = new TencentListener() {
            @Override
            public void loginSuccess(JSONObject json) {
                LogUtil.e(getClass(), "tencen login json=>" + json.toString());
                QQAuth qqbean = JSONTool.jsonToBean(QQAuth.class, json);
                currentSnsId=qqbean.getOpenid();
                loginUserSns.setDeviceidentifyid(GUIUtil.getDeviceId(UserActivity.this));
                loginUserSns.setLatitude(Const.point.getGeoLat().toString());
                loginUserSns.setLogincityid(Const.city.getCityid());
                loginUserSns.setLongitude(Const.point.getGeoLng().toString());
                loginUserSns.setSnsuserid(qqbean.getOpenid());
                loginUserSns.setSnstype(""+SNS_TYPE_QQ);
                request(loginUserSns);
            }

            @Override
            public void shareSuccess(JSONObject json, Object object) {

            }

            @Override
            public void failed(int type) {
                loginF.setFailTips(1);
            }
        };

        private SinaListener sSinaListener = new SinaListener() {

            @Override
            public void requestSucessSina(String response) {
            }

            @Override
            public void failed() {
                loginF.setFailTips(2);
            }

            @Override
            public void authSuccess(Oauth2AccessToken accessToken, String uid,
                                    String userName) {
                LogUtil.e(getClass(), "username=>" + userName);
                currentSnsId=uid;
                loginUserSns.setDeviceidentifyid(GUIUtil.getDeviceId(UserActivity.this));
                loginUserSns.setLatitude(Const.point.getGeoLat().toString());
                loginUserSns.setLogincityid(Const.city.getCityid());
                loginUserSns.setLongitude(Const.point.getGeoLng().toString());
                loginUserSns.setSnsuserid(uid);
                loginUserSns.setSnstype(""+SNS_TYPE_WEIBO);
                request(loginUserSns);
            }
        };

    };

    private IUserHomeListener userL = new IUserHomeListener() {
        @Override
        public void gotoUserInfoDetail() {
            gotoUserInfo();
        }

        @Override
        public void gotoUserFilmTicket() {
            startActivity(new Intent(instance, UserOrderActivity.class));
        }

        @Override
        public void gotoCountCash() {
            startActivity(new Intent(instance, UserAccountActivity.class));
        }

        @Override
        public void gotoChit() {
            startActivity(new Intent(instance, CouponActivity.class));
        }

        @Override
        public void setHead(String imgUrl, ImageView head) {
            int size = getResources().getDimensionPixelSize(R.dimen.message_round_agle) ;
            LogUtil.e(getClass(), "mimgetf==>"+mImageFetcher+", head==>"+head+", imgurl==>"+imgUrl);
            if(head != null)
                mImageFetcher.loadImage(imgUrl, head,size);
        }
    };

    private String psw;

    private void httpPhoneLogin(String phone, String psw) {
        needDialog = true;
        this.psw = psw;
        userPhoneParam.setPhone(phone);
        userPhoneParam.setPassword(psw);
        userPhoneParam.setLatitude(Const.point.getGeoLat() + "");
        userPhoneParam.setLongitude(Const.point.getGeoLng() + "");
        userPhoneParam.setLogincityid(Const.city.getCityid());
        userPhoneParam.setDevicetype(Const.DEVICE_TYPE);
        userPhoneParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        request(userPhoneParam);
    }

    private void httpGetUser() {
        needDialog = false;
        getUserParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        getUserParam.setUserid(Const.user.getUserid());
        getUserParam.setDevicetype("1");
        request(getUserParam);
    }

    private void startOpenfire() {
        if (Const.user != null) {
            ServiceManager serviceManager = new ServiceManager(this);
            serviceManager.startService();
        }
    }

    private void gotoLoginPage() {
        loginF = new LoginHomeFragment(loginL);
        replaceFragment(loginF, true);
    }

//    private void gotoUserHomePage() {
//        userF = new UserHomeFragment(userL);
//        replaceFragment(userF, false);
//        LogUtil.d(getClass(), "initUserF-->" + userF);
//    }

    private void gotoRegistActivity() {
        startActivity(new Intent(instance, RegistPhoneActivity.class));
    }

    private void gotoUserInfo() {
        startActivity(new Intent(instance, UserInfoActivity.class));
    }

//    private void initUserF(User user) {
//        if(userF != null) {
//            MainActivity.mHost.getTabWidget().setVisibility(View.VISIBLE);
//            getSupportFragmentManager().popBackStack();
//            userF.initData(user);
//            LogUtil.d(getClass(),"initUserF-->"+userF);
//        } else {
//            MainActivity.mHost.getTabWidget().setVisibility(View.VISIBLE);
//            gotoUserHomePage();
//            userF.initData(user);
//        }
//    }

    public void initFragment() {
        LogUtil.e(getClass(), "inif===");
            loginF = new LoginHomeFragment(loginL);
            replaceFragment(loginF, false);

    }


}
