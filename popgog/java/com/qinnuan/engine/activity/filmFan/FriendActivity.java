package com.qinnuan.engine.activity.filmFan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.qinnuan.engine.xmpp.IReciveMessage;
import com.qinnuan.engine.xmpp.provider.DynamicNumberSessionDaoImpl;
import com.qinnuan.engine.xmpp.provider.SessionManager;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.activity.user.DynamicActivity;
import com.qinnuan.engine.activity.user.UserActivity;
import com.qinnuan.engine.api.UpdateIsshow;
import com.qinnuan.engine.application.BaoguApplication;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.fragment.filmFan.FriendFragment;
import com.qinnuan.engine.listener.IFriendHomeListener;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.provider.LastHeadSessionDaoImpl;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.LogUtil;

import java.util.List;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class FriendActivity extends BaseActivity {

    private FriendActivity instance = this;
    private FriendFragment friendF;
//    private int count = 0;
    private String headUrl;

    private final int MESSAGE_COME = 1;
    private SessionManager sessionManager;
    private LastHeadSessionDaoImpl headImpl;
    private DynamicNumberSessionDaoImpl numImpl;

    private UpdateIsshow isShowParam = new UpdateIsshow();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gotoFriendHome();
        sessionManager = SessionManager.getInstance(this);
        if (BaoguApplication.xmppManager != null) {
            BaoguApplication.xmppManager.getReciveMessageList().add(irm);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(Const.user==null) {

        } else {
            xmppDynamicMsg();
        }
    }

    @Override
    protected void requestFailed() {
        super.requestFailed();
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        if(url.contains(isShowParam.getApi())) {

        }
    }

    private void gotoFriendHome() {
        friendF = new FriendFragment(friendL);
        replaceFragment(friendF, false);
    }

    private IFriendHomeListener friendL = new IFriendHomeListener() {
        @Override
        public void gotoMyFriend() {
            startActivity(new Intent(instance, MyFriendActivity.class));
        }

        @Override
        public void gotoFan() {
            startActivity(new Intent(instance, FanActivity.class));
        }

        @Override
        public void gotoCinemaCircle() {
            if(headImpl != null) {
                headImpl.deleteSession(null);

//                LogUtil.e(getClass(), "==deleteSession=="
//                        +headImpl.getSession().get(0).getHeadImage());
                //friendF.clearMsgUI();
            }

            LogUtil.e(getClass(), "numImpl:"+numImpl);
//            if(numImpl != null) {
//                numImpl.deleteSession(null);
//                //friendF.clearMsgUI();
//            }

            if(Const.user != null) {
                Const.user.setIsshow(1);//修改本地数据，出现在附近
                startActivity(new Intent(instance, DynamicActivity.class));
            } else {
                GUIUtil.checkIsOnline(instance, UserActivity.class);
            }
        }

        @Override
        public void gotoAddFriend() {
            startActivity(new Intent(instance, FriendAddHomeActivity.class));
        }

        @Override
        public void updateIsShow(String isShow) {
            isShowParam.setDevicetype(Const.DEVICE_TYPE);
            isShowParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
            isShowParam.setUserid(Const.user.getUserid());
            isShowParam.setIsshow(isShow);
            request(isShowParam);
        }
    };


    private void xmppDynamicMsg() {
        int count = 0;
        numImpl = new DynamicNumberSessionDaoImpl(this);
        List<MessageListBean> msgCounts = numImpl.getSession();
        if(msgCounts != null) {
            count = msgCounts.get(0).getCount();
            LogUtil.e(getClass(), "count=>"+count);
        }

        headImpl = new LastHeadSessionDaoImpl(this);
        List<MessageListBean> msgHeads = headImpl.getSession();
        if(msgHeads != null) {
            headUrl = msgHeads.get(0).getHeadImage();
        } else {
            headUrl = null;
        }

        friendF.fillData(count, headUrl, mImageFetcher);
    }

    private IReciveMessage irm = new IReciveMessage() {
        @Override
        public boolean reciveMessage(BaseMessage msg) {
            sendMessage(MESSAGE_COME, msg);
            return false;
        }
    };

    private void initMsgNumber() {
        if (Const.user != null) {
            xmppDynamicMsg();
        }
    }

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
                case MESSAGE_COME:
                    initMsgNumber();
                    break;
                default:break;
            }
        }
    };

    @Override
    public void onDestroy() {
        if(BaoguApplication.xmppManager != null)
            BaoguApplication.xmppManager.getReciveMessageList().remove(irm);
        super.onDestroy();
    }

}