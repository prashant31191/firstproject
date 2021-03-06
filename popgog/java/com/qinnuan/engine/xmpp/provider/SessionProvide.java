package com.qinnuan.engine.xmpp.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by showu on 13-7-8.
 */
public class SessionProvide extends PopApstractProvide {



    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(ChatSession.Session.TABLE_NAME);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        return qb.query(db, projection, selection, selectionArgs, null, null,
                sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(ChatSession.Session.TABLE_NAME, null, initialValues);
        if (rowId > 0) {
            Uri noteUri = ContentUris
                    .withAppendedId(ChatSession.Session.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.delete(ChatSession.Session.TABLE_NAME, where, whereArgs);
        if (rowId > 0) {
            Uri noteUri = ContentUris
                    .withAppendedId(ChatSession.Session.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return (int) rowId;
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where,
                      String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowId = db.update(ChatSession.Session.TABLE_NAME, values, where, whereArgs);
        return rowId;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }
}
