package com.qinnuan.engine.xmpp.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;
import com.qinnuan.engine.xmpp.json.UserInfo;
import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.xmpp.message.MessageSRC;
import com.qinnuan.engine.xmpp.message.MessageType;
import com.qinnuan.engine.xmpp.message.MessageUtil;
import com.qinnuan.common.util.JSONTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.net.Uri.Builder;

/**
 * Created by showu on 13-8-8.
 */
public abstract class AbstractMessageDao {
    protected Context mContext;
    protected Uri uri;
    protected ContentResolver contentResolver;
    private MessageUtil messageUtil ;
    protected String[] project = new String[]{ChatMessage.Message.BASE_INFO, ChatMessage.Message.ID,
            ChatMessage.Message.MESSAGE_TYPE, ChatMessage.Message.MESSAGE_BODY,
            ChatMessage.Message.USER_ID,
            ChatMessage.Message.MESSAGE_SEND_TIME,
            ChatMessage.Message.MESSAGE_SRC,
            ChatMessage.Message.ROOM,
            ChatMessage.Message.LOCAL_PATH,
            ChatMessage.Message.TARGET_USER_ID};
    protected static Map<Integer, MessageType> typeMap = new HashMap<Integer, MessageType>();

    public AbstractMessageDao(Context context) {
        this.mContext = context;
        Builder builder = new Builder();
        uri = builder.scheme("content").authority("com.engine.showu.provider")
                .appendPath("chat_message").build();
        contentResolver = context.getContentResolver();
        messageUtil=MessageUtil.getInstance(mContext);
        for (MessageType type : MessageType.values()) {
            typeMap.put(Integer.parseInt(type.toString()), type);
        }
    }

    protected void saveMessage(BaseMessage message) {
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
        values.put(ChatMessage.Message.HELP_TYPE,message.getHelpType());
        contentResolver.insert(uri, values);
    }
    protected BaseMessage getMessage(Cursor cursor) {
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
        messageUtil.messageJsonToObject(baseMessage);
        if (baseMessage.getMessageSRC() == MessageSRC.FROME) {
            baseMessage.setFrom(targetUserId + "@");
            baseMessage.setTo(userId + "@");
        } else {
            baseMessage.setFrom(userId + "@");
            baseMessage.setTo(targetUserId + "@");
        }
        return baseMessage;
    }
    public void deleteMessage(BaseMessage message) {
        int i = contentResolver.delete(uri, ChatMessage.Message.ID + "=?", new String[]{message.getId() + ""});
    }

    public abstract void deleteGroupMeessage(String flag);
    public  abstract List<BaseMessage> getMessageList(String primkey) ;
}
