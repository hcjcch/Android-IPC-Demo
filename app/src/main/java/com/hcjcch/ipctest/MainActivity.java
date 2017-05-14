package com.hcjcch.ipctest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hcjcch.ipctest.message.Book;
import com.hcjcch.ipctest.service.AIDLService;

import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_aidl)
    Button buttonAidl;

    @Bind(R.id.btn_messenger)
    Button btnMessenger;

    @Bind(R.id.btn_add_book)
    Button btnAddBook;

    @Bind(R.id.btn_get_all_book)
    Button btnGetAllBook;

    private IBookManager iBookManager;

    private ServiceConnection aidlServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBookManager = IBookManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initListener();
    }

    private void initListener() {
        buttonAidl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AIDLService.class);
                bindService(intent, aidlServiceConnection, BIND_AUTO_CREATE);
            }
        });
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iBookManager == null) {
                    Toast.makeText(MainActivity.this, "先bind service", Toast.LENGTH_SHORT).show();
                    return;
                }
                Book book = new Book(new Random().nextInt(1000), "JAVA");
                try {
                    iBookManager.addBook(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        btnGetAllBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iBookManager == null) {
                    Toast.makeText(MainActivity.this, "先bind service", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    List<Book> books = iBookManager.getBookList();
                    Log.d(AIDLService.TAG, books.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        btnMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
