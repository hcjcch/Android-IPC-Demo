// IBookManager.aidl
package com.hcjcch.ipctest;
import com.hcjcch.ipctest.message.Book;
// Declare any non-default types here with import statements

interface IBookManager {
    void addBook(in Book book);

    List<Book> getBookList();
}
