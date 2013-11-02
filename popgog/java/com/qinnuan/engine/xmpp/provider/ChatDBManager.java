package com.qinnuan.engine.xmpp.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;

import com.google.gson.Gson;
import com.qinnuan.engine.xmpp.json.ImageMessageJson;
import com.qinnuan.engine.xmpp.json.LocationJson;
import com.qinnuan.engine.xmpp.json.TextMessageJson;
import com.qinnuan.engine.xmpp.json.UserInfo;
import com.qinnuan.engine.xmpp.json.VoiceJson;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.message.MessageSRC;
import com.qinnuan.engine.xmpp.message.MessageType;
import com.qinnuan.engine.xmpp.message.MessageUtil;
import com.qinnuan.common.util.JSONTool;
import com.qinnuan.engine.application.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatDBManager {
    private Context mContext;
    private Uri uri;
    private ContentResolver contentResolver;
    private String[] project = new String[]{ChatMessage.Message.BASE_INFO, ChatMessage.Message.ID,
            ChatMessage.Message.MESSAGE_TYPE, ChatMessage.Message.MESSAGE_BODY,
            ChatMessage.Message.USER_ID,
            ChatMessage.Message.MESSAGE_SEND_TIME,
            ChatMessage.Message.MESSAGE_SRC,
            ChatMessage.Message.ROOM,
            ChatMessage.Message.LOCAL_PATH,
            ChatMessage.Message.TARGET_USER_ID};
    private static ChatDBManager instance;
    private Map<Integer, MessageType> typeMap = new HashMap<Integer, MessageType>();
    private Map<MessageType, Class> providerMap = new HashMap<MessageType, Class>();

    public static ChatDBManager getInstance(Context context) {
        if (instance == null) {
            instance = new ChatDBManager(context);
        }
        return instance;
    }

    private ChatDBManager(Context context) {
        this.mContext = context;
        Builder builder = new Builder();
        uri = builder.scheme("content").authority("com.engine.showu.provider")
                .appendPath("chat_message").build();
        contentResolver = context.getContentResolver();
        for (MessageType type : MessageType.values()) {
            typeMap.put(Integer.parseInt(type.toString()), type);
        }
        providerMap.put(MessageType.TEXT, TextMessageJson.class);
        providerMap.put(MessageType.IMAGE, ImageMessageJson.class);
        providerMap.put(MessageType.VOICE, VoiceJson.class);
        providerMap.put(MessageType.LOCATION, LocationJson.class);
    }

    public void saveMessage(BaseMessage message) {
        ContentValues values = new ContentValues();
        values.put(ChatMessage.Message.ID, message.getId());
        values.put(ChatMessage.Message.BASE_INFO, new Gson().toJson(message.getUserinfo()));
        values.put(ChatMessage.Message.MESSAGE_BODY, message.getBody());
        values.put(ChatMessage.Message.MESSAGE_SRC, message.getMessageSRC().toString());
        values.put(ChatMessage.Message.MESSAGE_SEND_TIME, message.getSendTime());
        values.put(ChatMessage.Message.MESSAGE_TYPE, message.getMessageType().toString());
        values.put(ChatMessage.Message.TARGET_USER_ID, message.getTargetId());
        values.put(ChatMessage.Message.USER_ID, message.getUserId());
        values.put(ChatMessage.Message.MESSAGE_STATUS, message.getStatus());
        values.put(ChatMessage.Message.ROOM, message.getRoom());
        values.put(ChatMessage.Message.LOCAL_PATH, message.getLocalPath());
        values.put(ChatMessage.Message.HELP_TYPE, message.getHelpType());
        contentResolver.insert(uri, values);
        updateUserInfo(message);
    }

    public void deleteMessage(BaseMessage message) {
        int i = contentResolver.delete(uri, ChatMessage.Message.ID + "=?", new String[]{message.getId() + ""});
    }

    public List<BaseMessage> getMessage(String targetId) {
        List<BaseMessage> list = new ArrayList<BaseMessage>();
        Cursor cursor = contentResolver.query(uri, project, ChatMessage.Message.TARGET_USER_ID + "=? AND "
                + ChatMessage.Message.USER_ID + "=?",
                new String[]{targetId, Const.user.getUserid()},
                ChatMessage.Message.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            list.add(getMessage(cursor));
        }
        cursor.close();
        return list;
    }

    public List<BaseMessage> getRoomMessage(String roomId) {
        List<BaseMessage> list = new ArrayList<BaseMessage>();
        Cursor cursor = contentResolver.query(uri, project, ChatMessage.Message.ROOM + "=? AND "
                + ChatMessage.Message.USER_ID + "=?",
                new String[]{roomId, Const.user.getUserid()},
                ChatMessage.Message.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            list.add(getMessage(cursor));
        }
        cursor.close();
        return list;
    }

    public List<BaseMessage> getHelpMessage() {
        List<BaseMessage> list = new ArrayList<BaseMessage>();
        Cursor cursor = contentResolver.query(uri, project, ChatMessage.Message.HELP_TYPE + "=? AND "
                + ChatMessage.Message.USER_ID + "=?",
                new String[]{"1", Const.user.getUserid()},
                ChatMessage.Message.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            list.add(getMessage(cursor));
        }
        cursor.close();
        return list;
    }

    public int toMessageCount(String targetId) {
        Cursor cursor = contentResolver.query(uri, project, ChatMessage.Message.TARGET_USER_ID + "=? AND"
                + ChatMessage.Message.USER_ID + "=? AND " + ChatMessage.Message.MESSAGE_SRC + "=?",
                new String[]{targetId, Const.user.getUserid(), MessageSRC.TO.toString()},
                ChatMessage.Message.DEFAULT_SORT_ORDER);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    private BaseMessage getMessage(Cursor cursor) {
        int type = cursor.getInt(cursor.getColumnIndex(ChatMessage.Message.MESSAGE_TYPE));
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setMessageType(typeMap.get(type));
        baseMessage.setBody(cursor.getString(cursor.getColumnIndex(ChatMessage.Message.MESSAGE_BODY)));
        baseMessage.setId(cursor.getString(cursor.getColumnIndex(ChatMessage.Message.ID)));
        baseMessage.setSendTime(cursor.getLong(cursor.getColumnIndex(ChatMessage.Message.MESSAGE_SEND_TIME)));
        baseMessage.setMessageSRC(cursor.getInt(cursor.getColumnIndex(ChatMessage.Message.MESSAGE_SRC)));
        String userInfo = cursor.getString(cursor.getColumnIndex(ChatMessage.Message.BASE_INFO));
        baseMessage.setUserInfo(JSONTool.jsonToBean(UserInfo.class, userInfo));
        baseMessage.setLocalPath(cursor.getString(cursor.getColumnIndex(ChatMessage.Message.LOCAL_PATH)));
        baseMessage.setRoom(cursor.getString(cursor.getColumnIndex(ChatMessage.Message.ROOM)));
        String targetUserId = cursor.getString(cursor.getColumnIndex(ChatMessage.Message.TARGET_USER_ID));
        String userId = cursor.getString(cursor.getColumnIndex(ChatMessage.Message.USER_ID));
        MessageUtil.getInstance(mContext).messageJsonToObject(baseMessage);
        if (baseMessage.getMessageSRC() == MessageSRC.FROME) {
            baseMessage.setFrom(targetUserId + "@");
            baseMessage.setTo(userId + "@");
        } else {
            baseMessage.setFrom(userId + "@");
            baseMessage.setTo(targetUserId + "@");
        }
        return baseMessage;
    }

    public void deletMessageByTargetId(String targetId) {
        contentResolver.delete(uri, ChatMessage.Message.TARGET_USER_ID + "=?  AND " + ChatMessage.Message.USER_ID + "=?",
                new String[]{targetId.toLowerCase(), Const.user.getUserid().toLowerCase()});
    }

    public void deletHelpMessage() {
        contentResolver.delete(uri, ChatMessage.Message.USER_ID + "=?  AND " + ChatMessage.Message.HELP_TYPE + "=?",
                new String[]{Const.user.getUserid().toLowerCase(), "1"});
    }

    public void cleanAllNotReadmark(Context context) {
        ContentValues values = new ContentValues();
        values.put("MESSAGESTATUS", 0);

        contentResolver.update(uri,
                values,
                "USERID=? AND MESSAGETARGET=1 AND MESSAGETYPE not in (10,11,12)",
                new String[]{Const.user.getUserid().toLowerCase()});
    }

    public void deleteAllSession(Context context) {
        // String sql =
        // "delete from BaseMessage where MESSAGETYPE not in (10,11,12) AND USERID="
        // + Constants.userid.toLowerCase();
        // DBHelper.getInstance(context).exuteSql(sql);
        contentResolver.delete(uri,
                ChatMessage.Message.USER_ID + "=? AND MESSAGETYPE not in (?,?,?)",
                new String[]{Const.user.getUserid(), "10",
                        "11", "12"});
    }

    public void updateMessage(BaseMessage message) {
        ContentValues values = new ContentValues();
        values.put(ChatMessage.Message.MESSAGE_BODY, new Gson().toJson(message.getMessage()));
        values.put(ChatMessage.Message.BASE_INFO, new Gson().toJson(message.getUserinfo()));
        contentResolver.update(uri, values, ChatMessage.Message.ID + "=?", new String[]{message.getId()});
    }

    public void updateUserInfo(BaseMessage message) {
        ContentValues values = new ContentValues();
        values.put(ChatMessage.Message.BASE_INFO, new Gson().toJson(message.getUserinfo()));
        if (message.getMessageSRC() == MessageSRC.TO) {
            contentResolver.update(uri, values, ChatMessage.Message.USER_ID + "=? AND "+ChatMessage.Message.MESSAGE_SRC+" =?", new String[]{Const.user.getUserid(),MessageSRC.TO.toString()});
        }else{
            contentResolver.update(uri, values, ChatMessage.Message.TARGET_USER_ID+ "=? AND "+ChatMessage.Message.MESSAGE_SRC+"=?", new String[]{message.getTargetId(),MessageSRC.FROME.toString()});
        }
    }

    public void deleteRoomById(String roomId) {
        contentResolver.delete(uri,
                ChatMessage.Message.USER_ID + "=? AND " + ChatMessage.Message.ROOM + "=?",
                new String[]{Const.user.getUserid(), roomId});
    }
}
