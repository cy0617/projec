package com.yunbao.beauty.ui.enums;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yunbao.beauty.R;

public enum BeautyTypeEnum {

    //beautyTabs
    BEAUTY_TYPE_ENUM(0, R.string.beauty), //美颜
    SHAPE_TYPE_ENUM(1, R.string.beauty_shape),  //美型
    ONKEY_TYPE_ENUM(2, R.string.beauty_one_key),  //一键美颜
    FILTER_TYPE_ENUM(3, R.string.filter),  //滤镜
    //speciallyTabs
    WATER_TYPE_ENUM(5, R.string.beauty_water),  //水印
    SPECIALLY_TYPE_ENUM(4, R.string.beauty_specially),  //特效

    //distortionTaabs
    DISTORTION_TYPE_ENUM(6, R.string.beauty_distortion);  //哈哈镜

    private int value;
    private int stringId;

    private BeautyTypeEnum(int var3, int var4) {
        this.value = var3;
        this.stringId = var4;
    }

    public int getValue() {
        return this.value;
    }

    public int getStringId() {
        return this.stringId;
    }

    public String getString(@NonNull Context var1) {
        return var1.getResources().getString(this.stringId);
    }

}
