package com.qinnuan.engine.activity.message;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.qinnuan.engine.adapter.HelloMessageAdapter;
import com.qinnuan.engine.api.user.AddFriend;
import com.qinnuan.engine.xmpp.IReciveMessage;
import com.qinnuan.engine.xmpp.message.MessageUtil;
import com.qinnuan.engine.xmpp.message.SessionType;
import com.qinnuan.engine.xmpp.provider.ChatSession;
import com.qinnuan.engine.xmpp.provider.SessionManager;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.widget.TitleWidget;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.application.BaoguApplication;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.fragment.message.HelloFragment;
import com.qinnuan.engine.xmpp.MessageSendListener;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.message.MessageType;

import org.jivesoftware.smack.packet.Packet;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HelloActivity extends BaseActivity implements TitleWidget.IOnClick, MessageSendListener {
    private HelloMessageAdapter adapter;
    private SessionManager sessionContentPro;
    private HelloFragment helloFragment;
    private AddFriend addFriParam = new AddFriend();
    private String userid;
    private int type;
    private final int GET_DATE_SUCCESS = 1;
    private final int RECIVE_HELLO_MSG = 2;
    private List<MessageListBean> list = new ArrayList<MessageListBean>();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_DATE_SUCCESS: {
                    if (list.size() > 0) {
                        setAdapter();
                    } else {
                        finish();
                    }
                    break;
                }

//                case RECIVE_HELLO_MSG: {
//
//                    break;
//                }

                default:
                    break;
            }
        }

        ;
    };

    public void verified(String userid) {
        this.userid = userid;
        addFriParam.setUserid(Const.user.getUserid());
        addFriParam.setDeviceidentifyid(GUIUtil.getDeviceId(this));
        addFriParam.setDevicetype(Const.DEVICE_TYPE);
        addFriParam.setUseridto(userid);
        request(addFriParam);
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        try {
            JSONObject jsonObject = new JSONObject(conent);
            int status = jsonObject.getInt("status");
            if (status == 1) {
                GUIUtil.toast(this, "验证成功");
                SessionManager.getInstance(this).changeHelloToMessage(userid);
                BaseMessage msg = MessageUtil.getInstance(this).createFriendMessage(userid);
                msg.setMessageType(MessageType.ADDFRIEND);
                BaoguApplication.xmppManager.sendPacket(msg, this);
                removeMessage(userid);
                if (list.size() > 0) {
                    helloFragment.notifyDataSetChanged();
                } else {
                    finish();
                }
            }
        } catch (JSONException e) {
            e.getMessage();
        }
    }

    private void removeMessage(String userid) {
        for (int i = 0; i < list.size(); i++) {
            MessageListBean b = list.get(i);
            if (b.getTargetId().equals(userid)) {
                list.remove(b);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_message);
        sessionContentPro = SessionManager.getInstance(this);
        sessionContentPro.readHello();

        if (BaoguApplication.xmppManager != null) {
            BaoguApplication.xmppManager.getReciveMessageList().add(iReciveMessage);
        }
        helloFragment = new HelloFragment();
        addFragment(helloFragment, false);
    }

    private void setAdapter() {
        adapter = new HelloMessageAdapter(this, list, mImageFetcher);
        helloFragment.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        new Thread() {
            public void run() {
                getAllMessage();
            }

            ;
        }.start();
    }

    private void getAllMessage() {
        // 取得会话信息
        list.clear();
        Cursor cursor = null;
        cursor = sessionContentPro.getHelloList();
        while (cursor.moveToNext()) {
            MessageListBean b1 = getBeanFromCursor(cursor);
            MessageListBean b2 = getBeanById(b1.getTargetId());
            if (b2 != null) {
                if (b2.getTime() > b1.getTime()) continue;
                list.remove(b2);
                list.add(b1);
            } else {
                list.add(b1);
            }
        }
        cursor.close();
        Message msg = new Message();
        msg.what = GET_DATE_SUCCESS;
        handler.sendMessage(msg);
    }

    private MessageListBean getBeanById(String id) {
        for (int i = 0; i < list.size(); i++) {
            MessageListBean bean = list.get(i);
            if (bean.getTargetId().equals(id)) {
                return bean;
            }
        }
        return null;
    }

    private MessageListBean getBeanFromCursor(Cursor cursor) {
        MessageListBean bean = new MessageListBean();
        bean.setContent(cursor.getString(cursor
                .getColumnIndex(ChatSession.Session.LAST_MESSAGE)));
        bean.setCount(cursor.getInt(cursor
                .getColumnIndex(ChatSession.Session.NOTREADCOUNT)));
        bean.setDistanc(cursor.getString(cursor
                .getColumnIndex(ChatSession.Session.DISTANCE)));
        bean.setHeadImage(cursor.getString(cursor
                .getColumnIndex(ChatSession.Session.USER_HEADIMAGE)));
        bean.setName(cursor.getString(cursor
                .getColumnIndex(ChatSession.Session.TARGET_USER_NAME)));
        bean.setTargetId(cursor.getString(cursor
                .getColumnIndex(ChatSession.Session.TARGET_USER_ID)));
        bean.setTime(Long.parseLong(cursor.getString(cursor
                .getColumnIndex(ChatSession.Session.LAST_TIME))));
        String type = cursor.getString(cursor
                .getColumnIndex(ChatSession.Session.MESSAGE_TYPE));
        LogUtil.i(getClass(), "getAllMessage()" + type + "-->" + bean.getName());
        if (type.equals(SessionType.HELLO.toString())) {
            bean.setType(SessionType.MESSAGE);
        } else {
            bean.setType(SessionType.MESSAGE);
        }
        return bean;
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            MessageListBean bean = adapter.getItem(position);
            if (bean.getType() == SessionType.MESSAGE) {
                Intent intent = new Intent(HelloActivity.this,
                        ChatActivity.class);
                intent.putExtra("targetId", bean.getTargetId());
                sessionContentPro.markRead(bean.getTargetId());
                HelloActivity.this.startActivity(intent);
            }
        }
    };

    private void creatAlerDialog(final MessageListBean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_message_tip))
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            MessageListBean bean = adapter.getItem(i);
            creatAlerDialog(bean);
            return true;
        }
    };

    private void filmMessageItemClick(MessageListBean bean) {
//        sessionContentPro.markRead(bean.getTargetId());
//        Intent intent = new Intent(this, WantSeePersons.class);
//        intent.putExtra("filmid", bean.getTargetId());
//        startActivity(intent);
    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {

    }

    @Override
    public void centerClick() {

    }

    @Override
    public void sendSuccess(Packet message) {

    }

    @Override
    public void sendFail(BaseMessage message, FailType type) {

    }

    private IReciveMessage iReciveMessage = new IReciveMessage() {
        @Override
        public boolean reciveMessage(BaseMessage msg) {
            getAllMessage();
            sessionContentPro.readHello();
            return false;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BaoguApplication.xmppManager != null) {
            BaoguApplication.xmppManager.getReciveMessageList().remove(iReciveMessage);
        }
    }
}
