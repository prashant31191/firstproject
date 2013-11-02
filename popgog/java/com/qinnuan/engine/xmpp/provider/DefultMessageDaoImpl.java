package com.qinnuan.engine.xmpp.provider;

import android.content.Context;
import android.database.Cursor;

import com.qinnuan.engine.xmpp.message.BaseMessage;
import com.qinnuan.engine.application.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by showu on 13-8-8.
 */
public class DefultMessageDaoImpl extends AbstractMessageDao {
    public DefultMessageDaoImpl(Context context) {
        super(context);
    }

    @Override
    public void deleteGroupMeessage(String targetId) {
        contentResolver.delete(uri, ChatMessage.Message.TARGET_USER_ID + "=?  AND " + ChatMessage.Message.USER_ID + "=?",
                new String[]{targetId.toLowerCase(), Const.user.getUserid().toLowerCase()});
    }

    @Override
    public List<BaseMessage> getMessageList(String targetId) {
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
}
