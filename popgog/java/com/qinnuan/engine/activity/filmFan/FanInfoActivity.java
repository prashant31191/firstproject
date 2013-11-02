package com.qinnuan.engine.activity.filmFan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.showu.baogu.R;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.activity.message.ChatActivity;
import com.qinnuan.engine.activity.user.DynamicFanActivity;
import com.qinnuan.engine.api.AddReportUser;
import com.qinnuan.engine.api.GetOtheruserinfo;
import com.qinnuan.engine.api.PullBlack;
import com.qinnuan.engine.api.user.AddFriend;
import com.qinnuan.engine.application.BaoguApplication;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.Dynamic;
import com.qinnuan.engine.bean.Fan;
import com.qinnuan.engine.bean.OtherUserInfo;
import com.qinnuan.engine.bean.Sns;
import com.qinnuan.engine.bean.User;
import com.qinnuan.engine.fragment.filmFan.FanInfoFragment;
import com.qinnuan.engine.fragment.filmFan.FanSayhiFragment;
import com.qinnuan.engine.listener.IFanInfoListener;
import com.qinnuan.engine.listener.IFanSayhiListener;
import com.qinnuan.engine.listener.ILoadImgListener;
import com.qinnuan.engine.xmpp.IReciveMessage;
import com.qinnuan.engine.xmpp.MessageSendListener;
import com.qinnuan.engine.xmpp.json.TextMessageJson;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.message.MessageUtil;
import com.qinnuan.engine.xmpp.message.MessageType;
import com.qinnuan.engine.xmpp.message.SessionType;
import com.qinnuan.engine.xmpp.provider.SessionManager;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.common.widget.BottomPopWindow;

import org.jivesoftware.smack.packet.Packet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class FanInfoActivity extends BaseActivity implements MessageSendListener {

    private FanInfoActivity instance = this;
    public static final String FAN_ID = "FAN_ID";
    public static final String CHECK_TYPE = "Check_Type";
    public static Integer check_value;

    private GetOtheruserinfo userInfoParam = new GetOtheruserinfo();
    private AddReportUser reportParam = new AddReportUser();
    private AddFriend addFriParam = new AddFriend();
    private PullBlack pullParam = new PullBlack();

    private FanInfoFragment fanInfoF;
    private FanSayhiFragment sayihF;
    private Fan fan;
    private List<Dynamic> dynamics;

    private final int MESSAGE_COME_ADD = 11;
    private final int MESSAGE_COME_DELETE = 44;
    private final int SAY_HELLO_SUCCESS = 22;
    private final int SAY_HELLO_FAIL = 33;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_bg);
        sessionManager = SessionManager.getInstance(this);
        if (BaoguApplication.xmppManager != null) {
            BaoguApplication.xmppManager.getReciveMessageList().add(irm);
        }

        check_value = getIntent().getIntExtra(CHECK_TYPE, -1);
        httpFanInfo(getIntent().getStringExtra(FAN_ID));
    }

    private IReciveMessage irm = new IReciveMessage() {
        @Override
        public boolean reciveMessage(BaseMessage msg) {
            LogUtil.e(getClass(), "msg==>" + msg.toXML());
            if (msg.getMessageType() == MessageType.ADDFRIEND) {
                if(msg.getTargetId().equals(fan.getUserid())) {
                    sendMessage(MESSAGE_COME_ADD, msg);
                }
            } else if (msg.getMessageType() == MessageType.DELETEFRIEND) {
                if(msg.getTargetId().equals(fan.getUserid())) {
                    sendMessage(MESSAGE_COME_DELETE, msg);
                }
            }
            return false;
        }
    };

    private void sendMessage(int what, Object o) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = o;
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_COME_ADD:
                    fanInfoF.showLayout(FanInfoFragment.LAYOUT_TYPE.SENDMSG);
                    break;
                case MESSAGE_COME_DELETE:
                    fanInfoF.showLayout(FanInfoFragment.LAYOUT_TYPE.SAYHI);
                    break;
                case SAY_HELLO_SUCCESS:
                    GUIUtil.toast(instance, "招呼发送成功！");
                    sayihF.back();
                    break;
                case SAY_HELLO_FAIL:
                    GUIUtil.toast(instance, "招呼发送失败,请重新发送！");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void requestFailed() {
        super.requestFailed();
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        try {
            if (url.contains(userInfoParam.getApi())) {
                parseFanInfo(conent);
            } else if (url.contains(reportParam.getApi())) {
                GUIUtil.toast(instance, "感谢你的举报,包谷将严格处理违法用户");
            } else if (url.contains(addFriParam.getApi())) {
                parseVerifySuccess(conent);
            } else if (url.contains(pullParam.getApi())) {
                int status = new JSONObject(conent).getInt("status");
                if (Const.HTTP_OK == status) {
                    GUIUtil.toast(instance, "加入黑名单成功");
                    fan.setIspullblack(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ILoadImgListener fanL = new ILoadImgListener() {

        @Override
        public void load(String url, ImageView img) {
            mImageFetcher.loadImage(url, img, GUIUtil.getDpi(instance, R.dimen.margin_8));
        }
    };

    private void parseFanInfo(String json) throws Exception {
        JSONObject jobj = new JSONObject(json);
        if (Const.HTTP_OK == jobj.getInt("status")) {
            JSONObject jdata = jobj.getJSONObject("data");
            JSONObject jFan = jdata.getJSONObject("users");
            fan = JSONTool.jsonToBean(Fan.class, jFan);
            if (!jdata.isNull("sns")) {
                JSONArray jSnsArray = jdata.getJSONArray("sns");
                List<Sns> snss = JSONTool.getJsonToListBean(Sns.class, jSnsArray);
                fan.setSnss(snss);
            }
            if (!jdata.isNull("dynamic")) {
                JSONArray jDynArray = jdata.getJSONArray("dynamic");
                List<Dynamic> dynamics = JSONTool.getJsonToListBean(Dynamic.class, jDynArray);
                fan.setDynamics(dynamics);
            }
            LogUtil.e(getClass(), "fans=>" + fan);
            gotoFanInfoPage(fan);
        }
    }

    private void parseVerifySuccess(String json) throws Exception {
        JSONObject jobj = new JSONObject(json);
        if (jobj.getInt("status") == Const.HTTP_OK) {
            GUIUtil.toast(instance, "通过验证,现在你们可以聊天了");
            fanInfoF.showLayout(FanInfoFragment.LAYOUT_TYPE.SENDMSG);
            SessionManager.getInstance(this).changeHelloToMessage(fan.getUserid());
            BaseMessage msg = MessageUtil.getInstance(this).createFriendMessage(fan.getUserid());
            msg.setMessageType(MessageType.ADDFRIEND);
            BaoguApplication.xmppManager.sendPacket(msg, this);
        } else {
            GUIUtil.toast(instance, "验证失败,请重新验证");
        }
    }

    private void httpFanInfo(String userid) {
        userInfoParam.setUserid(Const.user.getUserid());
        userInfoParam.setLatitude(Const.point.getGeoLat() + "");
        userInfoParam.setLongitude(Const.point.getGeoLng() + "");
        userInfoParam.setDevicetype(Const.DEVICE_TYPE);
        userInfoParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        userInfoParam.setOtheruserid(userid);
        request(userInfoParam);
    }

    private void gotoFanInfoPage(Fan fan) {
        fanInfoF = new FanInfoFragment(fan, fanInfoL);
        replaceFragment(fanInfoF, false);
    }

    private void sayhello() {
        sayihF = new FanSayhiFragment(sayL);
        replaceFragment(sayihF, true);
    }

    private IFanInfoListener fanInfoL = new IFanInfoListener() {

        @Override
        public void gotoWeibo(Fan o, View b) {

        }

        @Override
        public void gotoDynamic(Fan o, View b) {
            Intent intent = new Intent(instance, DynamicFanActivity.class);
            intent.putExtra(DynamicFanActivity.USER_TO_ID, o.getUserid());
            if (Const.user.getUserid().equals(o.getUserid())) {
                intent.putExtra(DynamicFanActivity.TYPE_ID, DynamicFanActivity.DYNAMIC_MINE);
            } else {
                intent.putExtra(DynamicFanActivity.TYPE_ID, DynamicFanActivity.DYNAMIC_OTHER);
            }

            startActivity(intent);
        }

        @Override
        public void verified(Fan o, View b) {
            addFriParam.setUserid(Const.user.getUserid());
            addFriParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
            addFriParam.setDevicetype(Const.DEVICE_TYPE);
            addFriParam.setUseridto(o.getUserid());
            request(addFriParam);
        }

        @Override
        public void forbid(Fan o, View b) {
            httpPullBlack();
        }

        private void httpPullBlack() {
            pullParam.setUseridto(fan.getUserid());
            pullParam.setUserid(Const.user.getUserid());
            pullParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
            pullParam.setDevicetype(Const.DEVICE_TYPE);
            request(pullParam);
        }

        @Override
        public void sayhi() {
            sayhello();
        }

        private Fan reportFan;

        @Override
        public void report(Fan o, View b) {
            reportFan = o;
            showReportView(b);
        }

        private void showReportView(View view) {
            String[] items = new String[]{"色情/暴力信息", "个人资料不当", "盗用他人资料"};
            showButtomPop(onMenuSelect, view, items);
        }

        private BottomPopWindow.OnMenuSelect onMenuSelect = new BottomPopWindow.OnMenuSelect() {
            @Override
            public void onItemMenuSelect(int position) {
                buttomPop.dismiss();
                httpReport(position);
            }

            @Override
            public void onCancelSelect() {
                buttomPop.dismiss();
            }
        };

        private void httpReport(int position) {
            reportParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
            reportParam.setDevicetype(Const.DEVICE_TYPE + "");
            reportParam.setReftype(position + "");
            reportParam.setUploadfile("fuck");
            reportParam.setUserid(Const.user.getUserid());
            reportParam.setUseridto(reportFan.getUserid());
            request(reportParam);
        }

        @Override
        public void sendMsg(Fan o, View b) {
            Intent intent = new Intent(instance, ChatActivity.class);
            intent.putExtra(ChatActivity.ACTIVTY_EX_ID, o.getUserid());
            intent.putExtra(ChatActivity.ACTIVTY_EX_NIKENAME, o.getNickname());
            intent.putExtra(ChatActivity.ACTIVTY_EX_PROFILE, o.getProfileimg());
//            intent.putExtra(ChatActivity.ACTIVTY_EX_DISTANCE,o.getDistance()) ;
            startActivity(intent);
        }

        @Override
        public void fillData(User user) {

        }

        @Override
        public void setHeadImg(String url, ImageView img) {
            mImageFetcher.loadImage(url, img, getResources().getDimensionPixelSize(R.dimen.margin_5));
        }

        @Override
        public void setDynamicImg(String url, ImageView img) {
            if (fan.getDynamic() != null) {
                mImageFetcher.loadImage(url, img, getResources().getDimensionPixelSize(R.dimen.margin_5));
            }
        }

    };

    private IFanSayhiListener sayL = new IFanSayhiListener() {
        @Override
        public void send(String content) {
            sendContent(content);
        }

        private void sendContent(String content) {
            //do something
            if (TextUtil.isEmpty(content)) {
                content = "向你打了招呼";
            }
            TextMessageJson messageJson = new TextMessageJson(content);
            OtherUserInfo info = new OtherUserInfo();
            info.setUserid(fan.getUserid());
            info.setDistance(fan.getDistance());

            BaseMessage message = MessageUtil.getInstance(FanInfoActivity.this).createSendMessage(fan.getUserid(), MessageType.TEXT, messageJson, "0");
            message.setSessionType(SessionType.HELLO);
            BaoguApplication.xmppManager.sendPacket(message, new MessageSendListener() {
                @Override
                public void sendSuccess(Packet message) {
                    LogUtil.e(getClass(), "sendSuccess===");
                    sendMessage(SAY_HELLO_SUCCESS, null);

                }

                @Override
                public void sendFail(BaseMessage message, FailType type) {
                    sendMessage(SAY_HELLO_FAIL, null);

                }
            });
        }

    };

    @Override
    public void sendSuccess(Packet message) {
        LogUtil.v(getClass(), message.toXML());
    }

    @Override
    public void sendFail(BaseMessage message, FailType type) {

    }


    @Override
    public void onDestroy() {
        BaoguApplication.xmppManager.getReciveMessageList().remove(irm);
        super.onDestroy();
    }
}