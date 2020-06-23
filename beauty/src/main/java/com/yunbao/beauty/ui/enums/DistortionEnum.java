package com.yunbao.beauty.ui.enums;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import com.yunbao.beauty.R;

@Keep
public enum DistortionEnum {
    NO_DISTORTION(0, R.string.distortion_no),
    ET_DISTORTION(1, R.string.distortion_et),
    PEAR_FACE_DISTORTION(2, R.string.distortion_pear_face),
    SLIM_FACE_DISTORTION(3, R.string.distortion_slim_face),
    SQUARE_FACE_DISTORTION(4, R.string.distortion_square_face);

    private int value;
    private int stringId;

    private DistortionEnum(int var3, int var4) {
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