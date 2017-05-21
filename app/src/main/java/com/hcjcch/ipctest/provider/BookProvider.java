package com.hcjcch.ipctest.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hcjcch.ipctest.db.IPCDbHelper;

/**
 * ContentProvider
 * <p>
 * Created by hcjcch on 2017/5/17.
 */

public class BookProvider extends ContentProvider {
    public static final String TAG = "BookProvider";

    public static final String INTENT_KEY_CALL_ARG = "callArg";
    public static Uri BOOK_CONTENT_URI;
    public static Uri STUDENT_CONTENT_UIi;
    public static final int BOOK_URI_CODE = 0;
    public static final int STUDENT_URI_CODE = 1;
    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private SQLiteDatabase db;

    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);
        BOOK_CONTENT_URI = Uri.parse("content://" + info.authority + "/book");
        STUDENT_CONTENT_UIi = Uri.parse("content://" + info.authority + "/student");
        matcher.addURI(info.authority, "book", BOOK_URI_CODE);
        matcher.addURI(info.authority, "student", STUDENT_URI_CODE);
    }

    @Override
    public boolean onCreate() {
        IPCDbHelper dbHelper = new IPCDbHelper(getContext());
        db = dbHelper.getReadableDatabase();
//        dbHelper.getWritableDatabase().execSQL("insert into book values(1,'JAVA')");
        return true;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query thread :" + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (tableName == null) {
            return null;
        }
        return db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("unSupport URI :" + uri);
        }
        db.insert(tableName, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        int count;
        if (tableName == null) {
            throw new IllegalArgumentException("unSupport URI :" + uri);
        }
        count = db.delete(tableName, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        int count;
        if (tableName == null) {
            throw new IllegalArgumentException("unSupport URI :" + uri);
        }
        count = db.update(tableName, values, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        super.call(method, arg, extras);
        Log.d(TAG, "provider call");
        Log.d(TAG, "call arg " + arg);
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_KEY_CALL_ARG, "call finish");
        return bundle;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (matcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = IPCDbHelper.TABLE_NAME_BOOK;
                break;
            case STUDENT_URI_CODE:
                tableName = IPCDbHelper.TABLE_NAME_STUDENT;
                break;
        }
        return tableName;
    }
}
