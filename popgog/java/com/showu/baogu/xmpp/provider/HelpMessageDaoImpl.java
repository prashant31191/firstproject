package com.showu.baogu.xmpp.provider;

import android.content.Context;
import android.database.Cursor;

import com.showu.baogu.application.Const;
import com.showu.baogu.xmpp.message.BaseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by showu on 13-8-8.
 */
public class HelpMessageDaoImpl extends AbstractMessageDao {
    public HelpMessageDaoImpl(Context context) {
        super(context);
    }

    @Override
    public void deleteGroupMeessage(String flag) {
        contentResolver.delete(uri, ChatMessage.Message.USER_ID+ "=?  AND " + ChatMessage.Message.HELP_TYPE+ "=?",
                new String[]{ Const.user.getUserid().toLowerCase(),"1"});
    }

    @Override
    public List<BaseMessage> getMessageList(String primkey) {
        List<BaseMessage> list = new ArrayList<BaseMessage>();
        Cursor cursor = contentResolver.query(uri, project, ChatMessage.Message.HELP_TYPE+ "=? AND "
                + ChatMessage.Message.USER_ID + "=?",
                new String[]{"1", Const.user.getUserid()},
                ChatMessage.Message.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            list.add(getMessage(cursor));
        }
        cursor.close();
        return list;
    }
}
