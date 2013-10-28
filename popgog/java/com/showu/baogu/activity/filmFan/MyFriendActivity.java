package com.showu.baogu.activity.filmFan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.showu.baogu.R;
import com.showu.baogu.activity.BaseActivity;
import com.showu.baogu.activity.message.ChatActivity;
import com.showu.baogu.activity.user.DynamicFanActivity;
import com.showu.baogu.api.AddReportUser;
import com.showu.baogu.api.filmFan.DeleteFriend;
import com.showu.baogu.api.GetMyfriends;
import com.showu.baogu.api.GetOtheruserinfo;
import com.showu.baogu.api.Giveyouwrite;
import com.showu.baogu.api.PullBlack;
import com.showu.baogu.api.UpdateRemarkname;
import com.showu.baogu.application.BaoguApplication;
import com.showu.baogu.application.Const;
import com.showu.baogu.bean.Dynamic;
import com.showu.baogu.bean.Fan;
import com.showu.baogu.bean.OtherUserInfo;
import com.showu.baogu.bean.Sns;
import com.showu.baogu.bean.User;
import com.showu.baogu.fragment.filmFan.FanSayhiFragment;
import com.showu.baogu.fragment.filmFan.FriendInfoFragment;
import com.showu.baogu.fragment.filmFan.FriendRemarkFragment;
import com.showu.baogu.fragment.filmFan.FriendsFragment;
import com.showu.baogu.listener.IFanSayhiListener;
import com.showu.baogu.listener.IFriendInfoListener;
import com.showu.baogu.listener.ILoadImgListener;
import com.showu.baogu.listener.IUpdateRemarkListener;
import com.showu.baogu.util.view.XListView;
import com.showu.baogu.xmpp.IReciveMessage;
import com.showu.baogu.xmpp.MessageSendListener;
import com.showu.baogu.xmpp.json.TextMessageJson;
import com.showu.baogu.xmpp.message.BaseMessage;
import com.showu.baogu.xmpp.message.MessageType;
import com.showu.baogu.xmpp.message.MessageUtil;
import com.showu.baogu.xmpp.message.SessionType;
import com.showu.baogu.xmpp.provider.ChatDBManager;
import com.showu.baogu.xmpp.provider.FanBlacklistManager;
import com.showu.baogu.xmpp.provider.SessionManager;
import com.showu.common.util.GUIUtil;
import com.showu.common.util.JSONTool;
import com.showu.common.util.LogUtil;
import com.showu.common.util.TextUtil;
import com.showu.common.widget.BottomPopWindow;

import org.jivesoftware.smack.packet.Packet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class MyFriendActivity extends BaseActivity implements MessageSendListener {

    private MyFriendActivity instance = this;
    private List<Fan> fans = new ArrayList<Fan>();

    private FriendsFragment friendF;
    private FriendRemarkFragment friendRemarkF;
    private FriendInfoFragment friendInfoF;
    private FanSayhiFragment sayihF;

    private GetMyfriends friendsParam = new GetMyfriends();
    private GetOtheruserinfo userInfoParam = new GetOtheruserinfo();
    private AddReportUser reportParam = new AddReportUser();

    private UpdateRemarkname updateRemarkParam = new UpdateRemarkname();
    private PullBlack pullParam = new PullBlack();
    private DeleteFriend delFriendParam = new DeleteFriend();
    private Giveyouwrite removeBlcakParam = new Giveyouwrite();


    private Integer isnext = 0;
    private int pageIndex = 0;
    private boolean notifyChange = false;

    public static boolean isDeleteAFriend = false;
    public static String remarkStr;

    private final int MESSAGE_COME_ADD = 11;
    private final int MESSAGE_COME_DELETE = 44;
    private final int SAY_HELLO_SUCCESS = 22;
    private final int SAY_HELLO_FAIL = 33;
    private static int type = 1;
    //private SessionManager sessionManager;

    public static String FAN_ID = "fan_id";
    public static String fanid;

    private Fan fan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fanid = getIntent().getStringExtra(FAN_ID);
        if (TextUtil.isEmpty(fanid)) {
            gotoFriendsPage();
            httpRefreshFriend();
        } else {
            httpFanInfo(fanid);
        }

        //sessionManager = SessionManager.getInstance(this);
        if (BaoguApplication.xmppManager != null) {
            BaoguApplication.xmppManager.getReciveMessageList().add(irm);
        }
    }

    private IReciveMessage irm = new IReciveMessage() {
        @Override
        public boolean reciveMessage(BaseMessage msg) {
            LogUtil.e(getClass(), "msg==>" + msg.toXML());
            if (msg.getMessageType() == MessageType.ADDFRIEND) {
                LogUtil.e(getClass(), "fromid=>"+msg.getTargetId()+", fanid==>"+fan.getUserid());
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

                case MESSAGE_COME_DELETE:
                    type = 2;
                    friendInfoF.showSayHiLayout(type);
                    break;
                case MESSAGE_COME_ADD:
                    type = 1;
                    friendInfoF.showSayHiLayout(type);
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
    public void onResume() {
        super.onResume();
        LogUtil.e(getClass(), "onResume==>");
        type = 1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.e(getClass(), "onActivityResult==>" + resultCode);
    }

    private void gotoFriendsPage() {
        friendF = new FriendsFragment(mImageFetcher);
        replaceFragment(friendF, false);
    }

    private ILoadImgListener loadImgL = new ILoadImgListener() {
        @Override
        public void load(String url, ImageView img) {
            LogUtil.e(getClass(), "mImageFetcher=>" + mImageFetcher);
            mImageFetcher.loadImage(url, img, GUIUtil.getDpi(instance, R.dimen.margin_8));
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
            int status = new JSONObject(conent).getInt("status");
            if (url.contains(userInfoParam.getApi())) {
                parseFanInfo(conent);
            } else if (url.contains(friendsParam.getApi())) {
                parseFans(conent);
                friendF.onLoaded();
            } else if (url.contains(updateRemarkParam.getApi())) {
                if (Const.HTTP_OK == status) {
                    GUIUtil.toast(instance, "备注名修改成功");
                    friendRemarkF.back();
                    fan.setRemarkname(remarkStr);
                }
            } else if (url.contains(pullParam.getApi())) {
                if (Const.HTTP_OK == status) {
                    GUIUtil.toast(instance, "加入黑名单成功");
                    fan.setIspullblack(1);
                    FanBlacklistManager.getInstance(this)
                            .insertFanBlacklist(fan.getUserid());

                }
            } else if (url.contains(delFriendParam.getApi())) {
                if (Const.HTTP_OK == status) {
                    GUIUtil.toast(instance, "删除影友成功");
                    isDeleteAFriend = true;
                    //friendF.notifyChangeData(deleteFan);
                }
                SessionManager.getInstance(this).deleteChatSession(fan.getUserid());
                ChatDBManager.getInstance(this).deletMessageByTargetId(fan.getUserid());
                BaseMessage msg = MessageUtil.getInstance(this).createFriendMessage(fan.getUserid());
                msg.setMessageType(MessageType.DELETEFRIEND);
                BaoguApplication.xmppManager.sendPacket(msg, this);
                friendInfoF.back();
            } else if (url.contains(removeBlcakParam.getApi())) {
                if (Const.HTTP_OK == status) {
                    GUIUtil.toast(instance, "解除黑名单成功");
                    fan.setIspullblack(0);
                    FanBlacklistManager.getInstance(this)
                            .deleteFanBlacklist(fan.getUserid());
                }
            } else if (url.contains(reportParam.getApi())) {
                GUIUtil.toast(instance, "感谢你的举报,包谷将严格处理违法用户");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void httpRefreshFriend() {
        notifyChange = false;
        pageIndex = 0;
        httpFansBySex();
    }

    private void httpFansBySex() {
        friendsParam.setUserid(Const.user.getUserid());
        friendsParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        friendsParam.setDevicetype(Const.DEVICE_TYPE);
        friendsParam.setLatitude(Const.point.getGeoLat() + "");
        friendsParam.setLongitude(Const.point.getGeoLng() + "");
        friendsParam.setPageindex(pageIndex + "");
        request(friendsParam);
    }

    private void parseFans(String json) throws Exception {
        JSONObject jobj = new JSONObject(json);
        if (Const.HTTP_OK == jobj.getInt("status")) {
            JSONObject jdata = jobj.getJSONObject("data");
            isnext = jdata.getInt("isnext");
            List<Fan> fansTmp = JSONTool.getJsonToListBean(Fan.class,
                    //测试时使用的是附近影迷，因为现在好友列表为空
                    //以后改为friends
                    jdata.getJSONArray("friends"));
            //jdata.getJSONArray("nearbyfans"));
            if (!notifyChange) {
                fans.clear();
                fans = fansTmp;
                LogUtil.e(getClass(), "fans size in activity parse=>" + fans.size());
                initAdapter();
            } else {
                notifyAdapter(fansTmp);
            }
            LogUtil.e(getClass(), "fans size=>" + fans.size());
        }
    }

    private void notifyAdapter(List<Fan> fans) {
        if (isnext == 0) {
            friendF.removeFooter();
        }
        friendF.notifyAdapter(fans, isnext);
    }



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
            gotoFriendInfo(fan);
        }
    }

    private void httpFanInfo(String fanid) {
        userInfoParam.setUserid(Const.user.getUserid());
        userInfoParam.setLatitude(Const.point.getGeoLat() + "");
        userInfoParam.setLongitude(Const.point.getGeoLng() + "");
        userInfoParam.setDevicetype(Const.DEVICE_TYPE);
        userInfoParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        userInfoParam.setOtheruserid(fanid);
        request(userInfoParam);
    }

    private void initAdapter() {
        friendF.setIXL(XListener);
        friendF.initAdapter(fans, isnext);
        friendF.setIOnClick(iOnListItemClick);
    }


    public static Fan deleteFan;
    private FriendsFragment.IOnListVItemClick iOnListItemClick = new FriendsFragment.IOnListVItemClick() {
        @Override
        public void onItemClick(Fan fan) {
            deleteFan = fan;
            //用户类型0=普通用户1=枪手用户2=官方/系统用户3=公共帐号
            int type = fan.getUsertype();
            if (type == 3) {
                Intent in = new Intent(instance, PublicInfoActivity.class);
                in.putExtra(PublicInfoActivity.PUBLIC_FAN_ID, fan.getUserid());
                startActivityForResult(in, RESULT_OK);
            } else {
                httpFanInfo(fan.getUserid());
            }
        }
    };

    private void gotoFriendInfo(Fan fan) {
        friendInfoF = new FriendInfoFragment(fan, friendInfoL);

        LogUtil.e(getClass(), "fanid=>" + fanid);
        if (TextUtil.isEmpty(fanid)) {
            replaceFragment(friendInfoF, true);
        } else {
            replaceFragment(friendInfoF, false);
        }
//        if(fan.getUsertype() == 2) {
//            friendInfoF.initTitle();
//        }
    }

    private void gotoFriendRemark(Fan fan) {
        friendRemarkF = new FriendRemarkFragment(fan, updateRemarkL);
        replaceFragment(friendRemarkF, true);
    }

    private IFriendInfoListener friendInfoL = new IFriendInfoListener() {
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

        }

        @Override
        public void forbid(Fan o, View b) {

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
            String[] items = new String[]{"色情内容", "反动政治", "广告欺诈", "谩骂骚扰"};
            showButtomPop(onMenuReportSelect, view, items);
        }

        private BottomPopWindow.OnMenuSelect onMenuReportSelect = new BottomPopWindow.OnMenuSelect() {
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

        private String roomType = "0";

        @Override
        public void sendMsg(Fan o, View b) {
            Intent i = new Intent(instance, ChatActivity.class);
            i.putExtra(ChatActivity.ACTIVTY_EX_ID, o.getUserid());
            i.putExtra(ChatActivity.ACTIVTY_EX_NIKENAME, o.getNickname());
            i.putExtra(ChatActivity.ACTIVTY_EX_PROFILE, o.getProfileimg());
            i.putExtra(ChatActivity.ACTIVTY_EX_ROOM, roomType);
            if(o.getUsertype() == 2) {
                //i.putExtra(ChatActivity.ACTIVTY_EX_TYPE, 2);
                i.putExtra(ChatActivity.ACTIVITY_HELP_ID, true);
            }
            startActivity(i);
        }

        @Override
        public void fillData(User user) {

        }

        @Override
        public void setHeadImg(String url, ImageView img) {
            mImageFetcher.loadImage(url, img,
                    getResources().getDimensionPixelSize(R.dimen.margin_5));
        }

        @Override
        public void setDynamicImg(String[] url, ImageView[] img) {

        }

        @Override
        public void showPop(View view) {
            showFriendView(view);
        }

        @Override
        public void setDynamicImg(String url, ImageView imgv) {
            mImageFetcher.loadImage(url, imgv,
                    GUIUtil.getDpi(instance, R.dimen.margin_5));
        }


        private void showFriendView(View view) {
            String[] items;
            if (fan.getIspullblack() == 1) {
                items = new String[]{"备注", "解除黑名单", "删除影友"};
            } else {
                items = new String[]{"备注", "加入黑名单", "删除影友"};
            }
            showButtomPop(onMenuSelect, view, items);
        }

        private BottomPopWindow.OnMenuSelect onMenuSelect = new BottomPopWindow.OnMenuSelect() {
            @Override
            public void onItemMenuSelect(int position) {
                buttomPop.dismiss();
                switch (position) {
                    case 0:
                        gotoFriendRemark(fan);
                        break;
                    case 1:
                        if (fan.getIspullblack() == 1) {
                            httpRemoveBlack();
                        } else {
                            httpPullBlack();
                        }
                        break;
                    case 2:
                        GUIUtil.showDialog(instance,
                                "删除好友,也会删除你们的聊天记录,确定删除吗?",
                                "删除", "不要", l);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onCancelSelect() {
                buttomPop.dismiss();
            }

            private void httpPullBlack() {
                pullParam.setUseridto(fan.getUserid());
                pullParam.setUserid(Const.user.getUserid());
                pullParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
                pullParam.setDevicetype(Const.DEVICE_TYPE);
                request(pullParam);
            }

            private void httpRemoveBlack() {
                removeBlcakParam.setUserid(Const.user.getUserid());
                removeBlcakParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
                removeBlcakParam.setDevicetype(Const.DEVICE_TYPE);
                removeBlcakParam.setUseridto(fan.getUserid());
                request(removeBlcakParam);
            }

            private void httpDelFriend() {
                delFriendParam.setUseridto(fan.getUserid());
                delFriendParam.setUserid(Const.user.getUserid());
                delFriendParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
                delFriendParam.setDevicetype(Const.DEVICE_TYPE);
                request(delFriendParam);
            }

            GUIUtil.IOnAlertDListener l = new GUIUtil.IOnAlertDListener() {
                @Override
                public void ok() {
                    httpDelFriend();
                }

                @Override
                public void cancel() {

                }
            };
        };

    };

    private void sayhello() {
        sayihF = new FanSayhiFragment(sayL);
        replaceFragment(sayihF, true);
    }

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

            BaseMessage message = MessageUtil.getInstance(instance).createSendMessage(fan.getUserid(), MessageType.TEXT, messageJson, "0");
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

    private IUpdateRemarkListener updateRemarkL = new IUpdateRemarkListener() {
        @Override
        public void save(String remark) {
            remarkStr = remark;
            httpRemarkName(remark);
        }

        private void httpRemarkName(String remark) {
            updateRemarkParam.setUseridto(fan.getUserid());
            updateRemarkParam.setUserid(Const.user.getUserid());
            updateRemarkParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
            updateRemarkParam.setDevicetype(Const.DEVICE_TYPE);
            updateRemarkParam.setRemarkname(remark);
            request(updateRemarkParam);
        }
    };

    private XListView.IXListViewListener XListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            // needDialog = false;
            httpRefreshFriend();
        }

        @Override
        public void onLoadMore() {
            //needDialog = false;
            pageIndex++;
            notifyChange = true;
            httpFansBySex();
        }
    };


    @Override
    public void sendSuccess(Packet message) {

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