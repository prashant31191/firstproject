package com.showu.baogu.xmpp.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.showu.baogu.application.Const;
import com.showu.baogu.bean.MessageListBean;
import com.showu.baogu.xmpp.message.MessageSRC;
import com.showu.baogu.xmpp.message.SessionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by showu on 13-8-8.
 */
public class DynamicNumberSessionDaoImpl extends AbstractSessionDao {
    public DynamicNumberSessionDaoImpl(Context context) {
        super(context);
    }

    @Override
    public void saveOrUpdateSession(MessageListBean bean, MessageSRC target) {
        Cursor c = getNumberSession();
        if (c.moveToNext()) {
            int cont = c.getInt(c.getColumnIndex(ChatSession.Session.NOTREADCOUNT));
            if (target == MessageSRC.FROME) {
                bean.setCount(cont + 1);
            } else {
                int member = c.getInt(c.getColumnIndex(ChatSession.Session.MEMBERCOUNT));
                bean.setMembercount(member);
                bean.setCount(0);
            }
            updateNumberSession(bean);
        } else {
            insertChatSession(bean, target);
        }
    }

    @Override
    public int readSession(MessageListBean bean) {
        ContentValues msgValues = new ContentValues();
        msgValues.put(ChatSession.Session.NOTREADCOUNT, 0);
        return contentResolver.update(uri, msgValues, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.MESSAGE_TYPE+ "=? ", new String[]{Const.user.getUserid(),
                SessionType.DYNAMICNUMBER.toString()});
    }

    @Override
    public int deleteSession(MessageListBean bean) {
         int i =contentResolver.delete(uri,  ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.MESSAGE_TYPE+ "=? ", new String[]{Const.user.getUserid(),
                SessionType.DYNAMICNUMBER.toString()});
        return i ;
    }

    @Override
    public List<MessageListBean> getSession() {
        Cursor cursor = getNumberSession() ;
        if(cursor.getCount()>0){
            List<MessageListBean> listBeans = new ArrayList<MessageListBean>() ;
            while (cursor.moveToNext()){
                MessageListBean bean=getBeanFromCursor(cursor) ;
                bean.setType(SessionType.DYNAMICNUMBER);
                listBeans.add(bean);
            }
            cursor.close();
            return  listBeans;
        }
        return null;
    }

    private void updateNumberSession(MessageListBean bean) {
        contentResolver.update(uri,getContentValues(bean), ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.MESSAGE_TYPE + " = ?", new String[]{Const.user.getUserid(), SessionType.DYNAMICNUMBER.toString()});
    }

    private Cursor getNumberSession() {
        return contentResolver.query(uri, project, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.MESSAGE_TYPE + " = ?",
                new String[]{Const.user.getUserid(), SessionType.DYNAMICNUMBER.toString()}, ChatSession.Session.DEFAULT_SORT_ORDER);
    }
}
