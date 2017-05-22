package com.hcjcch.ipctest.binderImpl;

import android.os.RemoteException;
import android.util.Log;

import com.hcjcch.ipctest.INetwork;

import static com.hcjcch.ipctest.service.AIDLService.TAG;

/**
 * Network aidl Impl
 * <p>
 * Created by hcjcch on 2017/5/22.
 */

public class INetworkImpl extends INetwork.Stub {
    @Override
    public void connectNetwork() throws RemoteException {
        Log.d(TAG, "接下来联网");
    }
}
