package com.hcjcch.ipctest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.hcjcch.ipctest.binderImpl.IBookManagerImpl;
import com.hcjcch.ipctest.binderImpl.INetworkImpl;
import com.hcjcch.ipctest.service.AIDLService;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

/**
 * Binder连接池的管理
 * <p>
 * Created by hcjcch on 2017/5/22.
 */

public class BinderPoolManager {

    static final int BINDER_BOOK = 0;
    static final int BINDER_NETWORK = 1;
    private static volatile BinderPoolManager instance;
    private CountDownLatch countDownLatch;
    private IBinderPool iBinderPool;
    /**
     * 使用弱应用，防止内存泄漏
     */
    private WeakReference<Context> context;

    private BinderPoolManager(Context context) {
        this.context = new WeakReference<>(context);
        connectService();
    }

    static BinderPoolManager getInstance(Context context) {
        if (instance == null) {
            synchronized (BinderPoolManager.class) {
                if (instance == null) {
                    instance = new BinderPoolManager(context);
                }
            }
        }
        return instance;
    }

    private ServiceConnection binderPoolServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                iBinderPool.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            iBinderPool.asBinder().unlinkToDeath(deathRecipient, 0);
            iBinderPool = null;
            connectService();
        }
    };

    private synchronized void connectService() {
        countDownLatch = new CountDownLatch(1);
        if (context.get() != null) {
            Intent intent = new Intent(context.get(), AIDLService.class);
            context.get().bindService(intent, binderPoolServiceConnection, Context.BIND_AUTO_CREATE);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    IBinder queryBinder(int binderCode) {
        switch (binderCode) {
            case BINDER_BOOK:
            case BINDER_NETWORK:
                try {
                    return iBinderPool.queryBinder(binderCode);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return null;
                }
            default:
                return null;
        }
    }

    public static class IBinderPoolImpl extends IBinderPool.Stub {

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            switch (binderCode) {
                case BINDER_BOOK:
                    return new IBookManagerImpl();
                case BINDER_NETWORK:
                    return new INetworkImpl();
                default:
                    return null;
            }
        }
    }
}
