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


## Android各种IPC通信方式
* [Intent](./Intent.md)
* AIDL
* Messenger
* ContentProvider
* Socket
