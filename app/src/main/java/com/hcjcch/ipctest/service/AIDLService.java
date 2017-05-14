package com.hcjcch.ipctest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hcjcch.ipctest.IBookManager;
import com.hcjcch.ipctest.message.Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 使用 AIDL 通信的Service
 * <p>
 * Created by hcjcch on 2017/5/13.
 */

public class AIDLService extends Service {
    public static final String TAG = "ipc_service";
    private List<Book> books = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "AIDLService onCreate");
        books = Collections.synchronizedList(books);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "AIDLService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IBookManager.Stub() {
            @Override
            public void addBook(Book book) throws RemoteException {
                Log.d(TAG, "service addBook");
                books.add(book);
            }

            @Override
            public List<Book> getBookList() throws RemoteException {
                Log.d(TAG, "client getAllBook");
                return books;
            }
        };
    }
}
