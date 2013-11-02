package com.qinnuan.engine.xmpp.message;

import android.content.Context;

import com.google.gson.Gson;
import com.qinnuan.engine.xmpp.json.AccountImageJson;
import com.qinnuan.engine.xmpp.json.AccountJson;
import com.qinnuan.engine.application.BaoguApplication;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.application.MyPref;
import com.qinnuan.engine.xmpp.json.AccountActivityJson;
import com.qinnuan.engine.xmpp.json.AccountMultImageTextJson;
import com.qinnuan.engine.xmpp.json.AccountSigleImageTextJson;
import com.qinnuan.engine.xmpp.json.AccountTextJson;
import com.qinnuan.engine.xmpp.json.CinemaJson;
import com.qinnuan.engine.xmpp.json.DynamicNumberJson;
import com.qinnuan.engine.xmpp.json.HelpTextJson;
import com.qinnuan.engine.xmpp.json.ImageMessageJson;
import com.qinnuan.engine.xmpp.json.LastHeadingJson;
import com.qinnuan.engine.xmpp.json.LinkTextJson;
import com.qinnuan.engine.xmpp.json.LocationJson;
import com.qinnuan.engine.xmpp.json.MessageJson;
import com.qinnuan.engine.xmpp.json.TextMessageJson;
import com.qinnuan.engine.xmpp.json.UserInfo;
import com.qinnuan.engine.xmpp.json.VoiceJson;
import com.qinnuan.common.util.LogUtil;

import org.jivesoftware.smack.packet.Message;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by showu on 13-7-12.
 */
public class MessageUtil {
    private static MessageUtil ourInstance;
    private Map<Integer, MessageType> map = new HashMap<Integer, MessageType>();
    private Map<MessageType, Class> providerMap = new HashMap<MessageType, Class>();
    private static String SEVER_ADDRESS = "@xmpptest.popgog.com";
    private Gson gson;

    public static MessageUtil getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new MessageUtil(context);
        }
        return ourInstance;
    }

    private MessageUtil(Context context) {
        registMessageType();
        gson = new Gson();
        MyPref pref = MyPref.getInstance(context);
        SEVER_ADDRESS = "@" + pref.getOpenfirSever();
    }


    private void registMessageType() {
        for (MessageType type : MessageType.values()) {
            map.put(Integer.parseInt(type.toString()), type);
        }
        providerMap.put(MessageType.TEXT, TextMessageJson.class);
        providerMap.put(MessageType.IMAGE, ImageMessageJson.class);
        providerMap.put(MessageType.VOICE, VoiceJson.class);
        providerMap.put(MessageType.LOCATION, LocationJson.class);
        providerMap.put(MessageType.HELPTEXT, HelpTextJson.class);
        providerMap.put(MessageType.HELPCINEMA, CinemaJson.class);
        providerMap.put(MessageType.HELPACCOUNT, AccountJson.class);
        providerMap.put(MessageType.LINKTEXT, LinkTextJson.class);
        providerMap.put(MessageType.DEFUILTLINKTEXT, LinkTextJson.class);
        providerMap.put(MessageType.ACCOUNTTEXT, AccountTextJson.class);
        providerMap.put(MessageType.ACCOUNTSIGLE, AccountSigleImageTextJson.class);
        providerMap.put(MessageType.ACCOUNTMULT, AccountMultImageTextJson.class);
        providerMap.put(MessageType.ACCOUNTIMAGE, AccountImageJson.class);
        providerMap.put(MessageType.ACCOUNTACTIVITY, AccountActivityJson.class);
        providerMap.put(MessageType.DYNAMICNUMBER, DynamicNumberJson.class);
        providerMap.put(MessageType.LASTHEAED, LastHeadingJson.class);
        providerMap.put(MessageType.ADDFRIEND,TextMessageJson.class) ;
    }


    public MessageType getMessageType(int type) {
        return map.get(type);
    }


    public BaseMessage createDynamicNumber(String toId) {
        BaseMessage message = new BaseMessage();
        DynamicNumberJson dynamicJson = new DynamicNumberJson();
        dynamicJson.setBaogu_send_time(getTime());
        dynamicJson.setText("333");
        message.setMessageType(MessageType.DYNAMICNUMBER);
        message.setType(Message.Type.chat);
        message.setTo(toId + SEVER_ADDRESS);
        message.setFrom(Const.user.getUserid() + SEVER_ADDRESS);
        message.setMessageSRC(MessageSRC.TO);
        message.setSessionType(SessionType.DYNAMICNUMBER);
        message.setMessageBody(dynamicJson);
        message.setRoom("0");
        message.setSendTime(getTime());
        UserInfo userInfo = new UserInfo();
        userInfo.setUserid(Const.user.getUserid());
        userInfo.setNickname(Const.user.getNickname());
        userInfo.setHeadImage(Const.user.getProfileimg());
        message.setUserInfo(userInfo);
        return message;
    }

    public BaseMessage createSendMessage(String toJid, MessageType type, MessageJson json, String roomId) {
        BaseMessage message = new BaseMessage();
        message.setMessageType(type);
        message.setType(Message.Type.chat);
        message.setTo(toJid + SEVER_ADDRESS);
        message.setFrom(Const.user.getUserid() + SEVER_ADDRESS);
        message.setMessageSRC(MessageSRC.TO);
        message.setSessionType(SessionType.MESSAGE);
        message.setMessageBody(json);
        message.setRoom(roomId);
        message.setSendTime(getTime());
        UserInfo userInfo = new UserInfo();
        userInfo.setUserid(Const.user.getUserid());
        userInfo.setNickname(Const.user.getNickname());
        userInfo.setHeadImage(Const.user.getProfileimg());
        message.setUserInfo(userInfo);
        return message;
    }

    public BaseMessage createFriendMessage(String toJid) {
        BaseMessage message = new BaseMessage();
        TextMessageJson json = new TextMessageJson("你的好友通过了你的请求!");
        message.setType(Message.Type.chat);
        message.setTo(toJid + SEVER_ADDRESS);
        message.setFrom(Const.user.getUserid() + SEVER_ADDRESS);
        message.setMessageSRC(MessageSRC.TO);
        message.setSessionType(SessionType.MESSAGE);
        message.setMessageBody(json);
        message.setRoom("0");
        message.setSendTime(getTime());
        UserInfo userInfo = new UserInfo();
        userInfo.setUserid(Const.user.getUserid());
        userInfo.setNickname(Const.user.getNickname());
        userInfo.setHeadImage(Const.user.getProfileimg());
        message.setUserInfo(userInfo);
        return message;
    }

    public long getTime() {
        DateFormat format = DateFormat.getDateInstance();
        Date date = null;
        try {
            date = format.parse("1970-01-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long times = (System.currentTimeMillis() + (BaoguApplication.xmppManager.bettewServerTiem
                - 28800000 - date.getTime())) / 1000;

        return times;
    }

    public void messageJsonToObject(BaseMessage message) {
        Class clazz = providerMap.get(message.getMessageType());
        if (clazz != null) {
            LogUtil.e(getClass(),"body=="+message.getBody());
            message.setMessageBody((MessageJson) gson.fromJson(message.getBody(), clazz));
        }
    }
}
