package com.hcjcch.ipctest;

import android.app.Application;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Application
 * <p>
 * Created by hcjcch on 2017/5/22.
 */

public class IpcApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!isMainProcess(this)) {
            return;
        }
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                BinderPoolManager.getInstance(IpcApplication.this);//提前bindService
            }
        }, BackpressureStrategy.DROP).subscribeOn(Schedulers.io()).subscribe();
    }

    private static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isMainProcess(Context context) {
        String processName = getProcessName();
        return processName == null || processName.equals(context.getPackageName());
    }
}
