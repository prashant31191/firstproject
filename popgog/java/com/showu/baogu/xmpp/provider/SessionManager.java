package com.showu.baogu.xmpp.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;

import com.showu.baogu.application.Const;
import com.showu.baogu.bean.HelloInfo;
import com.showu.baogu.bean.MessageListBean;
import com.showu.baogu.xmpp.message.MessageSRC;
import com.showu.baogu.xmpp.message.SessionType;
import com.showu.common.util.TextUtil;

import java.util.ArrayList;
import java.util.List;


public class SessionManager {
    private Context mContext;
    private Uri uri;
    private ContentResolver contentResolver;
    private String[] project = new String[]{ChatSession.Session.LAST_MESSAGE,
            ChatSession.Session.USER_HEADIMAGE, ChatSession.Session.TARGET_USER_ID, ChatSession.Session.MEMBERCOUNT,
            ChatSession.Session.TARGET_USER_NAME, ChatSession.Session.NOTREADCOUNT, ChatSession.Session.ROOM, ChatSession.Session.DISTANCE,
            ChatSession.Session.MESSAGE_TYPE, ChatSession.Session.USER_ID, ChatSession.Session.LAST_TIME};

    private static SessionManager instance;

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    private SessionManager(Context context) {
        this.mContext = context;
        Builder builder = new Builder();
        uri = builder.scheme("content").authority("com.baogu.showu.session.provider")
                .appendPath("chat_session").build();
        contentResolver = context.getContentResolver();
    }

    public void cleanNotmarkRead() {
        ContentValues msgValues = new ContentValues();
        msgValues.put(ChatSession.Session.NOTREADCOUNT, 0);
        contentResolver.update(uri, msgValues, ChatSession.Session.USER_ID + "=? ",
                new String[]{Const.user.getUserid()});
    }

    private Uri insertChatSession(MessageListBean bean, MessageSRC target) {
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
        return contentResolver.insert(uri, msgValues);
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
                + ChatSession.Session.MESSAGE_TYPE + " in (?,?)", new String[]{Const.user.getUserid(),
                bean.getTargetId(), SessionType.MESSAGE.toString(), SessionType.ACCOUNT.toString()});
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

    public void readHello() {
        ContentValues msgValues = new ContentValues();
        msgValues.put(ChatSession.Session.NOTREADCOUNT, 0);
        contentResolver.update(uri, msgValues, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.MESSAGE_TYPE
                + " =?", new String[]{Const.user.getUserid(),
                SessionType.HELLO.toString()});
    }

    // 标记已读
    public int markRead(String targetId) {
        ContentValues msgValues = new ContentValues();
        msgValues.put(ChatSession.Session.NOTREADCOUNT, 0);
        return contentResolver.update(uri, msgValues, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.TARGET_USER_ID + "=? AND " + ChatSession.Session.MESSAGE_TYPE
                + " in (?,?,?)", new String[]{Const.user.getUserid(),
                targetId, SessionType.HELLO.toString(), SessionType.ACCOUNT.toString(), SessionType.MESSAGE.toString()});
    }

    public void readedHelp() {
        ContentValues msgValues = new ContentValues();
        msgValues.put(ChatSession.Session.NOTREADCOUNT, 0);
        contentResolver.update(uri, msgValues, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.MESSAGE_TYPE + "=? ", new String[]{Const.user.getUserid(),
                SessionType.HELP.toString()});
    }

    public int deleteChatSession(String targetUserId) {
        return contentResolver.delete(uri, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.TARGET_USER_ID + "=? AND " + ChatSession.Session.MESSAGE_TYPE
                + " in (?,?)",
                new String[]{Const.user.getUserid(), targetUserId,
                        SessionType.MESSAGE.toString(), SessionType.ACCOUNT.toString()});
    }

    public int deleteHelpSession() {
        return contentResolver.delete(uri, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.MESSAGE_TYPE
                + " in (?)",
                new String[]{Const.user.getUserid(),
                        SessionType.HELP.toString()});
    }

    public void changeHelloToMessage(String targetId) {
        ContentValues msgValues = new ContentValues();
        msgValues.put(ChatSession.Session.MESSAGE_TYPE, SessionType.MESSAGE.toString());
        contentResolver.update(uri, msgValues, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.TARGET_USER_ID + "=?", new String[]{Const.user.getUserid(),
                targetId});
    }

//    public void changeMessageToHello(String targetId) {
//        ContentValues msgValues = new ContentValues();
//        msgValues.put(ChatSession.Session.MESSAGE_TYPE, SessionType.HELLO.toString());
//        contentResolver.update(uri, msgValues, ChatSession.Session.USER_ID + "=? AND "
//                + ChatSession.Session.TARGET_USER_ID + "=?", new String[]{Const.user.getUserid(),
//                targetId});
//    }
    public int cleanChatSession() {
        return contentResolver.delete(uri, ChatSession.Session.USER_ID + "=? ",
                new String[]{Const.user.getUserid()});
    }

    public Cursor getAllChatSession() {
        return contentResolver.query(uri, project, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.MESSAGE_TYPE + " in (?,?,?)", new String[]{Const.user.getUserid(),
                SessionType.MESSAGE.toString(), SessionType.HELP.toString(), SessionType.ACCOUNT.toString()}, ChatSession.Session.DEFAULT_SORT_ORDER);
    }


    public Cursor getChatSession(String targetUserId) {
        return contentResolver.query(uri, project, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.TARGET_USER_ID + "=? AND " + ChatSession.Session.MESSAGE_TYPE
                + " in (?,?)",
                new String[]{Const.user.getUserid(), targetUserId,
                        SessionType.MESSAGE.toString(), SessionType.ACCOUNT.toString()}, ChatSession.Session.DEFAULT_SORT_ORDER);
    }

    public Cursor getHelloSession(String targetUserId) {
        return contentResolver.query(uri, project, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.TARGET_USER_ID + "=? AND " + ChatSession.Session.MESSAGE_TYPE
                + "=?",
                new String[]{Const.user.getUserid(), targetUserId,
                        SessionType.HELLO.toString()}, ChatSession.Session.DEFAULT_SORT_ORDER);
    }

    public Cursor getRoomSessionByRoomId(String room) {
        return contentResolver.query(uri, project, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.ROOM + " = ?",
                new String[]{Const.user.getUserid(), room}, ChatSession.Session.DEFAULT_SORT_ORDER);
    }

    public void updateRoomMessage(MessageListBean bean) {
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
                + ChatSession.Session.ROOM + " = ?", new String[]{Const.user.getUserid(), bean.getRoom()});
    }

    public void saveOrUpdateRoomSession(MessageListBean bean, MessageSRC target) {
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

    public void deleteRoomSession(String roomId) {
        contentResolver.delete(uri, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.ROOM + " = ?", new String[]{Const.user.getUserid(), roomId});
    }

    public void saveOrUpdateSession(MessageListBean bean, MessageSRC target) {
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
        } else if (bean.getType() == SessionType.MESSAGE || bean.getType() == SessionType.ACCOUNT) {//save or update
            Cursor chatSession = getChatSession(bean.getTargetId());
            if (chatSession.moveToNext()) {// 已经是chat  ChatSession.Session
                int cont = chatSession.getInt(chatSession
                        .getColumnIndex(ChatSession.Session.NOTREADCOUNT));
                if (target == MessageSRC.FROME) {
                    updateChatSession(bean, target, cont + 1);
                } else {
                    updateChatSession(bean, target, 0);
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

    // 删除hello信息
    public void deleteHello(String targetId) {
        contentResolver.delete(
                uri,
                ChatSession.Session.USER_ID + "=? AND " + ChatSession.Session.TARGET_USER_ID
                        + "=? AND " + ChatSession.Session.MESSAGE_TYPE + "=?",
                new String[]{Const.user.getUserid(), targetId,
                        SessionType.HELLO.toString()});
    }

    // 获取hello用户的数量和未读条数
    public HelloInfo getHelloInfo() {
        HelloInfo info = new HelloInfo();
        Cursor cursor = contentResolver.query(uri, project, ChatSession.Session.USER_ID
                + "=? AND " + ChatSession.Session.MESSAGE_TYPE + "=?", new String[]{
                Const.user.getUserid(), SessionType.HELLO.toString()},
                ChatSession.Session.DEFAULT_SORT_ORDER);
        info.setUserCount(cursor.getCount());
        int count = 0;
        while (cursor.moveToNext()) {
            int noteRead = cursor.getInt(cursor
                    .getColumnIndex(ChatSession.Session.NOTREADCOUNT));
            count += noteRead;
        }
        // info.setNotReadCount(count);
        info.setCount(count);
        if (cursor.moveToFirst()) {
            info.setContent(cursor.getString(cursor
                    .getColumnIndex(ChatSession.Session.LAST_MESSAGE)));
            info.setDistanc(cursor.getString(cursor
                    .getColumnIndex(ChatSession.Session.DISTANCE)));
            info.setHeadImage(cursor.getString(cursor
                    .getColumnIndex(ChatSession.Session.USER_HEADIMAGE)));
            info.setName(cursor.getString(cursor
                    .getColumnIndex(ChatSession.Session.TARGET_USER_NAME)));
            info.setTargetId(cursor.getString(cursor
                    .getColumnIndex(ChatSession.Session.TARGET_USER_ID)));
            info.setTime(Long.parseLong(cursor.getString(cursor
                    .getColumnIndex(ChatSession.Session.LAST_TIME))));
            info.setType(SessionType.HELLO);
        }
        cursor.close();
        info.setName("有" + info.getUserCount() + "个人和你打招呼");
        return info;
    }

    // 获取hello用户的id

    public List<String> getHelloIdList() {
        List<String> list = new ArrayList<String>();
        Cursor cursor = contentResolver.query(uri, project, ChatSession.Session.USER_ID
                + "=? AND " + ChatSession.Session.MESSAGE_TYPE + "=?", new String[]{
                Const.user.getUserid(), SessionType.HELLO.toString()},
                ChatSession.Session.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            String targetId = cursor.getString(cursor
                    .getColumnIndex(ChatSession.Session.TARGET_USER_ID));
            list.add(targetId);
        }
        return list;
    }

    public void deleteHello() {
        contentResolver.delete(uri, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.MESSAGE_TYPE + "=?", new String[]{Const.user.getUserid(),
                SessionType.HELLO.toString()});
    }


    // 获取hello的列表
    public Cursor getHelloList() {
        return contentResolver.query(uri, project, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.MESSAGE_TYPE + "=?", new String[]{Const.user.getUserid(),
                SessionType.HELLO.toString()}, ChatSession.Session.DEFAULT_SORT_ORDER);
    }

    public int countNotReadMessage() {
        Cursor cursor = contentResolver.query(uri, project, ChatSession.Session.USER_ID
                + "=? ", new String[]{Const.user.getUserid()},
                ChatSession.Session.DEFAULT_SORT_ORDER);
        int count = 0;
        while (cursor.moveToNext()) {
            count += cursor.getInt(cursor.getColumnIndex(ChatSession.Session.NOTREADCOUNT));
        }
        cursor.close();
        return count;
    }

    public int getMainNotRead() {
        Cursor cursor = contentResolver.query(uri, project, ChatSession.Session.USER_ID
                + "=? AND " + ChatSession.Session.MESSAGE_TYPE + " in (?,?,?,?)", new String[]{
                Const.user.getUserid(), SessionType.HELLO.toString(), SessionType.HELP.toString(), SessionType.ACCOUNT.toString(), SessionType.MESSAGE.toString()},
                ChatSession.Session.DEFAULT_SORT_ORDER);
        int count = 0;
        while (cursor.moveToNext()) {
            count += cursor.getInt(cursor.getColumnIndex(ChatSession.Session.NOTREADCOUNT));
        }
        cursor.close();
        return count;
    }

    public int getDynamicNotRead() {
        Cursor cursor = contentResolver.query(uri, project, ChatSession.Session.USER_ID
                + "=? AND " + ChatSession.Session.MESSAGE_TYPE + " in (?,?)", new String[]{
                Const.user.getUserid(), SessionType.DYNAMICNUMBER.toString(), SessionType.LASTHEAD.toString()},
                ChatSession.Session.DEFAULT_SORT_ORDER);
        int count = 0;
        while (cursor.moveToNext()) {
            count += cursor.getInt(cursor.getColumnIndex(ChatSession.Session.NOTREADCOUNT));
        }
        cursor.close();
        return count;
    }
    public Cursor getHelpSession() {
        return contentResolver.query(uri, project, ChatSession.Session.USER_ID + "=? AND "
                + ChatSession.Session.MESSAGE_TYPE + " = ?",
                new String[]{Const.user.getUserid(), SessionType.HELP.toString()}, ChatSession.Session.DEFAULT_SORT_ORDER);
    }

    public void saveOrUpdateHelpSession(MessageListBean bean, MessageSRC target) {
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

    public void updateHelpMessage(MessageListBean bean) {
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
}
