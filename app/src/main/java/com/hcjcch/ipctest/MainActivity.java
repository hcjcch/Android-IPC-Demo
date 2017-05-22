package com.hcjcch.ipctest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hcjcch.ipctest.message.Book;
import com.hcjcch.ipctest.message.Student;
import com.hcjcch.ipctest.provider.BookProvider;
import com.hcjcch.ipctest.service.AIDLService;
import com.hcjcch.ipctest.service.MessengerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.hcjcch.ipctest.service.MessengerService.INTENT_KEY_STUDENTS;
import static com.hcjcch.ipctest.service.MessengerService.MSG_GET_STUDENTS;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_aidl)
    Button buttonAidl;

    @Bind(R.id.btn_messenger)
    Button btnMessenger;

    @Bind(R.id.btn_aidl_add_book)
    Button btnAidlAddBook;

    @Bind(R.id.btn_aidl_get_all_book)
    Button btnAidlGetAllBook;

    @Bind(R.id.btn_messenger_add_student)
    Button btnMessengerAddStudent;

    @Bind(R.id.btn_messenger_get_all_student)
    Button btnMessengerGetAllStudent;

    @Bind(R.id.btn_provider)
    Button btnProvider;

    @Bind(R.id.btn_socket_ipc)
    Button btnSocketIpc;

    @Bind(R.id.btn_network_binder)
    Button btnNetworkBinder;

    private IBookManager iBookManager;
    private INetwork iNetwork;
    private Messenger serviceMessenger;
    private IOnNewBookArrivedListener iOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Log.d(AIDLService.TAG, Thread.currentThread().getName() + "  new Book : " + newBook);
        }
    };

    private Handler clientMessengerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_STUDENTS:
                    msg.getData().setClassLoader(Student.class.getClassLoader());
                    ArrayList<Student> students = msg.getData().getParcelableArrayList(INTENT_KEY_STUDENTS);
                    assert students != null;
                    Log.d(AIDLService.TAG, students.toString());
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private Messenger clientMessenger = new Messenger(clientMessengerHandler);

    private ServiceConnection messengerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceMessenger = new Messenger(service);
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

    @Override
    protected void onStop() {
        try {
            iBookManager.unRegisterListener(iOnNewBookArrivedListener);
            unbindService(messengerConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    private void initListener() {
        buttonAidl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                        iBookManager = IBookManager.Stub.asInterface(
                                BinderPoolManager.getInstance(MainActivity.this).queryBinder(BinderPoolManager.BINDER_BOOK));
                        try {
                            iBookManager.registerListener(iOnNewBookArrivedListener);
                        } catch (RemoteException exception) {
                            exception.printStackTrace();
                        }
                    }
                }).subscribeOn(Schedulers.io()).subscribe();
            }
        });
        btnAidlAddBook.setOnClickListener(new View.OnClickListener() {
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
        btnAidlGetAllBook.setOnClickListener(new View.OnClickListener() {
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
        btnNetworkBinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flowable.just(1).map(new Function<Integer, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull Integer integer) throws Exception {
                        iNetwork = INetwork.Stub.asInterface(BinderPoolManager.getInstance(MainActivity.this)
                                .queryBinder(BinderPoolManager.BINDER_NETWORK));
                        iNetwork.connectNetwork();
                        return true;
                    }
                }).subscribeOn(Schedulers.io()).subscribe();
            }
        });
        btnMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MessengerService.class);
                bindService(intent, messengerConnection, BIND_AUTO_CREATE);
            }
        });

        btnMessengerAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = Message.obtain(null, MessengerService.MSG_ADD_STUDENT);
                Bundle bundle = new Bundle();
                bundle.putParcelable(MessengerService.INTENT_KEY_STUDENT, new Student("huangchen", 1));
                message.setData(bundle);
                message.replyTo = clientMessenger;
                try {
                    serviceMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        btnMessengerGetAllStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = Message.obtain(null, MSG_GET_STUDENTS);
                message.replyTo = clientMessenger;
                try {
                    serviceMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        btnProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getContentResolver().query(Uri.parse("content://com.hcjcch.ipctest.bookprovider/book"),
                        null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int id = cursor.getInt(cursor.getColumnIndex("_id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    Book book = new Book(id, name);
                    Log.d(BookProvider.TAG, book.toString());
                }
                Log.d(BookProvider.TAG, "cursor is null" + (cursor == null));
                try {
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (Exception ignore) {
                }
                Bundle bundle = getContentResolver().call(Uri.parse("content://com.hcjcch.ipctest.bookprovider/book"), "method", "parameter", null);
                assert bundle != null;
                Log.d(BookProvider.TAG, bundle.getString("callArg"));
            }
        });
    }
}
