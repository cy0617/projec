package com.yunbao.beauty.ui.views;

import android.content.Context;
import android.view.ViewGroup;

import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.utils.ToastUtil;
import com.yunbao.beauty.ui.interfaces.StickerCanClickListener;

public class BeautyViewHolderFactory {

    public static BaseBeautyViewHolder getBeautyViewHolder(Context context, ViewGroup viewGroup, StickerCanClickListener canClickListener) {
        BaseBeautyViewHolder beautyViewHolder;
        String ver = MHSDK.getInstance().getVer();
        if (ver == null) {
            ToastUtil.show(MHSDK.getInstance().getAppContext(), "授权校验异常");
            ver = "-1";
        }
        switch (ver) {
            case "0":
                beautyViewHolder = new DefaultBeautyViewHolder(context, viewGroup);
                break;
            case "1":
                beautyViewHolder = new BeautyViewHolder(context, viewGroup,canClickListener);
                break;
            default:
                beautyViewHolder = new DefaultBeautyViewHolder(context, viewGroup);
                break;
        }
        return beautyViewHolder;
    }
}
