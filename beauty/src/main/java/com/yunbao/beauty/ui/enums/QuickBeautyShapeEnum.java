package com.yunbao.beauty.ui.enums;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yunbao.beauty.R;

public enum QuickBeautyShapeEnum {

    BEAUTY_WHITE(0, R.string.beauty_meibai),//美白
    BEAUTY_GRIND(1, R.string.beauty_mopi),  //磨皮
    BEAUTY_TENDER(3, R.string.beauty_hongrun), //粉嫩
    SHAPE_BIGEYE(2, R.string.beauty_big_eye),     //大眼
    SHAPE_EYEBROW(4, R.string.beauty_eye_brow),      //眼眉
    SHAPE_EYELENGTH(5, R.string.beauty_eye_length), //眼距
    SHAPE_EYECORNER(6, R.string.beauty_eye_corner),  //眼角
    SHAPE_FACE(7, R.string.beauty_face),     //瘦脸
    SHAPE_MOUSE(8, R.string.beauty_mouse),  //嘴型
    SHAPE_NOSE(9, R.string.beauty_nose),  //鼻子
    SHAPE_CHIN(10, R.string.beauty_chin),  //下巴
    SHAPE_FOREHEAD(11, R.string.beauty_forehead),  //额头
    SHAPE_LENGTHNOSE(12, R.string.beauty_lengthen_nose),  //长鼻
    SHAPE_SHAVE_FACE(13, R.string.beauty_face_shave),  //削脸
    SHAPE_EYEALAT(14, R.string.beauty_eye_alat);  //开眼角

    private int value;
    private int stringId;

    private QuickBeautyShapeEnum(int var3, int var4) {
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
