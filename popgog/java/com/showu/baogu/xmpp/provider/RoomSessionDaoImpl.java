package com.showu.baogu.xmpp.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.showu.baogu.application.Const;
import com.showu.baogu.bean.MessageListBean;
import com.showu.baogu.xmpp.message.MessageSRC;

import java.util.List;

/**
 * Created by showu on 13-8-8.
 */
public class RoomSessionDaoImpl extends AbstractSessionDao{
    public RoomSessionDaoImpl(Context context) {
        super(context);
    }

    @Override
    public void saveOrUpdateSession(MessageListBean bean, MessageSRC target) {
        Cursor c = getRoomSessionByRoomId(bean.getRoom());
        if (c.moveToNext()) {
            int cont = c.getInt(c.getColumnIndex(ChatSession.Session.NOTREADCOUNT));
            if (target == MessageSRC.FROME) {
                bean.setCount(cont + 1);
            } else {
                int member = c.getInt(c.getColumnIndex(ChatSession.Session.MEMBERCOUNT));
                bean.setMembercount(member);
                bean.setCount(0);
            }
            updateRoomMessage(bean);
        } else {
            insertChatSession(bean, target);
        }
    }

    @Override
    public int readSession(MessageListBean bean) {
        return 0;
    }

    @Override
    public int deleteSession(MessageListBean bean) {
       return contentResolver.delete(uri, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.ROOM + " = ?", new String[]{Const.user.getUserid(), bean.getRoom()});
    }

    @Override
    public List<MessageListBean> getSession() {
        return null;
    }

    public Cursor getRoomSessionByRoomId(String room) {
        return contentResolver.query(uri, project, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.ROOM + " = ?",
                new String[]{Const.user.getUserid(), room}, ChatSession.Session.DEFAULT_SORT_ORDER);
    }

    public void updateRoomMessage(MessageListBean bean) {
        contentResolver.update(uri, getContentValues(bean), ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.ROOM + " = ?", new String[]{Const.user.getUserid(), bean.getRoom()});
    }

    public void updateRoomName(String roomId,String roomName){
        ContentValues values= new ContentValues() ;
        values.put(ChatSession.Session.TARGET_USER_NAME,roomName);
        contentResolver.update(uri, values, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.ROOM + " = ?", new String[]{Const.user.getUserid(), roomId});
    }
}
