package com.showu.baogu.xmpp.message;

import android.content.Context;

import com.google.gson.Gson;
import com.showu.baogu.application.BaoguApplication;
import com.showu.baogu.application.Const;
import com.showu.baogu.application.MyPref;
import com.showu.baogu.xmpp.json.AccountActivityJson;
import com.showu.baogu.xmpp.json.AccountImageJson;
import com.showu.baogu.xmpp.json.AccountJson;
import com.showu.baogu.xmpp.json.AccountMultImageTextJson;
import com.showu.baogu.xmpp.json.AccountSigleImageTextJson;
import com.showu.baogu.xmpp.json.AccountTextJson;
import com.showu.baogu.xmpp.json.CinemaJson;
import com.showu.baogu.xmpp.json.DynamicNumberJson;
import com.showu.baogu.xmpp.json.HelpTextJson;
import com.showu.baogu.xmpp.json.ImageMessageJson;
import com.showu.baogu.xmpp.json.LastHeadingJson;
import com.showu.baogu.xmpp.json.LinkTextJson;
import com.showu.baogu.xmpp.json.LocationJson;
import com.showu.baogu.xmpp.json.MessageJson;
import com.showu.baogu.xmpp.json.TextMessageJson;
import com.showu.baogu.xmpp.json.UserInfo;
import com.showu.baogu.xmpp.json.VoiceJson;
import com.showu.common.util.LogUtil;

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
