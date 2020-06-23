package com.yunbao.beauty.ui.bean;

/**
 * Created by kxr on 2019/10/31.
 */

public class SpeciallyBean {

    private String speciallyName;
    private int mImgSrc;
    private int[] mImgSrcs;
    private boolean mChecked;

    public SpeciallyBean() {
    }

    public SpeciallyBean(String speciallyName, int mImgSrc) {
        this.speciallyName = speciallyName;
        this.mImgSrc = mImgSrc;
    }

    public SpeciallyBean(String speciallyName,int mImgSrc, boolean mChecked) {
        this.speciallyName = speciallyName;
        this.mImgSrc = mImgSrc;
        this.mChecked = mChecked;
    }

    public String getSpeciallyName() {
        return speciallyName;
    }

    public void setSpeciallyName(String speciallyName) {
        this.speciallyName = speciallyName;
    }

    public int getmImgSrc() {
        return mImgSrc;
    }

    public void setmImgSrc(int mImgSrc) {
        this.mImgSrc = mImgSrc;
    }

    public int[] getmImgSrcs() {
        return mImgSrcs;
    }

    public void setmImgSrcs(int[] mImgSrcs) {
        this.mImgSrcs = mImgSrcs;
    }

    public boolean ismChecked() {
        return mChecked;
    }

    public void setmChecked(boolean mChecked) {
        this.mChecked = mChecked;
    }
}
