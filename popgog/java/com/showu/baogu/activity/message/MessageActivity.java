package com.showu.baogu.activity.message;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.showu.baogu.R;
import com.showu.baogu.activity.BaseActivity;
import com.showu.baogu.activity.MainActivity;
import com.showu.baogu.application.BaoguApplication;
import com.showu.baogu.application.Const;
import com.showu.baogu.bean.HelloInfo;
import com.showu.baogu.bean.MessageListBean;
import com.showu.baogu.fragment.message.ChatSessionFragment;
import com.showu.baogu.fragment.message.ChatSessionFragment.ChatSessionOperation;
import com.showu.baogu.xmpp.IReciveMessage;
import com.showu.baogu.xmpp.XmppPacketListener;
import com.showu.baogu.xmpp.message.BaseMessage;
import com.showu.baogu.xmpp.message.SessionType;
import com.showu.baogu.xmpp.provider.ChatDBManager;
import com.showu.baogu.xmpp.provider.ChatSession;
import com.showu.baogu.xmpp.provider.SessionManager;
import com.showu.common.util.LogUtil;
import com.showu.common.widget.BottomPopWindow;
import com.showu.common.widget.TitleWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends BaseActivity implements IReciveMessage,
        ChatSessionOperation {
    private ListView messageList;
    // private MessageListAdapter adapter;
    private List<MessageListBean> list = new ArrayList<MessageListBean>();
    private List<MessageListBean> tempList = new ArrayList<MessageListBean>();
    //    private TextView titleTextView;
    private PopupWindow morePop;
    private SessionManager sessionContentPro;
    private ChatSessionFragment sessionFragment;
    private final int MESSAGE_LOAD_SUCCESS = 1;
    private final int RECONNECTION = 2;
    private final int NOTCONNCTION = 3;
    private final int CONNECTION_SUCCESS = 4;
    private final int RECIVE_MESSAGE = 5;
    private Map<Integer,SessionType> sessionType= new HashMap<Integer, SessionType>();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:// 更新item 的未读数量
                    TextView view = (TextView) msg.obj;
                    view.setText(msg.arg1 + "");
                    break;
                case MESSAGE_LOAD_SUCCESS:
                    notifyListUI();
                    break;
                case RECIVE_MESSAGE:
                    initMessage();
                    break;
                default:
                    break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registSessionType();
        mImageFetcher.setLoadingImage(R.drawable.defaul_head);
        sessionContentPro = SessionManager.getInstance(this);
//        setContentView(R.layout.activity_message);
//        titleWidget= (TitleWidget) findViewById(R.id.titleBar);
        sessionFragment = new ChatSessionFragment(mImageFetcher);
        sessionFragment.setOperation(this);
//        replaceFragment(R.id.chat_session, sessionFragment);
        addFragment(sessionFragment, false);
//        titleTextView = (TextView) findViewById(R.id.title);
        IntentFilter filter = new IntentFilter(
                XmppPacketListener.MESSAGE_ACTION);
        if (Const.user != null&&BaoguApplication.xmppManager!=null) {
            BaoguApplication.xmppManager.getReciveMessageList().add(this);
        }
    }

    private void notifyListUI() {
        list.clear();
        list.addAll(tempList);
        sessionFragment.notifyListUI(list);
    }

    private void registSessionType(){
        for(SessionType type:SessionType.values()){
            sessionType.put(Integer.parseInt(type.toString()),type) ;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        initMessage();
        if(BaoguApplication.xmppManager!=null){
            BaoguApplication.xmppManager.submitTimeIQ();
        }
    }

    private void initMessage() {
        if (Const.user != null) {
            list.clear();
            sessionFragment.notifyListUI(list);
            getAllMessage();
        }

    }

    @Override
    public boolean reciveMessage(BaseMessage message) {
        LogUtil.i(getClass(), "messageCome");
        if (hasWindowFocus()) {
            sendHandlerMessage(RECIVE_MESSAGE, message);
        }
        return true;
    }


    private void deleteMessage(MessageListBean bean) {
        if (bean.getType() == SessionType.MESSAGE||bean.getType()==SessionType.ACCOUNT) {
            ChatDBManager.getInstance(this).deletMessageByTargetId(
                    bean.getTargetId());
        } else if (bean.getType() == SessionType.HELLO) {
            List<String> idList = sessionContentPro.getHelloIdList();
            for (String id : idList) {
                ChatDBManager.getInstance(this).deletMessageByTargetId(id);
            }
            sessionContentPro.deleteHello();
        } else if (bean.getType() == SessionType.HELP) {
            ChatDBManager.getInstance(this).deletHelpMessage();
        }
        BaoguApplication.activity.deleteMessage();
        initMessage();
    }

    private void creatAlerDialog(final MessageListBean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_message_tip))
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (bean.getRoom().equals("0")) {
                            if (bean.getType() == SessionType.HELP) {
                                sessionContentPro.deleteHelpSession();
                                deleteMessage(bean);
                            } else {
                                sessionContentPro.deleteChatSession(bean.getTargetId());
                                deleteMessage(bean);
                            }
                        } else {
                            sessionContentPro.deleteRoomSession(bean.getRoom());
                            ChatDBManager.getInstance(MessageActivity.this).deleteRoomById(bean.getRoom());
                            BaoguApplication.activity.deleteMessage();
                            initMessage();
                        }
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

    // 招呼被点击
    private void helloItemClick(MessageListBean bean) {
        Intent intent = new Intent(this, HelloActivity.class);
        intent.putExtra("type", 0);
        startActivity(intent);
    }

    // 消息被点击
    private void messageItemClick(MessageListBean bean) {
        LogUtil.e(getClass(), "messageItemClick:" + bean.getTargetId());
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.ACTIVTY_EX_ID, bean.getTargetId());
//        intent.putExtra(ChatActivity.ACTIVTY_EX_DISTANCE,Integer.parseInt(bean.getDistanc()));
        intent.putExtra(ChatActivity.ACTIVTY_EX_ROOM, bean.getRoom());
        intent.putExtra(ChatActivity.ACTIVTY_EX_PROFILE, bean.getHeadImage());
        intent.putExtra(ChatActivity.ACTIVTY_EX_NIKENAME, bean.getName());
        intent.putExtra(ChatActivity.ACTIVTY_EX_TYPE,Integer.parseInt(bean.getType().toString())) ;
        startActivity(intent);
    }

    private void filmMessageItemClick(MessageListBean bean) {
//        sessionContentPro.markRead(bean.getTargetId());
//        Intent intent = new Intent(this, WantSeePersons.class);
//        intent.putExtra("filmid", bean.getTargetId());
//        startActivity(intent);
    }


    // 影圈动态被点击
    private void trendItemClick(MessageListBean bean) {
//        MessageDBHelp.markReadTrends(MessageActivity.this);
//        sessionContentPro.deleteTrendSession();
//        Intent intent = new Intent(MessageActivity.this, CircleTrends.class);
//        if (MessageDBHelp.haveOfficalTrend(this)) {
//            intent.putExtra("num", 0);
//        } else {
//            intent.putExtra("num", bean.getCount());
//        }
//        MessageActivity.this.startActivity(intent);
    }


    private BottomPopWindow.OnMenuSelect onMenuSelect = new BottomPopWindow.OnMenuSelect() {

        @Override
        public void onItemMenuSelect(int position) {
            //buttomPop.dismiss();
            if (position == 0) {// 清除所有未读标记
                ChatDBManager.getInstance(MessageActivity.this).cleanAllNotReadmark(MessageActivity.this);
                sessionContentPro.cleanNotmarkRead();
//                application.getMainActivity().newMessageCome();
            } else if (position == 1) {// 删除最近联系人
                ChatDBManager.getInstance(MessageActivity.this).deleteAllSession(MessageActivity.this);
                sessionContentPro.cleanChatSession();
                // adapter.getList().clear();
                list.clear();
                sessionFragment.notifyListUI(list);
            }
            initMessage();
        }

        @Override
        public void onCancelSelect() {
            //buttomPop.dismiss();
        }
    };

    private void showMorePop() {
//		showButtomPop(onMenuSelect, findViewById(R.id.titleLayout),
//				new String[] { "清除所有未读标记", "删除最近联系人" });
//        Intent inent = new Intent(this, MyFriend.class);
//        startActivity(inent);
    }

    private void getAllMessage() {
        new Thread() {
            public void run() {
                // 取得会话信息
                Cursor cursor = sessionContentPro.getAllChatSession();
                tempList.clear();
                while (cursor.moveToNext()) {
                    LogUtil.e(getClass(), "cursor" + cursor.getString(cursor
                            .getColumnIndex(ChatSession.Session.TARGET_USER_NAME)));
                    tempList.add(getBeanFromCursor(cursor));
                }
                cursor.close();
                // 取得hello消息
                MessageListBean b = sessionContentPro.getHelloInfo();
                if (((HelloInfo) b).getUserCount() > 0) {
                    tempList.add(b);
                }
                sendHandlerMessage(MESSAGE_LOAD_SUCCESS, null);
            }

            ;
        }.start();
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
        bean.setRoom(cursor.getString(cursor.getColumnIndex(ChatSession.Session.ROOM)));
        bean.setMembercount(cursor.getInt(cursor.getColumnIndex(ChatSession.Session.MEMBERCOUNT)));
        bean.setTime(Long.parseLong(cursor.getString(cursor
                .getColumnIndex(ChatSession.Session.LAST_TIME))));
        String type = cursor.getString(cursor
                .getColumnIndex(ChatSession.Session.MESSAGE_TYPE));
        LogUtil.i(getClass(), "getAllMessage()" + type);
        bean.setType(sessionType.get(Integer.parseInt(type)));
//        if (type.equals(SessionType.HELLO.toString())) {
//            bean.setType(SessionType.HELLO);
//        } else if (type.equals(SessionType.HELP.toString())) {
//            bean.setType(SessionType.HELP);
//        } else {
//            bean.setType(SessionType.MESSAGE);
//        }
        return bean;
    }

    private void sendHandlerMessage(int what, Object o) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = o;
        handler.sendMessage(msg);
    }


    @Override
    public void onItemClick(MessageListBean bean) {
        // TODO Auto-generated method stub
        if (bean.getType() == SessionType.HELLO) {
            helloItemClick(bean);
        } else if (bean.getType() == SessionType.MESSAGE||bean.getType()==SessionType.ACCOUNT) {
            messageItemClick(bean);
        } else if (bean.getType() == SessionType.HELP) {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(ChatActivity.ACTIVTY_EX_ID, bean.getTargetId());
//        intent.putExtra(ChatActivity.ACTIVTY_EX_DISTANCE,Integer.parseInt(bean.getDistanc()));
            intent.putExtra(ChatActivity.ACTIVTY_EX_ROOM, bean.getRoom());
            intent.putExtra(ChatActivity.ACTIVTY_EX_PROFILE, bean.getHeadImage());
            intent.putExtra(ChatActivity.ACTIVTY_EX_NIKENAME, bean.getName());
            intent.putExtra(ChatActivity.ACTIVITY_HELP_ID, true);
            startActivity(intent);
        }
    }

    @Override
    public void onItemLongClick(MessageListBean bean) {
        // TODO Auto-generated method stub
//        if (bean.getType() != SessionType.TIME
//                && bean.getType() != SessionType.TRENDS) {
        creatAlerDialog(bean);
//        }
    }
}
