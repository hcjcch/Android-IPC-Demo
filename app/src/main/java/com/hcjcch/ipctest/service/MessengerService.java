package com.hcjcch.ipctest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 使用 Messenger 通信的Service
 * <p>
 * Created by hcjcch on 2017/5/14.
 */

public class MessengerService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
