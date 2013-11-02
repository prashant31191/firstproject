/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qinnuan.engine.xmpp.provider;

import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.qinnuan.common.util.LogUtil;


/**
 * Provides access to a database of notes. Each note has a title, the note
 * itself, a creation date and a modified data.
 */
public abstract class PopApstractProvide extends ContentProvider {
    private static final String DATABASE_NAME = "POPGOG.db";
    private static final int DATABASE_VERSION = 2;
    protected DatabaseHelper mOpenHelper;

    /**
     * This class helps open, create, and upgrade the database file.
     */
    protected static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createChatSessionTable(db);
            createFanBlacklistTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion) {

                if(oldVersion == 1) {
                    createFanBlacklistTable(db);
                }

//                db.execSQL("DROP TABLE IF EXISTS " + ChatSession.Session.TABLE_NAME);
//                db.execSQL("DROP TABLE IF EXISTS " + ChatMessage.Message.TABLE_NAME);
//                onCreate(db);
            }
        }



        //verson 1 table
        private void createChatSessionTable(SQLiteDatabase db) {
            String sessionTable = "CREATE TABLE " + ChatSession.Session.TABLE_NAME + " (" + ChatSession.Session._ID
                    + " INTEGER PRIMARY KEY," + ChatSession.Session.TARGET_USER_ID + " TEXT,"
                    + ChatSession.Session.NOTREADCOUNT + " INTEGER,"
                    + ChatSession.Session.MEMBERCOUNT+ " INTEGER,"
                    + ChatSession.Session.LAST_MESSAGE + " TEXT,"
                    + ChatSession.Session.TARGET_USER_NAME + " TEXT,"
                    + ChatSession.Session.USER_HEADIMAGE + " TEXT,"
                    + ChatSession.Session.LAST_TIME + " TEXT,"
                    + ChatSession.Session.DISTANCE + " TEXT,"
                    + ChatSession.Session.ROOM + " TEXT,"
                    + ChatSession.Session.USER_ID + " TEXT," + ChatSession.Session.MESSAGE_TYPE + " TEXT" + ");" ;
            String  messageTable="CREATE TABLE " + ChatMessage.Message.TABLE_NAME + " (" + ChatMessage.Message._ID
                    + " INTEGER PRIMARY KEY," + ChatMessage.Message.TARGET_USER_ID + " TEXT,"
                    + ChatMessage.Message.MESSAGE_BODY + " TEXT,"
                    + ChatMessage.Message.MESSAGE_SRC + " INTEGER,"
                    + ChatMessage.Message.MESSAGE_SEND_TIME + " INTEGER,"
                    + ChatMessage.Message.HELP_TYPE+ " INTEGER,"
                    + ChatMessage.Message.MESSAGE_STATUS + " INTEGER,"
                    + ChatMessage.Message.BASE_INFO + " TEXT,"
                    + ChatMessage.Message.ID+ " TEXT,"
                    + ChatMessage.Message.ROOM + " TEXT,"
                    + ChatMessage.Message.LOCAL_PATH + " TEXT,"
                    + ChatMessage.Message.USER_ID + " TEXT," + ChatMessage.Message.MESSAGE_TYPE + " INTEGER" + ");";
            LogUtil.e(getClass(), sessionTable);
            LogUtil.e(getClass(),messageTable);
            db.execSQL(sessionTable);
            db.execSQL(messageTable);

        }


        //verson 2 table
        private void createFanBlacklistTable(SQLiteDatabase db) {
            String blacklistTable = "CREATE TABLE " +  FanBlacklist.Balcklist.TABLE_NAME +
                    " (" + FanBlacklist.Balcklist._ID
                    + " INTEGER PRIMARY KEY," + FanBlacklist.Balcklist.USER_ID + " TEXT,"
                    + FanBlacklist.Balcklist.MINE_ID + " TEXT);";

            LogUtil.e(getClass(), blacklistTable);
            db.execSQL(blacklistTable);
        }
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }


}
