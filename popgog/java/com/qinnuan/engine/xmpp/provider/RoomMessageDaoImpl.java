package com.qinnuan.engine.xmpp.provider;

import android.content.Context;
import android.database.Cursor;

import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.xmpp.message.BaseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by showu on 13-8-8.
 */
public class RoomMessageDaoImpl extends AbstractMessageDao {
    public RoomMessageDaoImpl(Context context) {
        super(context);
    }

    @Override
    public void deleteGroupMeessage(String roomId) {
        contentResolver.delete(uri,
                ChatMessage.Message.USER_ID + "=? AND " + ChatMessage.Message.ROOM + "=?",
                new String[]{Const.user.getUserid(), roomId});
    }

    @Override
    public List<BaseMessage> getMessageList(String roomId) {
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
}
