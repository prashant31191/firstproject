package com.showu.baogu.xmpp.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.showu.baogu.application.Const;
import com.showu.baogu.bean.MessageListBean;
import com.showu.baogu.xmpp.message.MessageSRC;
import com.showu.baogu.xmpp.message.SessionType;
import com.showu.common.util.TextUtil;

import java.util.List;

/**
 * Created by showu on 13-8-8.
 */
public class DefultSessionDaoImpl extends AbstractSessionDao {
    private DefultSessionDaoImpl(Context context) {
        super(context);
    }

    @Override
    public void saveOrUpdateSession(MessageListBean bean,MessageSRC target) {
        if (bean.getType() == SessionType.HELLO) {//hello  save or update
            Cursor helloSession = getHelloSession(bean.getTargetId());
            if (helloSession.moveToNext()
                    && TextUtil.isEmpty(bean.getIsOfficalUser())) {
                int cont = helloSession.getInt(helloSession
                        .getColumnIndex(ChatSession.Session.NOTREADCOUNT));
                helloSession.close();
                updateHelloSession(bean, target, cont + 1);
            } else {// 无用户记录,或者是公众账号信息
                if (!TextUtil.isEmpty(bean.getIsOfficalUser())) {// 是公众账号
//						bean.setType( SessionType.MESSAGE);
                } else {// 无用户记录
                    bean.setType(SessionType.HELLO);
                }
                if (target == MessageSRC.FROME) {
                    bean.setCount(1);
                } else {
                    bean.setCount(0);
                }
                insertChatSession(bean, target);
            }
        } else if (bean.getType() == SessionType.MESSAGE) {//save or update
            Cursor chatSession = getChatSession(bean.getTargetId());
            if (chatSession.moveToNext()) {// 已经是chat  ChatSession.Session
                int cont = chatSession.getInt(chatSession
                        .getColumnIndex(ChatSession.Session.NOTREADCOUNT));
                if (bean.getTargetId().equals("10000")) {
                    updateChatSession(bean, target, 0);
                } else {
                    if (target == MessageSRC.FROME) {
                        updateChatSession(bean, target, cont + 1);
                    } else {
                        updateChatSession(bean, target, 0);
                    }
                }
            } else {
                if (target == MessageSRC.FROME) {
                    bean.setCount(1);
                } else {
                    bean.setCount(0);
                }
                insertChatSession(bean, target);
            }
        }
    }


    @Override
    public int readSession(MessageListBean bean) {
        ContentValues msgValues = new ContentValues();
        msgValues.put(ChatSession.Session.NOTREADCOUNT, 0);
       return   contentResolver.update(uri, msgValues, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.TARGET_USER_ID + "=? AND " + ChatSession.Session.MESSAGE_TYPE
                + " in (?,?)", new String[]{Const.user.getUserid(),
                bean.getTargetId(), SessionType.HELLO.toString(), SessionType.MESSAGE.toString()});
    }

    @Override
    public int deleteSession(MessageListBean bean) {
        return contentResolver.delete(uri, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.TARGET_USER_ID + "=? AND " + ChatSession.Session.MESSAGE_TYPE
                + " in (?)",
                new String[]{Const.user.getUserid(), bean.getTargetId(),
                        SessionType.MESSAGE.toString()});
    }

    @Override
    public List<MessageListBean> getSession() {
        return null;
    }

    private int updateChatSession(MessageListBean bean, MessageSRC target,
                                  int currentCount) {
        ContentValues msgValues = new ContentValues();
        msgValues.put(ChatSession.Session.LAST_MESSAGE, bean.getContent());
        msgValues.put(ChatSession.Session.USER_HEADIMAGE, bean.getHeadImage());
        msgValues.put(ChatSession.Session.TARGET_USER_NAME, bean.getName());
        msgValues.put(ChatSession.Session.DISTANCE, bean.getDistanc());
        if (target == MessageSRC.TO) {
            msgValues.put(ChatSession.Session.MESSAGE_TYPE, bean.getType().toString());
        }
        msgValues.put(ChatSession.Session.NOTREADCOUNT, currentCount);
        msgValues.put(ChatSession.Session.LAST_TIME, bean.getTime());
        return contentResolver.update(uri, msgValues, ChatSession.Session.USER_ID
                + "=? AND " + ChatSession.Session.TARGET_USER_ID + "=? AND "
                + ChatSession.Session.MESSAGE_TYPE + "=?", new String[]{Const.user.getUserid(),
                bean.getTargetId(), SessionType.MESSAGE.toString()});
    }

    private Cursor getChatSession(String targetUserId) {
        return contentResolver.query(uri, project, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.TARGET_USER_ID + "=? AND " + ChatSession.Session.MESSAGE_TYPE
                + "=?",
                new String[]{Const.user.getUserid(), targetUserId,
                        SessionType.MESSAGE.toString()}, ChatSession.Session.DEFAULT_SORT_ORDER);
    }
    private int updateHelloSession(MessageListBean bean, MessageSRC target,
                                   int currentCount) {
        ContentValues msgValues = new ContentValues();
        msgValues.put(ChatSession.Session.LAST_MESSAGE, bean.getContent());
        msgValues.put(ChatSession.Session.USER_HEADIMAGE, bean.getHeadImage());
        msgValues.put(ChatSession.Session.TARGET_USER_NAME, bean.getName());
        msgValues.put(ChatSession.Session.DISTANCE, bean.getDistanc());
        msgValues.put(ChatSession.Session.NOTREADCOUNT, currentCount);
        msgValues.put(ChatSession.Session.LAST_TIME, bean.getTime());
        return contentResolver.update(uri, msgValues, ChatSession.Session.USER_ID
                + "=? AND " + ChatSession.Session.TARGET_USER_ID + "=? AND "
                + ChatSession.Session.MESSAGE_TYPE + "=?", new String[]{Const.user.getUserid(),
                bean.getTargetId(), SessionType.HELLO.toString()});
    }

    public Cursor getHelloSession(String targetUserId) {
        return contentResolver.query(uri, project, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.TARGET_USER_ID + "=? AND " + ChatSession.Session.MESSAGE_TYPE
                + "=?",
                new String[]{Const.user.getUserid(), targetUserId,
                        SessionType.HELLO.toString()}, ChatSession.Session.DEFAULT_SORT_ORDER);
    }
}
