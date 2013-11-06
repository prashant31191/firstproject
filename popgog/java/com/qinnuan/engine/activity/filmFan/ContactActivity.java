package com.qinnuan.engine.activity.filmFan;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;

import com.qinnuan.engine.api.filmFan.GetContactsUser;
import com.qinnuan.engine.bean.Fan;
import com.qinnuan.engine.util.view.MyXListView;
import com.qinnuan.engine.xmpp.IReciveMessage;
import com.qinnuan.engine.xmpp.message.MessageUtil;
import com.qinnuan.engine.xmpp.message.SessionType;
import com.qinnuan.common.util.GUIUtil;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.BaseActivity;
import com.qinnuan.engine.adapter.ContactAdapter;
import com.qinnuan.engine.application.BaoguApplication;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.OtherUserInfo;
import com.qinnuan.engine.listener.IOnItemClickListener;
import com.qinnuan.engine.util.view.XListView;
import com.qinnuan.engine.xmpp.MessageSendListener;
import com.qinnuan.engine.xmpp.json.TextMessageJson;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.message.MessageType;
import com.qinnuan.common.util.JSONTool;

import org.jivesoftware.smack.packet.Packet;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JingHuiLiu on 13-7-10.
 */
public class ContactActivity extends BaseActivity {

    private Integer isnext = 0;
    private boolean isNotify = false;
    private int pageindex = 0;
    private ContactActivity instance = this;
    private MyXListView listv;
    private List<Fan> fans;
    private ContactAdapter contactAdapter;
    private GetContactsUser contactParam = new GetContactsUser();
    private Fan fan;

    private StringBuilder phoneNumberSb = new StringBuilder();
    private StringBuilder contactNameSb = new StringBuilder();

    private final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER };
    /** 联系人显示名称 **/
    private final int PHONES_DISPLAY_NAME_INDEX = 0;
    /** 电话号码 **/
    private final int PHONES_NUMBER_INDEX = 1;


    private final int MESSAGE_COME_ADD = 11;
    private final int MESSAGE_COME_DELETE = 44;
    private final int SAY_HELLO_SUCCESS = 22;
    private final int SAY_HELLO_FAIL = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getTitleWidget().getLeftL().setOnClickListener(onClickL);
        listv = (MyXListView)findViewById(R.id.activity_contact_list);
        listv.setPullRefreshEnable(true);
        listv.setPullLoadEnable(true);
        getPhoneContacts();
        LogUtil.e(getClass(), "phone==>" + phoneNumberSb.toString());
        LogUtil.e(getClass(), "name==>" + contactNameSb.toString());
        if (BaoguApplication.xmppManager != null) {
            BaoguApplication.xmppManager.getReciveMessageList().add(irm);
        }
    }

    private void getPhoneContacts() {
        ContentResolver resolver = this.getContentResolver();
        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                phoneNumberSb.append(phoneNumber+"|");

                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                contactNameSb.append(contactName+"|");
            }
            phoneCursor.close();
            httpGetContact();
        }
    }

    private void httpGetContact() {
        contactParam.setDeviceidentifyid(GUIUtil.getDeviceId(instance));
        contactParam.setUserid(Const.user.getUserid());
        contactParam.setDevicetype(Const.DEVICE_TYPE);
        contactParam.setPhonenames(contactNameSb.toString().substring(0, contactNameSb.length()-1));
        contactParam.setPhonenumbers(phoneNumberSb.toString().substring(0, phoneNumberSb.length()-1));
        contactParam.setPageindex(pageindex+"");
        request(contactParam);
    }

    @Override
    protected void requestFailed() {
        super.requestFailed();
    }

    @Override
    protected void requestSuccess(String url, String conent) {
        super.requestSuccess(url, conent);
        try {
            parseContact(conent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        listv.onLoaded();
    }

    private void parseContact(String json) {
        try {
            JSONObject jobj = new JSONObject(json);
            if(Const.HTTP_OK != jobj.getInt("status")) return;
            JSONObject jdata = jobj.getJSONObject("data");
            isnext = jdata.getInt("isnext");
            fans = JSONTool.getJsonToListBean(Fan.class, jdata.getJSONArray("friends"));
            initAdpater();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAdpater() {
        if(!isNotify) {
            contactAdapter = new ContactAdapter(instance, fans, mImageFetcher);
            listv.setAdapter(contactAdapter);
        } else {
            contactAdapter.getModelList().addAll(fans);
            contactAdapter.notifyDataSetChanged();
        }
        contactAdapter.setIOnAddBtnClick(iOnAddBtnClick);
        listv.setIHasNext(ihn);
        listv.setXListViewListener(ixl);
        listv.setOnItemClickListener(onItemClick);

        if(isnext != 1)
            listv.removeFooter();
    }

    private XListView.IXListViewListener ixl = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {

        }

        @Override
        public void onLoadMore() {
            isNotify = true;
            httpGetContact();
        }
    };

    private View.OnClickListener onClickL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case  R.id.left: {
                    finish();
                } break;
                default:break;
            }
        }
    };

    private MyXListView.IHasNext ihn = new MyXListView.IHasNext() {
        @Override
        public Integer getHasNext() {
            return isnext;
        }
    };

    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(view!=listv.getFooterView() && view!=listv.getHeadView()) {
                //fan = contactAdapter.getItem(position);
                //sayHi();
            }
        }
    };

    private IReciveMessage irm = new IReciveMessage() {
        @Override
        public boolean reciveMessage(BaseMessage msg) {
            LogUtil.e(getClass(), "msg==>" + msg.toXML());
            if (msg.getMessageType() == MessageType.ADDFRIEND) {
                LogUtil.e(getClass(), "fromid=>"+msg.getTargetId()+", fanid==>"+fan.getUserid());
                if(msg.getTargetId().equals(fan.getUserid())) {

                }
               /* if(msg.getTargetId().equals(fan.getUserid())) {
                    sendMessage(MESSAGE_COME_ADD, msg);
                }*/
            } /*else if (msg.getMessageType() == MessageType.DELETEFRIEND) {
                if(msg.getTargetId().equals(fan.getUserid())) {
                    sendMessage(MESSAGE_COME_DELETE, msg);
                }
            }*/
            return false;
        }
    };

    private void sendMessage(int what, Object o) {
        android.os.Message msg = new android.os.Message();
        msg.what = what;
        msg.obj = o;
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                default:
                    break;
            }
        }
    };

    private void sayHi() {
        //do something
        TextMessageJson messageJson = new TextMessageJson("请求加你为好友");
        OtherUserInfo info = new OtherUserInfo();
        info.setUserid(fan.getUserid());
        //info.setDistance(fan.getDistance());

        BaseMessage message = MessageUtil.getInstance(instance).
                createSendMessage(fan.getUserid(), MessageType.TEXT, messageJson, "0");
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

    private List<Fan> fanSelect = new ArrayList<Fan>();

    private IOnItemClickListener iOnAddBtnClick = new IOnItemClickListener() {
        @Override
        public void onClick(Object obj) {
            fan = (Fan)obj;
            sayHi();
        }
    };
}