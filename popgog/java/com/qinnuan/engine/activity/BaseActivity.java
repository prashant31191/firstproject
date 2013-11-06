package com.qinnuan.engine.activity;

import android.app.LocalActivityManager;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import com.qinnuan.engine.api.AbstractParam;
import com.qinnuan.engine.application.BaoguApplication;
import com.qinnuan.engine.bean.SNSBean;
import com.qinnuan.engine.fragment.BaseFragment;
import com.qinnuan.engine.xmpp.ServiceManager;
import com.qinnuan.common.http.HttpClient;
import com.qinnuan.common.http.HttpMethod;
import com.qinnuan.common.http.HttpStringResult;
import com.qinnuan.common.http.IResponse;
import com.qinnuan.common.http.RequestType;
import com.qinnuan.common.http.image.util.ImageCache;
import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.common.share.SinaWeiboShare;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.widget.LoadingDialog;
import com.qinnuan.common.widget.TitleWidget;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.user.UserActivity;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.application.MyPref;
import com.qinnuan.engine.bean.User;
import com.qinnuan.common.widget.BottomPopWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

public abstract class BaseActivity extends FragmentActivity implements
        IResponse, HomeKeyEventBroadCastReceiver.OnHomeClick {

    public LoadingDialog progressDialog;
    protected boolean needDialog = true;

    private HomeKeyEventBroadCastReceiver homeKeyEventBroadCastReceiver;
    private HttpClient client;
    protected ImageFetcher mImageFetcher;
    protected FragmentManager baseFragmentManager;
    private static final String IMAGE_CACHE_DIR = "thumbs";
    protected MyPref basePref;
    protected PopupWindow buttomPop;
    protected Animation bottomIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseFragmentManager = getSupportFragmentManager();
        client = new HttpClient(this);
        basePref = MyPref.getInstance(this);
        initUserInfo();
        bottomIn = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
        homeKeyEventBroadCastReceiver = new HomeKeyEventBroadCastReceiver(this);
        registerReceiver(homeKeyEventBroadCastReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory
        int size = getResources().getDimensionPixelSize(R.dimen.image_size);

        mImageFetcher = new ImageFetcher(this, size);
        mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
        mImageFetcher.setLoadingImage(R.drawable.defaul_head);
    }

    protected void requestGloable(AbstractParam param) {
        client.get(param.getApi());
    }

    protected void requestFailed() {
        GUIUtil.toast(this, R.string.net_error);
    }


    protected void showButtomPop(BottomPopWindow.OnMenuSelect onMenuSelect, View dropAsView,
                                 String[] menus) {
        BottomPopWindow popLayout = new BottomPopWindow(this, onMenuSelect);
        popLayout.setMenu(menus);
        popLayout.startAnimation(bottomIn);
        buttomPop = new PopupWindow(popLayout,
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.FILL_PARENT);
        buttomPop.setFocusable(true);
        buttomPop.setBackgroundDrawable(new BitmapDrawable());
        buttomPop.setOutsideTouchable(true);
        buttomPop.update();
        buttomPop.showAsDropDown(dropAsView);
    }

    public void showDialog() {
        if(needDialog) {
            if (progressDialog == null) {
                progressDialog = new LoadingDialog(this, "正在加载更多数据");
                progressDialog.setOnCancelListener(onCancelListener);
                progressDialog.show();
            } else {
                progressDialog.show();
            }
        } else {

        }

    }

    public void showDialog(String message) {
        if(needDialog) {
            if (progressDialog == null) {
                progressDialog = new LoadingDialog(this, message);
                progressDialog.setOnCancelListener(onCancelListener);
                progressDialog.show();
            } else {
                progressDialog.show();
            }
        } else {

        }

    }

    private OnCancelListener onCancelListener = new OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            client.cancel();
        }
    };

    public void dimissDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onHomeShortClick() {
    }

    @Override
    public void onHomeLongClick() {

    }

    public void request(AbstractParam param) {
        showDialog();
        Class clazz = param.getClass();
        RequestType type = (RequestType) clazz.getAnnotation(RequestType.class);
        if (type != null) {
            if (type.type() == HttpMethod.GET) {
                //client.get(Const.json_api_url + param.getApi() + param.getString());
                client.get(Const.urlBean.getJson_api_url() + param.getApi() + param.getString());
            } else {
                //client.post(Const.json_api_url + param.getApi(), param.postString());
                client.post(Const.urlBean.getJson_api_url()+ param.getApi(), param.postString());
            }
        } else {
            LogUtil.e(getClass(), "You should set a RequestType for the param");
        }
    }

    protected void showLoading() {
        if (hasWindowFocus()) {
            if (progressDialog == null) {
                progressDialog = new LoadingDialog(this, getString(R.string.loading_more_data));
                progressDialog.show();
            } else {
                if(!progressDialog.isShowing()){
                    progressDialog.show();
                }
            }
        }
    }

    protected void uploadFile(String type, File file) {
        //String url = Const.upload_api_url
        String url = Const.urlBean.getUpload_api_url()
                + "/HttpHandlers/UploadFilesFromIphoneClient.ashx?filetype="
                + type;
        client.uploadFile(url, file);
    }

    protected void requestSuccess(String url, String conent) {

    }


    @Override
    public void response(HttpStringResult result) {
        if(needDialog){
        dimissDialog();
        }
        if (result.getStatus() == 200) {
            LogUtil.d(getClass(), result.getJson());
            try {
                JSONObject json = new JSONObject(result.getJson());
                int status = json.getInt("status") ;
                if(status==4){
                    GUIUtil.showDialog(this, "你的账号已经在别的设备登录,请重新登录",
                            "登录", "取消", ionAl);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            requestSuccess(result.getUrl(), result.getJson());
        } else {
            requestFailed();
        }
    }

    public GUIUtil.IOnAlertDListener ionAl = new GUIUtil.IOnAlertDListener() {

        @Override
        public void ok() {
            LogUtil.e(getClass(), "===ok===");
            cleanCache();
            MainActivity ma = BaoguApplication.activity;
            LocalActivityManager lm = ma.getLocalActivityManager();
            String aid = getResources().getString(R.string.tabs_tag_user);
            UserActivity ac = (UserActivity)lm.getActivity(aid);
          ac.initFragment();
            //ac.finish();
            //lm.startActivity(aid, new Intent(BaseActivity.this, UserActivity.class));
            MainActivity.mHost.setCurrentTab(3);
            MainActivity.mHost.getTabWidget().setVisibility(View.GONE);
        }

        @Override
        public void cancel() {
            LogUtil.e(getClass(), "===cancel===");
            cleanCache();
            ((BaoguApplication)getApplication()).exit();
            System.exit(0);
        }
    };

    private void cleanCache() {
        ServiceManager manager = new ServiceManager(BaseActivity.this) ;
        manager.stopService();
        SinaWeiboShare.getInstance().cleanToken(BaseActivity.this);
        basePref.setUser(null);
        Const.user=null ;
    }

    @Deprecated
    protected void replaceFragment(int id, BaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment);
        transaction.commitAllowingStateLoss();
    }

    protected void replaceFragment(BaseFragment newFragment, boolean isAddToBackStack) {
        LogUtil.e(getClass(), "replaceFragment before=====");
        String tag = getClass().getSimpleName();
        FragmentTransaction transaction = baseFragmentManager.beginTransaction();
        transaction.replace(android.R.id.content, newFragment, tag);
        if (isAddToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commitAllowingStateLoss();
       // transaction.commitAllowingStateLoss();
        LogUtil.e(getClass(), "replaceFragment after=====");
    }

    protected void addFragment(BaseFragment f, boolean isAddToBackStack) {
        String tag = getClass().getSimpleName();
        LogUtil.e(getClass(), "tag==>" + tag);
        if (baseFragmentManager.findFragmentByTag(tag) == null) {
            LogUtil.e(getClass(), "fragment==>" + f);
            final FragmentTransaction ft = baseFragmentManager.beginTransaction();
            ft.add(android.R.id.content, f, tag);
            if (isAddToBackStack) {
                ft.addToBackStack(tag);
            }
            ft.commit();
        }
    }

    @Override
    protected void onStart() {


        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        NotificationManager notificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(BaoguApplication.NOTIFYCATION_ID);
    }

    @Override
    public void onPause() {
        super.onPause();
//        mImageFetcher.setPauseWork(false);
//        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
        unregisterReceiver(homeKeyEventBroadCastReceiver);
        client.cancel();
    }

    private  void initUserInfo(){
        if(Const.user == null){
            Const.user = parseUserInfo(basePref.getUser()) ;
        }
        if(Const.urlBean == null) {
            Const.urlBean = basePref.getUrlBean();
        }


    }

    protected User parseUserInfo(String json) {
        LogUtil.e(getClass(),"parseUserInfo"+json);
        if(json == null) {
            return  null;
        }
        try {
            JSONObject jobj = new JSONObject(json);
            if(Const.HTTP_OK == jobj.getInt("status")) {
                JSONObject jdata = jobj.getJSONObject("data");
                JSONObject userJobj = jdata.getJSONObject("users");
                JSONArray jsonArray = jdata.getJSONArray("sns");
                User user = JSONTool.jsonToBean(User.class, userJobj);
                basePref.saveUserId(user.getUserid());
                basePref.saveOpenfirePass(user.getOpenfirepassword());
                basePref.saveOpenfireServer(user.getOpenfireserver());
                basePref.saveOpenfirePort(user.getOpenfireport());
                List<SNSBean> snsBeanList=JSONTool.getJsonToListBean(SNSBean.class,jsonArray) ;
                user.setSnsList(snsBeanList);
                Const.user=user;
                return user;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public  String parseImage(String content){
        String imgUrl= null ;
        try {
            JSONObject jobj = new JSONObject(content);
            if (Const.HTTP_OK == jobj.getInt("status")) {
                imgUrl = jobj.getString("httpurls");
            } else {
                GUIUtil.toast(this, R.string.regist_phone_upload_fail);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imgUrl;
    }

    protected TitleWidget getTitleWidget() {
        return  (TitleWidget) findViewById(R.id.titleBar);
    }

}
