package com.qinnuan.engine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.TextView;

import com.qinnuan.common.share.SinaListener;
import com.qinnuan.common.share.SinaWeiboShare;
import com.qinnuan.common.share.TencentListener;
import com.qinnuan.common.share.WXShare;
import com.qinnuan.common.util.FileUtil;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.widget.TitleWidget;
import com.showu.baogu.R;
import com.qinnuan.engine.application.Const;
import com.qinnuan.common.http.IDownLoadListener;
import com.qinnuan.common.http.ImageDowanLoad;
import com.qinnuan.common.share.TencentShare;
import com.qinnuan.common.util.FontCountLimit;
import com.weibo.sdk.android.Oauth2AccessToken;

import org.json.JSONObject;

import java.io.File;

public class FilmShareActivity extends BaseActivity implements TitleWidget.IOnClick {
    public static String EXT_SHARE_TYPE = "type";
    public static String EXT_SHARE_CONTENT = "content";
    public static int WEIBO = 0;
    public static int QZONE = 1;
    public static int WECHAT = 2;
    private String content;
    private EditText shareContentEdit;
    private TextView shareContentNumTextV;
    private TitleWidget titleWidget;
    private int MAX_NUM_TEXT = 120;
    private int type = -1;// 0:新浪微薄，1：QQ空间,2:微信
    private final int SHARE_FAILED = 1;
    private final int SHARE_SUCCESS = 2;
    private final int LOAD_IMAGE_END = 3;
    private final int LOGIN_TENCENT_SUCCESS = 4;
    private String filepath;
    private boolean hasSharing = false;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            dimissDialog();
            switch (msg.what) {
                case SHARE_FAILED:
                    GUIUtil.toast(FilmShareActivity.this, "分享失败");
                    hasSharing = false;
                    FilmShareActivity.this.finish();
                    break;
                case SHARE_SUCCESS:
                    GUIUtil.toast(FilmShareActivity.this, "分享成功");
                    hasSharing = false;
				FilmShareActivity.this.finish();
                    break;
                case LOAD_IMAGE_END:
                    shareSina(filepath);
                    break;
                case LOGIN_TENCENT_SUCCESS:
                    shareTencent();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_share);
        type = getIntent().getIntExtra(EXT_SHARE_TYPE, -1);
        content = getIntent().getStringExtra(EXT_SHARE_CONTENT);
    }

    @Override
    public void onResume() {
        super.onResume();
        initUI();
        initEvent();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initUI() {
        titleWidget = (TitleWidget) findViewById(R.id.titleBar);
        titleWidget.setTitleListener(this);
        if (type == WEIBO) {
            titleWidget.setCenterViewText("分享到新浪微博");
        } else if (type == QZONE) {
            titleWidget.setCenterViewText("分享到QQ空间");
        } else if (type == WECHAT) {
            titleWidget.setCenterViewText("分享到微信朋友圈");
        }
        shareContentEdit = (EditText) findViewById(R.id.activity_film_share_content);
        shareContentNumTextV = (TextView) findViewById(R.id.activity_film_share_content_num);
        shareContentNumTextV.setText(MAX_NUM_TEXT + "");

    }


    private void share() {
        hasSharing = true;
        if (type == 0) {// 新浪分享
            showDialog("正在发送");
            if (filepath == null) {
                loadData();
            } else {
                shareSina(filepath);
            }
        } else if (type == 1) {// 腾讯分享
            showDialog("正在发送");
            shareTencent();
        } else if (type == 2) {// 微信朋友圈
            if(WXShare.getInstance(this).isWXInstalled()){
            WXShare.getInstance(FilmShareActivity.this).share(
                    constractContent());
            }else {
                GUIUtil.toast(this,"请先安装微信客户端！");
            }
        }
    }

    private void shareSina(String filePath) {
        if (SinaWeiboShare.getInstance().isSessionValid()) {
            LogUtil.e(getClass(), "shareSina");
            SinaWeiboShare.getInstance().shareSinaWithImge(constractContent(),
                    filePath, sinaAuthListener);
//            try {
//                SinaWeiboShare.getInstance().shareSinaWithImgeURL(constractContent(),Const.officialinfo.getOfficial_qr_url(),sinaAuthListener);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
        } else {
            SinaWeiboShare.getInstance().authorize(FilmShareActivity.this,
                    sinaAuthListener);
        }
    }

    private void shareTencent() {

        if (TencentShare.getInstance(this).ready()) {
            TencentShare.getInstance(this).addShare(constractContent(),
                    tencentListener);
        } else {
            TencentShare.getInstance(this).login(this, tencentListener);
        }
    }

    private String constractContent() {
        StringBuilder sb = new StringBuilder(content);
        sb.append("#<");
        sb.append(shareContentEdit.getText().toString()).append(
                ">@爆谷电影  关注我吧！http://www.popgog.com");
        return sb.toString();
    }

    private void initEvent() {
        FontCountLimit fcl = new FontCountLimit(shareContentEdit,
                shareContentNumTextV);
        fcl.setOnEditTextListener(MAX_NUM_TEXT);
        // setOnEditTextListener();
    }

    private void loadData() {
        if (FileUtil.isExit(this, "office1_qr.png")) {
            File file = FileUtil.getNewFile(this, "office1_qr.png");
            filepath=file.getAbsolutePath();
            shareSina(filepath);
        } else {
            File file = FileUtil.getNewFile(this, "office1_qr.png");
            new ImageDowanLoad(loadListener, file, Const.officialinfo.getOfficial_qr_url()).start();
        }
    }

    private IDownLoadListener loadListener = new IDownLoadListener() {


        @Override
        public void startDownload(int length) {

        }

        @Override
        public void downloading(int len) {

        }

        @Override
        public void downLoadEnd(File file) {
            dimissDialog();
            filepath = file.getAbsolutePath();
            sendHandMessage(LOAD_IMAGE_END, null);
        }
    };

    private void sendHandMessage(int wath, Object o) {
        Message msg = new Message();
        msg.what = wath;
        msg.obj = o;
        handler.sendMessage(msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((!TencentShare.getInstance(this).getmTencent()
                .onActivityResult(requestCode, resultCode, data))) {
            if (SinaWeiboShare.getInstance().getmSsoHandler() != null) {
                SinaWeiboShare.getInstance().getmSsoHandler()
                        .authorizeCallBack(requestCode, resultCode, data);
            }
        }
    }

    private SinaListener sinaAuthListener = new SinaListener() {


        @Override
        public void failed() {
            // TODO Auto-generated method stub
            sendHandMessage(SHARE_FAILED, null);
        }

        @Override
        public void requestSucessSina(String response) {
            LogUtil.e(FilmShareActivity.class, response);
            sendHandMessage(SHARE_SUCCESS, response);
        }

        @Override
        public void authSuccess(Oauth2AccessToken accessToken, String uid,
                                String userName) {
            LogUtil.e(FilmShareActivity.class, "--authSuccess--");
            shareSina(filepath);
        }
    };

    private TencentListener tencentListener = new TencentListener() {

        @Override
        public void shareSuccess(JSONObject json, Object object) {
            sendHandMessage(SHARE_SUCCESS, null);
        }

        @Override
        public void loginSuccess(JSONObject json) {
            sendHandMessage(LOGIN_TENCENT_SUCCESS, null);
        }

        @Override
        public void failed(int type) {
            sendHandMessage(SHARE_FAILED, null);
        }
    };

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {
        share();
    }

    @Override
    public void centerClick() {

    }
}
