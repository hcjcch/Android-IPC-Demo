### 基本方法
服务端实现
```java
public class MessengerService extends Service {
    public static final int MSG_ADD_STUDENT = 1; //添加学生
    public static final int MSG_GET_STUDENTS = 2; //获取学生
    public static final String INTENT_KEY_STUDENT = "intent_key_student";
    public static final String INTENT_KEY_STUDENTS = "intent_key_students";

    /**
    * 创建一个与Handler绑定的Messenger 
    */
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

    /**
    * 返回回Messenger中从的IBinder 
    */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    /**
    * 服务端的Handler，里面重写了handlerMessage方法，在里面处理客户端发来的消息
    */
    private static class MessengerHandler extends Handler {
        private ArrayList<Student> students = new ArrayList<>();

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ADD_STUDENT:
                    Log.d(TAG, "service add student");
                    /**
                    * 这里不设置classLoader就会classNotFound 
                    */
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
                        /**
                        * 这里休眠3s，测试服务端的阻塞不会导致客户端阻塞 
                        */
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        /**
                        *这里将服务端的消息发回给客户端，replyTo是从客户端传过来的Messenger
                        */
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
```
客户端代码
```java
public class MainActivity extends AppCompatActivity {
    /**
    * 服务端的Messenger，从ServiceConnection的onServiceConnected()方法中赋值
    */
    private Messenger serviceMessenger;
    
    /**
    * 客户端的Handler，要处理服务端发送回来的消息 
    */
    private Handler clientMessengerHandler = new Handler() {
           @Override
           public void handleMessage(Message msg) {
               switch (msg.what) {
                   case MSG_GET_STUDENTS:
                       /**
                       * 这里不设置classLoader就会classNotFound 
                       */
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
    /**
    * 客户端的Messenger,当发送给服务端message时并且需要服务器回应客户端，此时将这个Messenger设置为message的replyTo 
    */
    private Messenger clientMessenger = new Messenger(clientMessengerHandler);
    
    private ServiceConnection messengerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            /**
            * 调用Messenger的构造函数
            */
            serviceMessenger = new Messenger(service);
        }
    
        @Override
        public void onServiceDisconnected(ComponentName name) {
    
        }
    };
    private void message(){
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
                        /**
                        * 需要服务端给客户端回消息，需要将客户端的Messenger放在Message中传递给服务端 
                        */
                        message.replyTo = clientMessenger;
                        try {
                            serviceMessenger.send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        Log.d(BookProvider.TAG, "Messenger send for reply");
                    }
                });
    }
}
```
### 源码解析
### 参考文章
* [http://blog.csdn.net/yanbober/article/details/48373341](http://blog.csdn.net/yanbober/article/details/48373341)
* [https://www.diycode.cc/topics/361](https://www.diycode.cc/topics/361)