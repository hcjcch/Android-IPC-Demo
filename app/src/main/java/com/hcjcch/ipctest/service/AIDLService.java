package com.hcjcch.ipctest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hcjcch.ipctest.IBookManager;
import com.hcjcch.ipctest.IOnNewBookArrivedListener;
import com.hcjcch.ipctest.message.Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 使用 AIDL 通信的Service
 * <p>
 * Created by hcjcch on 2017/5/13.
 */

public class AIDLService extends Service {
    public static final String TAG = "ipc_service";
    private List<Book> books = new ArrayList<>();
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
    private RemoteCallbackList<IOnNewBookArrivedListener> registerListeners = new RemoteCallbackList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "AIDLService onCreate");
        books = Collections.synchronizedList(books);
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Book book = new Book(new Random().nextInt(1000), "C++");
                int n = registerListeners.beginBroadcast();
                for (int i = 0; i < n; i++) {
                    IOnNewBookArrivedListener listener = registerListeners.getBroadcastItem(i);
                    if (listener != null) {
                        try {
                            listener.onNewBookArrived(book);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
                registerListeners.finishBroadcast();
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    @Override
    public void onDestroy() {
        scheduledThreadPool.shutdownNow();
        super.onDestroy();
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

            @Override
            public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
                registerListeners.register(listener);
                Log.d(TAG, "监听器数量: " + registerListeners.beginBroadcast());
                registerListeners.finishBroadcast();
            }

            @Override
            public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
                registerListeners.unregister(listener);
                Log.d(TAG, "监听器数量: " + registerListeners.beginBroadcast());
                registerListeners.finishBroadcast();
            }
        };
    }
}
