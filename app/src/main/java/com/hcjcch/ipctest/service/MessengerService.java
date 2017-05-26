package com.hcjcch.ipctest.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hcjcch.ipctest.message.Student;

import java.util.ArrayList;

import static com.hcjcch.ipctest.service.AIDLService.TAG;

/**
 * 使用 Messenger 通信的Service
 * <p>
 * Created by hcjcch on 2017/5/14.
 */

public class MessengerService extends Service {
    public static final int MSG_ADD_STUDENT = 1; //添加学生
    public static final int MSG_GET_STUDENTS = 2; //获取学生
    public static final String INTENT_KEY_STUDENT = "intent_key_student";
    public static final String INTENT_KEY_STUDENTS = "intent_key_students";

    private final Messenger messenger = new Messenger(new MessengerHandler());


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MessengerService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "MessengerService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    private static class MessengerHandler extends Handler {
        private ArrayList<Student> students = new ArrayList<>();

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ADD_STUDENT:
                    Log.d(TAG, "service add student");
                    msg.getData().setClassLoader(Student.class.getClassLoader());
                    Student student = msg.getData().getParcelable(INTENT_KEY_STUDENT);
                    students.add(student);

                    break;
                case MSG_GET_STUDENTS:
                    try {
                        Message message = Message.obtain(null, MSG_GET_STUDENTS);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(INTENT_KEY_STUDENTS, students);
                        message.setData(bundle);
                        Log.d(TAG, "messenger thread" + Thread.currentThread().getName());
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        msg.replyTo.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}
