package com.yunbao.beauty.views;

import android.graphics.Bitmap;

import com.yunbao.beauty.ui.interfaces.DefaultBeautyEffectListener;


public interface MHProjectBeautyEffectListener extends DefaultBeautyEffectListener {
    public void onFilterChanged(Bitmap bitmap);
}
