package com.qinnuan.engine.xmpp.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.qinnuan.engine.bean.MessageListBean;
import com.qinnuan.engine.xmpp.message.SessionType;
import com.qinnuan.common.util.LogUtil;
import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.xmpp.message.MessageSRC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by showu on 13-8-8.
 */
public abstract class AbstractSessionDao {
    protected Context mContext;
    protected Uri uri;
    protected ContentResolver contentResolver;


    protected String[] project = new String[]{ChatSession.Session.LAST_MESSAGE,
            ChatSession.Session.USER_HEADIMAGE, ChatSession.Session.TARGET_USER_ID, ChatSession.Session.MEMBERCOUNT,
            ChatSession.Session.TARGET_USER_NAME, ChatSession.Session.NOTREADCOUNT, ChatSession.Session.ROOM, ChatSession.Session.DISTANCE,
            ChatSession.Session.MESSAGE_TYPE, ChatSession.Session.USER_ID, ChatSession.Session.LAST_TIME};
    private Map<Integer,SessionType> sessionType= new HashMap<Integer, SessionType>();
    public AbstractSessionDao(Context context) {
        this.mContext = context;
        Uri.Builder builder = new Uri.Builder();
        uri = builder.scheme("content").authority("com.engine.showu.session.provider")
                .appendPath("chat_session").build();
        contentResolver = context.getContentResolver();
        registSessionType();
    }

    private void registSessionType(){
        for(SessionType type:SessionType.values()){
            sessionType.put(Integer.parseInt(type.toString()),type) ;
        }
    }

    protected Uri insertChatSession(MessageListBean bean, MessageSRC target) {
        return contentResolver.insert(uri,getContentValues(bean));
    }

    protected MessageListBean getBeanFromCursor(Cursor cursor) {
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
        return bean;
    }
    protected ContentValues getContentValues(MessageListBean bean) {
        ContentValues msgValues = new ContentValues();
        msgValues.put(ChatSession.Session.LAST_MESSAGE, bean.getContent());
        msgValues.put(ChatSession.Session.USER_HEADIMAGE, bean.getHeadImage());
        msgValues.put(ChatSession.Session.TARGET_USER_ID, bean.getTargetId());
        msgValues.put(ChatSession.Session.TARGET_USER_NAME, bean.getName());
        msgValues.put(ChatSession.Session.NOTREADCOUNT, bean.getCount());
        msgValues.put(ChatSession.Session.DISTANCE, bean.getDistanc());
        msgValues.put(ChatSession.Session.MESSAGE_TYPE, bean.getType().toString());
        msgValues.put(ChatSession.Session.USER_ID, Const.user.getUserid());
        msgValues.put(ChatSession.Session.LAST_TIME, bean.getTime());
        msgValues.put(ChatSession.Session.ROOM, bean.getRoom());
        msgValues.put(ChatSession.Session.MEMBERCOUNT, bean.getMembercount());
        return  msgValues ;
    }
    public abstract void saveOrUpdateSession(MessageListBean bean,MessageSRC target) ;
    public abstract int readSession(MessageListBean bean) ;
    public abstract int deleteSession(MessageListBean bean) ;
    public abstract List<MessageListBean> getSession() ;

}
