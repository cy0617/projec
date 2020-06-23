package com.yunbao.mall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

public class ManageClassBean implements Parcelable {

    private String mId;
    private String mName;
    private boolean mChecked;

    public ManageClassBean(){

    }


    @JSONField(name = "gc_id")
    public String getId() {
        return mId;
    }

    @JSONField(name = "gc_id")
    public void setId(String id) {
        mId = id;
    }

    @JSONField(name = "gc_name")
    public String getName() {
        return mName;
    }

    @JSONField(name = "gc_name")
    public void setName(String name) {
        mName = name;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
    }


    public ManageClassBean(Parcel in) {
        mId = in.readString();
        mName = in.readString();
    }

    public static final Creator<ManageClassBean> CREATOR = new Creator<ManageClassBean>() {
        @Override
        public ManageClassBean createFromParcel(Parcel in) {
            return new ManageClassBean(in);
        }

        @Override
        public ManageClassBean[] newArray(int size) {
            return new ManageClassBean[size];
        }
    };
}
