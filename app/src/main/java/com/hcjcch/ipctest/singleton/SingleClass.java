package com.hcjcch.ipctest.singleton;

/**
 * 含有静态变量的类，测试静态变量在多进程中失效的问题
 * <p>
 * Created by hcjcch on 2017/5/11.
 */

public class SingleClass {
    public static int a = 2;
}
