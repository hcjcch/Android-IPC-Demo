package com.hcjcch.ipctest.message;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p> 多进程通信的实体类 Student ，为 Messenger 通信方式使用
 * <p>
 * Created by hcjcch on 2017/5/14.
 */

public class Student implements Parcelable {
    private String name;
    private int id;

    public Student() {
    }

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
    }

    protected Student(Parcel in) {
        name = in.readString();
        id = in.readInt();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
