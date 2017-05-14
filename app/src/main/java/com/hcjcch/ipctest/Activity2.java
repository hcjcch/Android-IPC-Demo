package com.hcjcch.ipctest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hcjcch.ipctest.singleton.SingleClass;

/**
 * 请用一句话描述
 * <p>
 * Created by hcjcch on 2017/5/11.
 */

public class Activity2 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        Log.d("huangchen", "a = " + SingleClass.a);
    }
}
