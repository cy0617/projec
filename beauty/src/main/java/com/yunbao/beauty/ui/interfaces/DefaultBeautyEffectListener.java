package com.yunbao.beauty.ui.interfaces;

import com.yunbao.beauty.ui.enums.FilterEnum;

/**
 * Created by cxf on 2018/10/8.
 * 基础美颜回调
 */

public interface DefaultBeautyEffectListener extends BeautyEffectListener {

    void onFilterChanged(FilterEnum filterEnumEnum);

    void onMeiBaiChanged(int progress);

    void onMoPiChanged(int progress);

    void onFengNenChanged(int progress);

    void onBeautyOrigin();

}
