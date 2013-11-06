package com.qinnuan.engine.xmpp.message;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qinnuan.engine.activity.filmFan.MyFriendActivity;
import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.xmpp.json.UserInfo;
import com.qinnuan.common.http.image.util.ImageFetcher;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.common.util.TextUtil;
import com.qinnuan.engine.R;
import com.qinnuan.engine.activity.filmFan.PublicInfoActivity;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.xmpp.json.MessageJson;


import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by showu on 13-7-4.
 */
public class BaseMessage extends Message implements View.OnClickListener {
    private static final String USERINFO = "userinfo";
    private static final String SEND_TIME = "baogu_send_time";
    private static final String MESSAGE_TYPE = "baogu_message_type";
    private static final String SESSION_TYPE = "session_type";
    private static final String ROOM = "room";
    private UserInfo userInfo;
    private long sendTime;
    private MessageType messageType = MessageType.TEXT;
    private MessageSRC messageSRC;
    private SessionType sessionType;
    private String id;
    private Gson gson;
    private MessageJson message;
    private int status = 0;//0:成功，1失败 2 发送中
    private String room = "0";//非0时是群聊
    private int helpType = 0;//0 非助手消息，1 助手消息
    private Map<Integer, MessageType> typeMap = new HashMap<Integer, MessageType>();
    private Map<Integer, SessionType> sessionTypeMap = new HashMap<Integer, SessionType>();
    private String localPath;
    private boolean needShowTime = false;
    public List<Element> elements = new ArrayList<Element>();
    private Context mContext;

    public void setHelpType(int helpType) {
        this.helpType = helpType;
    }

    public int getHelpType() {
        return helpType;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public String getLocalPath() {
        return localPath;
    }

    public boolean isNeedShowTime() {
        return needShowTime;
    }

    public void setNeedShowTime(boolean needShowTime) {
        this.needShowTime = needShowTime;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public MessageJson getMessage() {
        return message;
    }


    public BaseMessage() {
        gson = new Gson();
        registMessageType();
        setId(UUID.randomUUID().toString());
    }

    public MessageSRC getMessageSRC() {
        return messageSRC;
    }

    public void setMessageBody(MessageJson message) {
        this.message = message;
//        setBody(gson.toJson(message));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public void setMessageSRC(MessageSRC messageSRC) {
        this.messageSRC = messageSRC;
    }

    public void setMessageSRC(int src) {
        if (src == 0) {
            this.messageSRC = MessageSRC.TO;
        } else {
            this.messageSRC = MessageSRC.FROME;
        }
    }

    public MessageType getMessageType() {
        return messageType;
    }


    public UserInfo getUserinfo() {
        return userInfo;
    }


    public long getSendTime() {
        return sendTime;
    }


    public String getTargetId() {
        if (messageSRC == MessageSRC.FROME) {
            return getFrom().split("@")[0];
        } else {
            return getTo().split("@")[0];
        }
    }

    public String getUserId() {
        if (messageSRC == MessageSRC.FROME) {
            return getTo().split("@")[0];
        } else {
            return getFrom().split("@")[0];
        }
    }


    public void setUserInfo(UserInfo userinfo) {
        this.userInfo = userinfo;
    }

    public void setSendTime(long time) {
        this.sendTime = time;
    }

    public void action(Context context) {
        message.action(context, this);
    }

    public View getItemView(Context context, ImageFetcher fetcher, View.OnClickListener onClickListener) {
        this.mContext = context;
        if (message != null) {
            View view = message.getItem(context, this, fetcher);
            View headView = view.findViewById(R.id.headImage);
            if (headView != null) {
                view.findViewById(R.id.headImage).setOnClickListener(this);
            }
            if(!room.equals("0")) {
                String nickname = userInfo.getNickname();
                if(!TextUtil.isEmpty(nickname)) {
                    TextView fanName = (TextView)view.findViewById(R.id.fan_name);
                    if(fanName != null) {
                        fanName.setVisibility(View.VISIBLE);
                        fanName.setText(nickname);
                    }
                }
            }
            return view;
        } else {
            TextView text = new TextView(context);
            return text;
        }
    }

    public void parseXml(String key, String value) {
        if (key.equals(SEND_TIME)) {
            long v = Long.parseLong(value.split("\\.")[0]);
            setSendTime(v);
        } else if (key.equals("body")) {
            setBody(value);
        } else if (key.equals(MESSAGE_TYPE)) {
            setMessageType(typeMap.get(Integer.parseInt(value)));
        } else if (key.equals(ROOM)) {
            setRoom(value);
        } else if (key.equals(USERINFO)) {
            setUserInfo(JSONTool.jsonToBean(UserInfo.class, value));
        } else if (key.equals("to")) {
            setTo(value);
        } else if (key.equals("from")) {
            setFrom(value);
        } else if (key.equals(SESSION_TYPE)) {
            setSessionType(sessionTypeMap.get(Integer.parseInt(value)));
        } else {
            LogUtil.i(BaseMessage.class, "Not suport this XML tag " + key);
        }
    }

    private void registMessageType() {
        for (MessageType type : MessageType.values()) {
            typeMap.put(Integer.parseInt(type.toString()), type);
        }
        for (SessionType type : SessionType.values()) {
            sessionTypeMap.put(Integer.parseInt(type.toString()), type);
        }
    }

    @Override
    public String toXML() {
        setBody(gson.toJson(message));
        String xml = super.toXML();
        String target = xml.substring(0, xml.indexOf("</message>"));
        target = target + getBaoguXML() + "</message>";
        return target;
    }

    public void constactXML() {
        elements.clear();
//        LogUtil.d(getClass(), "constactXML=" + elements.size());
        elements.add(new Element(SEND_TIME, sendTime));
        elements.add(new Element(MESSAGE_TYPE, messageType.toString()));
        elements.add(new Element(USERINFO, gson.toJson(userInfo)));
        elements.add(new Element(ROOM, room));
        elements.add(new Element(SESSION_TYPE, sessionType.toString()));
        elements.add(new Element("body", gson.toJson(message)));
    }

    public String getBaoguXML() {
        constactXML();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < elements.size(); i++) {
            Element e = elements.get(i);
            if (e.getName() != null) {
                sb.append("<" + e.getName() + ">");
                sb.append(e.getValue());
                sb.append("</" + e.getName() + ">");
            }
        }
        return sb.toString();
    }

    public MessageListBean getSession() {
        MessageListBean messageListBean = message.getSessionBean();
        if (room.equals("0")) {
            if (TextUtil.isEmpty(messageListBean.getHeadImage())) {
                messageListBean.setHeadImage(userInfo.getHeadImage());
            }
            messageListBean.setName(userInfo.getNickname());
        } else {
            messageListBean.setName(userInfo.getGroupname());
            messageListBean.setHeadImage(userInfo.getFilmimgurl());
            messageListBean.setMembercount(userInfo.getMembercount());
        }
        messageListBean.setRoom(room);
        messageListBean.setTime(sendTime);
        messageListBean.setType(sessionType);
        messageListBean.setCount(1);
        if(room.equals("0")){
            messageListBean.setTargetId(userInfo.getUserid());
        }else{
            messageListBean.setTargetId(getTargetId());
        }
        return messageListBean;
    }

    private boolean isAccount() {
        switch (messageType) {
            case ACCOUNTACTIVITY:
                return true;
            case ACCOUNTIMAGE:
                return true;
            case ACCOUNTTEXT:
                return true;
            case ACCOUNTMULT:
                return true;
            case ACCOUNTSIGLE:
                return true;
            case LINKTEXT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onClick(View view) {
        if (!userInfo.getUserid().equals(Const.user.getUserid())) {
            if(isAccount()){
                Intent intent = new Intent(mContext, PublicInfoActivity.class);
                intent.putExtra(PublicInfoActivity.PUBLIC_FAN_ID, userInfo.getUserid());
                mContext.startActivity(intent);
            }else{
            Intent intent = new Intent(mContext, MyFriendActivity.class);
            intent.putExtra(MyFriendActivity.FAN_ID, userInfo.getUserid());
            mContext.startActivity(intent);
            }
        }
    }
}
