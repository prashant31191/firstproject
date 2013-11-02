package com.qinnuan.engine.activity.filmFan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.activity.film.EtickitActivity;
import com.qinnuan.engine.api.GetOtheruserinfo;
import com.qinnuan.engine.api.filmFan.DeleteFriend;
import com.qinnuan.engine.api.filmFan.FollowPublicaccount;
import com.qinnuan.engine.bean.Fan;
import com.qinnuan.engine.fragment.set.WebFragment;
import com.qinnuan.engine.xmpp.provider.ChatDBManager;
import com.qinnuan.engine.xmpp.provider.SessionManager;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.util.TextUtil;
import com.showu.baogu.R;
import com.qinnuan.engine.application.Const;

import org.json.JSONObject;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class PublicInfoActivity extends BaseActivity {

    private PublicInfoActivity instance = this;
    private ImageView head;
    private TextView name;
    private TextView sign;
    private TextView popcontent;
    private View history;
    private View ticket;
    private Button attent;
    private Button unattent;


    private Fan fan;

    private GetOtheruserinfo userInfoParam = new GetOtheruserinfo();
    private DeleteFriend delFriendParam = new DeleteFriend();
    private FollowPublicaccount addFriendParam = new FollowPublicaccount();

    public static final String PUBLIC_FAN_ID = "PUBLIC_FAN_ID";
    private String publicId;

    private WebFragment webF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_info);
        initView();
        publicId = getIntent().getStringExtra(PUBLIC_FAN_ID);
        httpFanInfo(publicId);
        initEvent();
    }

    private void initEvent() {
        ticket.setOnClickListener(onClickL);
        history.setOnClickListener(onClickL);
    }

    private void initView() {
        head = (ImageView) findViewById(R.id.activity_public_info_head);
        name = (TextView) findViewById(R.id.activity_public_info_nick);
        sign = (TextView) findViewById(R.id.activity_public_info_sign);
        popcontent = (TextView) findViewById(R.id.activity_public_info_popcontend);
        history = findViewById(R.id.activity_public_info_historylayout);
        ticket = findViewById(R.id.activity_public_info_ticketlayout);
        attent = (Button) findViewById(R.id.activity_public_info_attent);
        unattent = (Button) findViewById(R.id.activity_public_info_unattent);
        getTitleWidget().getLeftL().setOnClickListener(onClickL);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 推荐应用-暂时用的地址是ios版本的，发布新版本的时候需要改成android版本的网址
     */
    public void gotoHistory() {
        //WebFragment webFragment = new WebFragment(Const.app_plugin_url+ "/app_plugin/ios_others.html?devicetype=1","推荐应用") ;
        WebFragment webFragment = new WebFragment(Const.urlBean.getApp_plugin_url()
                + "/activity/getactivitylist?userid="+publicId, "历史活动") ;
        replaceFragment(webFragment,true);
//		Intent intent = new Intent(this, WebFragment.class);
//		Bundle extras = new Bundle();
//		extras.putString(Constants.URL,
//				Const.app_plugin_url + "/app_plugin/ios_others.html?devicetype=1");
//		extras.putString(Constants.PAGE_NAME, "推荐应用");
//		intent.putExtras(extras);
//		startActivity(intent);
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        try {
            if(url.contains(userInfoParam.getApi())) {
                parseFanInfo(conent);
            } else if(url.contains(delFriendParam.getApi())) {
                showAttent();
            } else if(url.contains(addFriendParam.getApi())) {
                showUnattent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void httpFanInfo(String userid) {
        userInfoParam.setUserid(Const.user.getUserid());
        userInfoParam.setLatitude(Const.point.getGeoLat()+"");
        userInfoParam.setLongitude(Const.point.getGeoLng()+"");
        userInfoParam.setDevicetype(Const.DEVICE_TYPE);
        userInfoParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        userInfoParam.setOtheruserid(userid);
        request(userInfoParam);
    }


    private void parseFanInfo(String json) throws Exception {
        JSONObject jobj = new JSONObject(json);
        if (Const.HTTP_OK == jobj.getInt("status")) {
            JSONObject jdata = jobj.getJSONObject("data");
            fan = JSONTool.jsonToBean(Fan.class, jdata.getJSONObject("users"));
            LogUtil.e(getClass(), "fans=>" + fan);
            fillData(fan);
        }
    }

    private void fillData(Fan fan) {
        mImageFetcher.loadImage(fan.getProfileimg(), head, GUIUtil.getDpi(instance, R.dimen.margin_8));
        name.setText(TextUtil.getProcessText(fan.getNickname(), instance));
        sign.setText(TextUtil.getProcessText(fan.getSignature(), instance));
        popcontent.setText(fan.getIconremark());

        //是否好友 0=否 1=是
        int isfriend = fan.getIsfriend();
        if(isfriend == 0) {
            showAttent();
        } else if(isfriend == 1) {
            showUnattent();
        }
    }

    private void showAttent() {
        MyFriendActivity.isDeleteAFriend = true;
        SessionManager.getInstance(this).deleteChatSession(fan.getUserid());
        ChatDBManager.getInstance(this).deletMessageByTargetId(fan.getUserid());
        attent.setVisibility(View.VISIBLE);
        unattent.setVisibility(View.GONE);
        attent.setOnClickListener(onClickL);
    }

    private void showUnattent() {
        unattent.setVisibility(View.VISIBLE);
        attent.setVisibility(View.GONE);
        unattent.setOnClickListener(onClickL);
    }



    private void httpDelFriend() {
        if(fan == null) return;
        delFriendParam.setUseridto(fan.getUserid());
        delFriendParam.setUserid(Const.user.getUserid());
        delFriendParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        delFriendParam.setDevicetype(Const.DEVICE_TYPE);
        request(delFriendParam);
    }

    private void httpAddFriend() {
        if(fan == null) return;
        addFriendParam.setUseridto(fan.getUserid());
        addFriendParam.setUserid(Const.user.getUserid());
        addFriendParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        addFriendParam.setDevicetype(Const.DEVICE_TYPE);
        request(addFriendParam);
    }

    private View.OnClickListener onClickL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.activity_public_info_ticketlayout:
                    if(publicId != null) {
                        startActivity(new Intent(instance, EtickitActivity.class).
                                putExtra(EtickitActivity.EXT_MECHENT_ID, publicId));
                    }

                    break;
                case R.id.activity_public_info_historylayout:
                    gotoHistory();
                    break;
                case R.id.activity_public_info_attent:
                    httpAddFriend();
                    break;
                case  R.id.activity_public_info_unattent:
                    httpDelFriend();
                    break;
                case R.id.left:
                    finish();
                    break;
                default:break;
            }
        }
    };
}
