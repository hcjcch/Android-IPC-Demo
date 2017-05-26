# Android-IPC-Demo
#### 采用多进程的原因

* 应用自身的原因(一些不稳定的代码如WebView，获取多份内存等)

* 与其他应用进行数据的交换（系统应用：通讯录、图库，第三方应用：新闻客户端等等）

#### 开启多进程
```

    android:process=":xxx"
    
    或者
    
    android:process="xxx"
    
``` 
":xx"方式开启的进程是私有进程，只能本应用的组件可以共同运行在这个进程中。其他应用的组件无法运行在这个进程中，因为它们根本无法访问到这个进程

"xx"这种方式开启的进程全局进程，其他应用可以通过shareUID的方式运行的这一个进程中。并且这些共用shareUID的应用签名必须一致

##### 多进程的问题
* 静态成员和单例模式完全失效
* 线程同步机制完全失效
* SharePreferences 的可靠性下降
* Application 会多次创建

运行在同一个进程中的组建属于同一个虚拟机和同一个Application

## 其他知识

* [序列化](./Serialization.md)

## Android各种IPC通信方式
* [Bundle](./Intent.md)
* [AIDL](./AIDL.md)
* [File Share](FILE_SHARE.md)
* [Messenger](MESSENGER.md)
* ContentProvider
* Socket
