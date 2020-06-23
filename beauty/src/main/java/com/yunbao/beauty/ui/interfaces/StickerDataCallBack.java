package com.yunbao.beauty.ui.interfaces;

import java.util.List;

public interface StickerDataCallBack {
    void onStart();
    void onSuccess(List responseList);
    void onError(Exception e);
    void onCancel();
}
