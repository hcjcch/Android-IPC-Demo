// IOnNewBookArrivedListener.aidl
package com.hcjcch.ipctest;
import com.hcjcch.ipctest.message.Book;

// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
