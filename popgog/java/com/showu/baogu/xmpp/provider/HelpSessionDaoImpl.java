package com.showu.baogu.xmpp.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.showu.baogu.application.Const;
import com.showu.baogu.bean.MessageListBean;
import com.showu.baogu.xmpp.message.MessageSRC;
import com.showu.baogu.xmpp.message.SessionType;

import java.util.List;

/**
 * Created by showu on 13-8-8.
 */
public class HelpSessionDaoImpl extends AbstractSessionDao {
    public HelpSessionDaoImpl(Context context) {
        super(context);
    }

    @Override
    public void saveOrUpdateSession(MessageListBean bean, MessageSRC target) {
        Cursor c = getHelpSession();
        if (c.moveToNext()) {
            int cont = c.getInt(c.getColumnIndex(ChatSession.Session.NOTREADCOUNT));
            if (target == MessageSRC.FROME) {
                bean.setCount(cont + 1);
            } else {
                int member = c.getInt(c.getColumnIndex(ChatSession.Session.MEMBERCOUNT));
                bean.setMembercount(member);
                bean.setCount(0);
            }
            updateHelpMessage(bean);
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
                SessionType.HELP.toString()});
    }

    @Override
    public int deleteSession(MessageListBean bean) {
        return 0;
    }

    @Override
    public List<MessageListBean> getSession() {
        return null;
    }

    private void updateHelpMessage(MessageListBean bean) {
        ContentValues msgValues = new ContentValues();
        msgValues.put(ChatSession.Session.LAST_MESSAGE, bean.getContent());
        msgValues.put(ChatSession.Session.USER_HEADIMAGE, bean.getHeadImage() + "");
        msgValues.put(ChatSession.Session.TARGET_USER_ID, bean.getTargetId()
                + "");
        msgValues.put(ChatSession.Session.TARGET_USER_NAME, bean.getName() + "");
        msgValues.put(ChatSession.Session.NOTREADCOUNT, bean.getCount());
        msgValues.put(ChatSession.Session.DISTANCE, bean.getDistanc() + "");
        msgValues.put(ChatSession.Session.MESSAGE_TYPE, bean.getType().toString());
        msgValues.put(ChatSession.Session.USER_ID, Const.user.getUserid());
        msgValues.put(ChatSession.Session.LAST_TIME, bean.getTime());
        msgValues.put(ChatSession.Session.ROOM, bean.getRoom());
        msgValues.put(ChatSession.Session.MEMBERCOUNT, bean.getMembercount());
        contentResolver.update(uri, msgValues, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.MESSAGE_TYPE + " = ?", new String[]{Const.user.getUserid(), SessionType.HELP.toString()});
    }

    private Cursor getHelpSession() {
        return contentResolver.query(uri, project, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.MESSAGE_TYPE + " = ?",
                new String[]{Const.user.getUserid(), SessionType.HELP.toString()}, ChatSession.Session.DEFAULT_SORT_ORDER);
    }
}
