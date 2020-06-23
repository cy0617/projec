package com.yunbao.beauty.ui.enums;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yunbao.beauty.R;

public enum  FilterEnum {

    NO_FILTER(0, R.string.filter_no),
    ROMANTIC_FILTER(1, R.string.filter_romantic),  //浪漫
    FRESH_FILTER(2, R.string.filter_fresh),     //清新
    BEAUTIFUL_FILTER(3, R.string.filter_beautiful), //唯美
    PINK_FILTER(4, R.string.filter_pink),      //粉嫩
    NOSTALGIC_FILTER(5, R.string.filter_nostalgic), //怀旧
    COOL_FILTER(6, R.string.filter_cool),      //清凉
    BLUES_FILTER(7, R.string.filter_blues),     //蓝调
    JAPANESE_FILTER(8, R.string.filter_japanese);  //日系

    private int value;
    private int stringId;

    private FilterEnum(int var3, int var4) {
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
