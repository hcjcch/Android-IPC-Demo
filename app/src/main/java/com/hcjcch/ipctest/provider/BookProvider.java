package com.hcjcch.ipctest.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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

    public static Uri BOOK_CONTENT_URI;
    public static Uri STUDENT_CONTENT_UIi;
    public static final int BOOK_URI_CODE = 0;
    public static final int STUDENT_URI_CODE = 1;
    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private IPCDbHelper dbHelper;

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
        dbHelper = new IPCDbHelper(getContext());
//        dbHelper.getWritableDatabase().execSQL("insert into book values(1,'JAVA')");
        return true;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query thread :" + Thread.currentThread().getName());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
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
