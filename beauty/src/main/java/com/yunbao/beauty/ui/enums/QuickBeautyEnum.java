package com.yunbao.beauty.ui.enums;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yunbao.beauty.R;

public enum QuickBeautyEnum {

    ORIGIN_BEAUTY(0, R.string.quick_beauty_origin),
    STANDARD_BEAUTY(1, R.string.quick_beauty_standard),  //标准
    ELEGANT_BEAUTY(3, R.string.quick_beauty_elegant), //优雅
    EXQUISITE_BEAUTY(2, R.string.quick_beauty_exquisite),     //精致
    LOVELY_BEAUTY(4, R.string.quick_beauty_lovely),      //可爱
    NATURAL_BEAUTY(5, R.string.quick_beauty_natural), //自然
    ONLINE_BEAUTY(6, R.string.quick_beauty_online),      //网红
    REFINED_BEAUTY(7, R.string.quick_beauty_refined),     //脱俗
    SOLEMN_BEAUTY(8, R.string.quick_beauty_solemn);  //高雅

    private int value;
    private int stringId;

    private QuickBeautyEnum(int var3, int var4) {
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
