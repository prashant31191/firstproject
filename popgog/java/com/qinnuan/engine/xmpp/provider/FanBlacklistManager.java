package com.qinnuan.engine.xmpp.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;

import com.qinnuan.engine.application.Const;
import com.qinnuan.engine.bean.FanBlack;
import com.qinnuan.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


public class FanBlacklistManager {
    private Context mContext;
    private Uri uri;
    private ContentResolver contentResolver;
    private String[] project = new String[]{
            FanBlacklist.Balcklist.MINE_ID,
            FanBlacklist.Balcklist.USER_ID};

    private static FanBlacklistManager instance;

    public static List<FanBlack> fanblacklist;

    public static FanBlacklistManager getInstance(Context context) {
        if (instance == null) {
            instance = new FanBlacklistManager(context);
        }
        return instance;
    }

    private FanBlacklistManager(Context context) {
        this.mContext = context;
        Builder builder = new Builder();
        uri = builder.scheme("content").authority(FanBlacklist.AUTHORITY)
                .appendPath(FanBlacklist.Balcklist.TABLE_NAME).build();
        contentResolver = context.getContentResolver();
        fanblacklist = getFanBlackList();
    }

    private Cursor getAllBlacklist() {
        return contentResolver.query(uri, project,
                FanBlacklist.Balcklist.MINE_ID+"=?", new String[]{Const.user.getUserid()}, null);
    }

    private Cursor getFanBlack(String blackId) {
        return contentResolver.query(uri, project,
                FanBlacklist.Balcklist.MINE_ID+"=? AND "+
                FanBlacklist.Balcklist.USER_ID+"=?",
                new String[]{Const.user.getUserid(), blackId}, null);
    }

    public boolean isInBlacklist(String blackid) {
        int count = getFanBlack(blackid).getCount();
        LogUtil.e(getClass(), "count==>"+count);
        return  count>0 ? true : false;
    }

    public int deleteFanBlacklist(String blackId) {
        return contentResolver.delete(uri, FanBlacklist.Balcklist.MINE_ID+"=? AND "
                +FanBlacklist.Balcklist.USER_ID+"=?", new String[]{Const.user.getUserid(), blackId});
    }

    public void deleteFanBlacklist() {
        List<FanBlack> fbs =  getFanBlackList();
        for (FanBlack f : fbs) {
            deleteFanBlacklist(f.getUserid());
        }
    }

    public List<FanBlack> getFanBlackList() {
        List<FanBlack> list = new ArrayList<FanBlack>();
        Cursor cursor = getAllBlacklist();
        while (cursor.moveToNext()) {
            String userid = cursor.getString(cursor.getColumnIndex(
                    FanBlacklist.Balcklist.USER_ID));
            String mineid = cursor.getString(cursor.getColumnIndex(
                    FanBlacklist.Balcklist.MINE_ID));
            FanBlack fblack = new FanBlack();
            fblack.setMineid(mineid);
            fblack.setUserid(userid);
            list.add(fblack);
        }
        return list;
    }

    public Uri insertFanBlacklist(String blackid) {
        ContentValues msgValues = new ContentValues();
        msgValues.put(FanBlacklist.Balcklist.USER_ID, blackid);
        msgValues.put(FanBlacklist.Balcklist.MINE_ID, Const.user.getUserid());
        return contentResolver.insert(uri, msgValues);
    }

    public void insertFanBlacklist(List<FanBlack> fans) {
        LogUtil.e(getClass(), "fans size=>"+fans.size());
        for (FanBlack fb : fans) {
            ContentValues msgValues = new ContentValues();
            msgValues.put(FanBlacklist.Balcklist.USER_ID, fb.getUserid());
            msgValues.put(FanBlacklist.Balcklist.MINE_ID, Const.user.getUserid());
            contentResolver.insert(uri, msgValues);
        }

    }
}
