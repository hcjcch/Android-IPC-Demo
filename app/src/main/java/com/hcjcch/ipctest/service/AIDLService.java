package com.hcjcch.ipctest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hcjcch.ipctest.BinderPoolManager;

/**
 * 使用 AIDL 通信的Service
 * <p>
 * Created by hcjcch on 2017/5/13.
 */

public class AIDLService extends Service {
    public static final String TAG = "ipc_service";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "AIDLService onCreate");
    }

    @Override
    public void onDestroy() {
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
        return new BinderPoolManager.IBinderPoolImpl();
    }
}
