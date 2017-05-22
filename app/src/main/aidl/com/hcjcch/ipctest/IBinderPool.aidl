// IBinderPool.aidl
package com.hcjcch.ipctest;
// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
